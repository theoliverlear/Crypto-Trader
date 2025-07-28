import logging
from datetime import datetime
from typing import Any

import pandas as pd
from attr import attr
from attrs import define
from pandas import DataFrame
import tensorflow as tf
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm.base_model import BaseModel
from apps.models.ai.lstm.complex_lstm_model import ComplexLstmModel
from apps.models.ai.lstm.layered.complex_multi_layer_lstm_model import \
    ComplexMultiLayerLstmModel
from apps.models.ai.lstm.layered.multi_layer_base_model import MultiLayerBaseModel
from apps.models.ai.lstm.layered.multi_layer_lstm_model import \
    MultiLayerLstmModel
from apps.models.ai.lstm.lstm_model import LstmModel
from apps.models.ai.model_retriever import get_lstm_model, \
    get_complex_lstm_model, get_multi_layer_lstm_model, \
    get_complex_multi_layer_lstm_model
from apps.models.ai.model_type import ModelType
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.database import Database
from apps.models.database.query_load import QueryLoad
from apps.models.prediction.predictions import log_actual_vs_printed
from apps.models.training.training_model import TrainingModel


@define
class TrainingSession:
    model: BaseModel = attr(default=None)
    gpu_id: int = attr(default=0)
    training_model: TrainingModel = attr(default=None)
    training_start_time: datetime = attr(default=None)
    training_end_time: datetime = attr(default=None)
    query_start_time: datetime = attr(default=None)
    query_end_time: datetime = attr(default=None)
    database: Database = attr(default=Database())
    target_currency: str = attr(default='BTC')
    prediction_id: int = attr(default=0)
    actual_rows: int = attr(default=0)
    starting_loss: float = attr(default=0.0)
    final_loss: float = attr(default=0.0)
    epochs_trained: int = attr(default=0)
    dimension_width: int = attr(default=1)
    query_load: QueryLoad = attr(default=QueryLoad.BULK)
    query_batch_size: int = attr(default=-1)


    def _get_dataframe(self) -> pd.DataFrame:
        logging.info("Getting dataframe...")
        self.query_start_time = datetime.now()
        dataframe: pd.DataFrame = self.database.fetch_data(self.target_currency,
                                        self.training_model.max_rows,
                                        self.training_model.query_type)
        self.query_end_time = datetime.now()
        dataframe.dropna(subset=[f"{self.target_currency.lower()}_price"], inplace=True)
        self.actual_rows = len(dataframe)
        return dataframe

    def _get_batched_dataframe(self) -> list[pd.DataFrame]:
        logging.info("Getting batched dataframe...")
        self.query_load = QueryLoad.BATCHES
        batch_size: int = self.training_model.max_rows // 10
        if isinstance(self.model, ComplexLstmModel) \
            or isinstance(self.model, MultiLayerLstmModel) or \
               isinstance(self.model, ComplexMultiLayerLstmModel):
            batch_size = min(batch_size, 15_000)
        self.query_batch_size = batch_size
        self.query_start_time = datetime.now()
        batches: list[pd.DataFrame] = self.database.fetch_data_in_batches(batch_size,
                                                      self.target_currency,
                                                      self.training_model.max_rows,
                                                      self.training_model.query_type)
        self.query_end_time = datetime.now()
        batches = [batch.dropna(subset=[f"{self.target_currency.lower()}_price"]) for batch in batches]
        self.actual_rows = sum(len(batch) for batch in batches)
        return batches

    def train(self,
              dataframe: pd.DataFrame | None,
              in_batches: bool = False,
              use_previous_model: bool = True) -> None:
        if in_batches:
            self._train_in_batches()
            return
        logging.info(f"Training model for {self.target_currency}...")
        if dataframe is None:
            dataframe: pd.DataFrame = self._get_dataframe()
        if dataframe.empty:
            logging.warning(f"No data available for {self.target_currency}. Skipping training.")
            return
        dataset: tf.data.Dataset
        target_scaler: MinMaxScaler
        dataset, target_scaler, historical_prices = self._transform_to_dataset(dataframe)
        self.model = self._get_model(historical_prices, use_previous_model)
        self.training_start_time = datetime.now()
        with tf.device(f"/GPU:{self.gpu_id}"):
            logging.info(f"Training on GPU...")
            history = self.model.train(dataset,
                                       self.training_model.epochs,
                                       self.training_model.batch_size)
        self.training_end_time = datetime.now()
        logging.info(f"Training completed for {self.target_currency}.")
        self.save_model()
        predicted_price: float = self.predict(target_scaler, historical_prices)
        log_actual_vs_printed(self.target_currency,
                              predicted_price,
                              ModelType.from_instance(self.model))
        self.capture_history_data(history)

    def capture_history_data(self, history):
        losses = history.history.get('loss', [])
        self.epochs_trained = len(losses)
        self.starting_loss = losses[0]
        self.final_loss = losses[-1]

    def predict(self, target_scaler: MinMaxScaler, last_sequence: Any) -> float:
        predicted_price: float = self.model.predict(last_sequence, target_scaler)
        logging.debug(f"Predicted Next {self.target_currency} Price: ${predicted_price:,}")
        return predicted_price


    def save_model(self) -> None:
        logging.info(f"Saving model for {self.target_currency}...")
        model_path: str = self.model.get_model_path(self.target_currency)
        self.model.save_model(model_path)

    def _train_in_batches(self) -> None:
        batched_dataframes: list[pd.DataFrame] = self._get_batched_dataframe()
        for dataframe in batched_dataframes:
            self.train(dataframe, in_batches=False)

    def _transform_to_dataset(self, dataframe: pd.DataFrame) -> tuple[tf.data.Dataset, MinMaxScaler, Any]:
        logging.info("Transforming data...")
        preprocessor: Preprocessor = Preprocessor()
        historical_prices, future_prices_unscaled, input_scaler = (
            preprocessor.transform(dataframe, self.target_currency))
        logging.info("Scaling target values...")
        target_scaler: MinMaxScaler = MinMaxScaler(feature_range=(0, 1))
        logging.info("Getting raw target values...")
        raw_target_vals = self._get_currency_prices(dataframe, self.target_currency)
        logging.info("Fitting target scaler...")
        target_scaler.fit(raw_target_vals)
        logging.info("Scaling future prices...")
        future_prices_scaled = target_scaler.transform(
            self._rescale_future_prices(future_prices_unscaled)).ravel()
        dataset = self._scaled_as_dataset(historical_prices, future_prices_scaled)
        shape_str = f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, " \
                 f"future_prices shape: {future_prices_unscaled.shape}"
        logging.info(shape_str)
        return dataset, target_scaler, historical_prices

    def _scaled_as_dataset(self, historical_prices, future_prices_scaled) -> tf.data.Dataset:
        logging.info("Creating TensorFlow dataset...")
        dataset: tf.data.Dataset = tf.data.Dataset.from_tensor_slices(
            (historical_prices, future_prices_scaled))
        dataset = dataset.cache().shuffle(1024).batch(
            self.training_model.batch_size).prefetch(tf.data.AUTOTUNE)
        return dataset

    def _get_model(self, historical_prices: DataFrame, use_previous_model: bool = True):
        logging.info("Getting model...")
        model_path: str = self.model.get_model_path(self.target_currency)
        if isinstance(self.model, LstmModel):
            model = get_lstm_model(self.target_currency,
                                   model_path,
                                   historical_prices,
                                   self.training_model,
                                   use_previous_model)
        elif isinstance(self.model, ComplexLstmModel):
            model = get_complex_lstm_model(self.target_currency,
                                           model_path,
                                           historical_prices,
                                           self.training_model,
                                           use_previous_model)
        elif isinstance(self.model, MultiLayerLstmModel):
            model = get_multi_layer_lstm_model(self.target_currency,
                                               model_path,
                                               historical_prices,
                                               self.training_model,
                                               use_previous_model)
        elif isinstance(self.model, ComplexMultiLayerLstmModel):
            model = get_complex_multi_layer_lstm_model(self.target_currency,
                                                       model_path,
                                                       historical_prices,
                                                       self.training_model,
                                                       use_previous_model)
        else:
            raise ValueError(f"Unsupported model type: {type(self.model)}")
        return model

    def get_model_type(self) -> ModelType:
        return ModelType.from_instance(self.model)

    def to_json(self) -> dict:
        date_format: str = "%Y-%m-%dT%H:%M:%S"
        return {
            "currency": self.target_currency,
            "prediction": self.prediction_id,
            "numRows": self.actual_rows,
            "epochsTrained": self.epochs_trained,
            "maxEpochs": self.training_model.epochs,
            "startingLoss": self.starting_loss,
            "finalLoss": self.final_loss,
            "modelType": str(self.get_model_type().value),
            "queryType": self.training_model.query_type.value,
            "trainingStartTime": self.training_start_time.strftime(date_format),
            "trainingEndTime": self.training_end_time.strftime(date_format),
            "queryStartTime": self.query_start_time.strftime(date_format),
            "queryEndTime": self.query_end_time.strftime(date_format),
            "sequenceLength": self.training_model.sequence_length,
            "batchSize": self.training_model.batch_size,
            "dimensionWidth": self.dimension_width,
            "queryLoad": self.query_load.value,
            "queryBatchSize": self.query_batch_size,
            "trainingDevice": f"gpu_{self.gpu_id}"
        }

    @staticmethod
    def _get_currency_prices(dataframe: pd.DataFrame, target_currency: str):
        return dataframe[[f"{target_currency.lower()}_price"]].values

    @staticmethod
    def _rescale_future_prices(future_prices_unscaled):
        return future_prices_unscaled.reshape(-1, 1)