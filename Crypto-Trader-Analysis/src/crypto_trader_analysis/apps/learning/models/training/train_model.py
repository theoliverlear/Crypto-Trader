# train_model.py
import logging
import os

import sys
os.environ["TF_XLA_FLAGS"] = "--tf_xla_auto_jit=2"
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"
os.environ["TF_ENABLE_ONEDNN_OPTS"] = "1"

import tensorflow as tf
tf.config.optimizer.set_jit(True)
tf.get_logger().setLevel('ERROR')

import numpy as np
from pandas import DataFrame
from sklearn.preprocessing import MinMaxScaler

from src.crypto_trader_analysis.apps.learning.models.ai.lstm.complex_lstm_model import ComplexLstmModel
from src.crypto_trader_analysis.apps.learning.models.ai.lstm.lstm_model import LstmModel
from src.crypto_trader_analysis.apps.learning.models.ai.model_type import ModelType
from src.crypto_trader_analysis.apps.learning.models.data.preprocessor import Preprocessor
from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType
from src.crypto_trader_analysis.apps.learning.models.ai.model_retriever import get_model, get_lstm_model, \
    get_complex_lstm_model
from src.crypto_trader_analysis.apps.learning.models.training.training_type import TrainingType
from src.crypto_trader_analysis.apps.learning.models.currency_json_generator import get_all_currency_codes


import tensorflow as tf
tf.config.optimizer.set_jit(True)
tf.function(jit_compile=True)

SMALL_DATASET_SIZE = 1
# SMALL_DATASET_SIZE = 999

#--------------------------------Setup-Logging--------------------------------
def setup_logging():
    logging.basicConfig(
        level=logging.DEBUG,
        format='[%(asctime)s] [%(levelname)s] %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S',
        handlers=[logging.StreamHandler(sys.stdout)]
    )
    logging.debug("Logging setup complete: Debug mode is enabled.")

#----------------------------Configure-Concurrency----------------------------
def configure_concurrency():
    os.environ["TF_XLA_FLAGS"] = "--tf_xla_auto_jit=2"
    os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"
    os.environ["TF_ENABLE_ONEDNN_OPTS"] = "1"

#----------------------------Setup-Tensorflow-Env-----------------------------
def setup_tensorflow_env():
    configure_concurrency()
    if tf.config.optimizer.get_jit():
        print("XLA JIT is enabled")
    else:
        print("XLA JIT is NOT enabled")

    gpus = tf.config.list_physical_devices('GPU')
    if gpus:
        logging.info(f"{len(gpus)} GPU(s) detected.")
        for index, pc_gpu in enumerate(gpus):
            tf.config.experimental.set_memory_growth(pc_gpu, True)
    else:
        logging.info("No DML devices detected.")
    if not gpus:
        logging.warning("No GPU devices detected. Running on CPU.")
    logging.info(f"TensorFlow Configured for CPU Cores and {len(gpus)} GPU(s).")

#----------------------------Get-Untrained-Models-----------------------------
def get_untrained_models() -> list[str]:
    from src.crypto_trader_analysis.apps.learning.models.prediction.predictions import model_exists
    currencies: list[str] = get_all_currency_codes(False)
    untrained_models: list[str] = []
    for currency in currencies:
        if not model_exists(currency):
            untrained_models.append(currency)
    return untrained_models

#------------------------------Train-New-Models-------------------------------
def train_new_models(currency_codes: list[str] = None,
                     training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                     model_type: ModelType = ModelType.LSTM,
                     gpu_id: int = 0):
    for currency in get_untrained_models():
        if currency_codes:
            if currency in currency_codes:
                logging.info(f"Training model for {currency}...")
                train_model(target_currency=currency,
                            training_type=training_type,
                            model_type=model_type,
                            gpu_id=gpu_id)
                logging.info(f"Model for {currency} trained and saved.")
        else:
            logging.info(f"Training model for {currency}...")
            train_model(target_currency=currency,
                        training_type=training_type,
                        model_type=model_type,
                        gpu_id=gpu_id)
            logging.info(f"Model for {currency} trained and saved.")

#---------------------------Train-Inaccurate-Models---------------------------
def train_inaccurate_models(training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                            model_type: ModelType = ModelType.LSTM,
                            gpu_id: int = 0,
                            only_recent_predictions: bool = False):
    from src.crypto_trader_analysis.apps.learning.models.database.database import Database
    db: Database = Database()
    inaccurate_models: list[str] = db.get_inaccurate_models(5, only_recent_predictions)
    if gpu_id == 1:
        inaccurate_models.reverse()
    logging.info(f"Found {len(inaccurate_models)} inaccurate models.")

    for currency in inaccurate_models:
        logging.info(f"Retraining model for {currency}...")
        train_model(target_currency=currency,
                    training_type=training_type,
                    model_type=model_type,
                    gpu_id=gpu_id)
        logging.info(f"Model for {currency} retrained and saved.")

#--------------------------------Get-Dataframe--------------------------------
def get_dataframe(target_currency: str = 'BTC',
                  limit: int = 2000,
                  query_type: QueryType = QueryType.HISTORICAL_PRICE) -> DataFrame:
    from src.crypto_trader_analysis.apps.learning.models.database.database import Database
    logging.info("Creating database connection...")
    db: Database = Database()
    logging.info("Fetching data frame...")
    df: DataFrame = db.fetch_data(target_currency, limit, query_type)
    return df

#----------------------------Get-Batched-Dataframe----------------------------
def get_batched_dataframe(target_currency: str = 'BTC',
                          limit: int = 2000,
                          query_type: QueryType = QueryType.HISTORICAL_PRICE,
                          batch_size: int = 10000) -> list[DataFrame]:
    from src.crypto_trader_analysis.apps.learning.models.database.database import Database
    logging.info("Creating database connection...")
    db: Database = Database()
    logging.info("Fetching batched data frame...")
    df: list[DataFrame] = db.fetch_data_in_batches(batch_size, target_currency, limit, query_type)
    return df

#--------------------------Get-Last-Historical-Price--------------------------
def get_last_historical_price(historical_prices):
    return np.array([historical_prices[-1]])

#---------------------------Get-Last-Currency-Price---------------------------
def get_last_currency_price(dataframe: DataFrame, target_currency: str):
    return dataframe[f"{target_currency}_price"].iloc[-1]

#---------------------------Train-Model-In-Batches----------------------------
def train_model_in_batches(target_currency: str = 'BTC',
                           training_type: TrainingType=TrainingType.BALANCED_MEDIUM_TRAINING,
                           model_type: ModelType=ModelType.LSTM,
                           gpu_id: int = 0,
                           use_previous_model: bool = True,
                           batch_size: int = 10000) -> None:
    if model_type == ModelType.COMPLEX_LSTM:
        batch_size = min(batch_size, 15000)
    dataframe = get_batched_dataframe(target_currency, limit=training_type.value.max_rows, query_type=QueryType.HISTORICAL_PRICE, batch_size=batch_size)
    for batch in dataframe:
        logging.info(f"Training model for {target_currency} with batch size {batch_size}...")
        if len(batch) < SMALL_DATASET_SIZE and training_type.value.skip_small_samples:
            logging.warning(f"Not enough data to train model for {target_currency}. Skipping batch...")
            continue
        train_model(target_currency, training_type, model_type, gpu_id, use_previous_model, batch)
        logging.info(f"Batch training completed for {target_currency}.")

#---------------------------------Train-Model---------------------------------
def train_model(target_currency: str = 'BTC',
                training_type: TrainingType=TrainingType.BALANCED_MEDIUM_TRAINING,
                model_type: ModelType=ModelType.LSTM,
                gpu_id: int = 0,
                use_previous_model: bool = True,
                dataframe: DataFrame = None):
    from src.crypto_trader_analysis.apps.learning.models.prediction.predictions import log_actual_vs_printed
    logging.info("Getting data frame...")
    if dataframe is None:
        dataframe = get_dataframe(target_currency, limit=training_type.value.max_rows, query_type=QueryType.HISTORICAL_PRICE)
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    if len(dataframe) < SMALL_DATASET_SIZE and training_type.value.skip_small_samples:
        logging.warning(f"Not enough data to train model for {target_currency}. Skipping...")
        return
    logging.info("Configuring preprocessor...")
    preprocessor = Preprocessor()
    logging.info("Transforming data...")
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    logging.info("Dropping NaN values...")
    dataframe.dropna(subset=[f"{target_currency.lower()}_price"], inplace=True)
    logging.info("Scaling target values...")
    target_scaler = MinMaxScaler(feature_range=(0,1))
    logging.info("Getting raw target values...")
    raw_target_vals = get_currency_prices(dataframe, target_currency)
    logging.info("Fitting target scaler...")
    target_scaler.fit(raw_target_vals)
    logging.info("Scaling future prices...")
    future_prices_scaled = target_scaler.transform(rescale_future_prices(future_prices_unscaled)).ravel()
    dataset = tf.data.Dataset.from_tensor_slices(
        (historical_prices, future_prices_scaled))
    dataset = dataset.cache().shuffle(1024).batch(
        training_type.value.batch_size).prefetch(tf.data.AUTOTUNE)

    logging.info(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}")
    if model_type == ModelType.LSTM:
        model_path: str = LstmModel.get_model_path(target_currency)
    elif model_type == ModelType.COMPLEX_LSTM:
        model_path: str = ComplexLstmModel.get_model_path(target_currency)
    else:
        raise ValueError(f"Incompatible model type: {model_type}")
    logging.info(f"Getting model for {target_currency} at {model_path}...")
    if model_type == ModelType.LSTM:
        model: LstmModel = get_lstm_model(target_currency, model_path, historical_prices, training_type.value, use_previous_model)
    elif model_type == ModelType.COMPLEX_LSTM:
        model: ComplexLstmModel = get_complex_lstm_model(target_currency, model_path, historical_prices, training_type.value, use_previous_model)
    else:
        raise ValueError(f"Incompatible model type: {model_type}")
    logging.info(f"Training model for {target_currency}...")
    with tf.device(f'/GPU:{gpu_id}'):
        logging.info(f"Training on GPU...")
        model.train(dataset, epochs=training_type.value.epochs, batch_size=training_type.value.batch_size)
    logging.info(f"Training completed for {target_currency}.")
    logging.info(f"Saving model for {target_currency}...")
    model.save_model(model_path)
    logging.info(f"Getting last sequence.")
    last_sequence = get_last_historical_price(historical_prices)
    logging.info(f"Predicting currency price.")
    predicted_price = model.predict(last_sequence, target_scaler)
    logging.debug(f"Predicted Next {target_currency} Price: {predicted_price}")
    log_actual_vs_printed(target_currency, predicted_price, model_type)

#-----------------------------Get-Currency-Prices-----------------------------
def get_currency_prices(dataframe, target_currency):
    return dataframe[[f"{target_currency.lower()}_price"]].values

#----------------------------Rescale-Future-Prices----------------------------
def rescale_future_prices(future_prices_unscaled):
    return future_prices_unscaled.reshape(-1, 1)

#------------------------------Train-All-Models-------------------------------
def train_all_models(training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                     currency_codes: list[str] = None,
                     model_type: ModelType = ModelType.LSTM,
                     gpu_id: int = 0,
                     in_batches: bool = False,
                     batch_size: int = 10000):
    for currency_code in currency_codes:
        logging.debug(f"Training model for {currency_code}...")
        if model_type == ModelType.LSTM or model_type == ModelType.COMPLEX_LSTM:
            if in_batches:
                train_model_in_batches(currency_code,
                                       training_type,
                                       model_type,
                                       gpu_id,
                                       use_previous_model=False,
                                       batch_size=batch_size)
            else:
                train_model(currency_code, training_type, gpu_id=gpu_id, model_type=model_type)
        elif model_type == ModelType.MULTI_LAYER or model_type == ModelType.COMPLEX_MULTI_LAYER:
            if in_batches:
                train_multi_layer_model_in_batches(currency_code,
                                                   10,
                                                   50,
                                                   150,
                                                   training_type,
                                                   model_type,
                                                   gpu_id,
                                                   batch_size)
            else:
                train_multi_layer_model(currency_code,
                                        10,
                                        50,
                                        150,
                                        training_type,
                                        model_type,
                                        gpu_id)
        logging.debug(f"Model for {currency_code} trained successfully.")

#------------------------------Full-Train-Model-------------------------------
def full_train_model(target_currency: str = 'BTC', gpu_id: int = 0, model_type: ModelType = ModelType.LSTM):
    for training_type in TrainingType:
        logging.debug(f"Training model for {target_currency} with training type {training_type}...")
        train_model(target_currency, training_type, gpu_id=gpu_id, model_type=model_type)
        logging.debug(f"Model for {target_currency} trained successfully with training type {training_type}.")
    train_model(target_currency, TrainingType.LARGE_DATA_DETAILED_TRAINING)

#--------------------------Full-Train-Layered-Model---------------------------
def full_train_layered_model(target_currency: str = 'BTC',
                             short_seq: int = 10,
                             medium_seq: int = 50,
                             long_seq: int = 150,
                             model_type: ModelType = ModelType.MULTI_LAYER,
                             gpu_id: int = 0):
    for training_type in TrainingType:
        logging.debug(f"Training model for {target_currency} with training type {training_type}...")
        train_multi_layer_model(target_currency, short_seq, medium_seq, long_seq, training_type, model_type, gpu_id)
        logging.debug(f"Model for {target_currency} trained successfully with training type {training_type}.")
    train_multi_layer_model(target_currency,
                            short_seq,
                            medium_seq,
                            long_seq,
                            TrainingType.LARGE_DATA_DETAILED_TRAINING,
                            model_type,
                            gpu_id)

#----------------------------Full-Train-All-Models----------------------------
def full_train_all_models(currency_codes: list[str] = None,
                          gpu_id: int = 0,
                          model_type: ModelType = ModelType.LSTM):
    for currency_code in currency_codes:
        if model_type == ModelType.LSTM or model_type == ModelType.COMPLEX_LSTM:
            logging.debug(f"Training model for {currency_code}...")
            full_train_model(currency_code, gpu_id=gpu_id, model_type=model_type)
            logging.debug(f"Model for {currency_code} trained successfully.")
        elif model_type == ModelType.MULTI_LAYER or model_type == ModelType.COMPLEX_MULTI_LAYER:
            logging.debug(f"Training multi-layer model for {currency_code}...")
            full_train_layered_model(currency_code, gpu_id=gpu_id, model_type=model_type)
            logging.debug(f"Multi-layer model for {currency_code} trained successfully.")

#---------------------Train-Multi-Layer-Model-In-Batches----------------------
def train_multi_layer_model_in_batches(target_currency: str = 'BTC',
                                       short_seq: int = 10,
                                       medium_seq: int = 50,
                                       long_seq: int = 150,
                                       training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                                       model_type: ModelType = ModelType.MULTI_LAYER,
                                       gpu_id: int = 0,
                                       batch_size: int = 10000):
    batch_size = min(batch_size, 15000)

    dataframe = get_batched_dataframe(target_currency,
                                        limit=training_type.value.max_rows,
                                        query_type=QueryType.HISTORICAL_PRICE,
                                        batch_size=batch_size)
    for batch in dataframe:
        logging.info(f"Training multi-layer model for {target_currency} with batch size {batch_size}...")
        if len(batch) < SMALL_DATASET_SIZE and training_type.value.skip_small_samples:
            logging.warning(f"Not enough data to train model for {target_currency}. Skipping batch...")
            continue
        train_multi_layer_model(target_currency, short_seq, medium_seq, long_seq, training_type, model_type, gpu_id, batch)
        logging.info(f"Batch training completed for {target_currency}.")

#---------------------------Train-Multi-Layer-Model---------------------------
def train_multi_layer_model(target_currency: str = 'BTC',
                            short_seq: int = 10,
                            medium_seq: int = 50,
                            long_seq: int = 150,
                            training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                            model_type: ModelType = ModelType.MULTI_LAYER,
                            gpu_id: int = 0,
                            dataframe: DataFrame = None):
    from src.crypto_trader_analysis.apps.learning.models.prediction.predictions import log_actual_vs_printed
    from src.crypto_trader_analysis.apps.learning.models.ai.lstm.layered.multi_layer_lstm_model import MultiLayerLstmModel
    from src.crypto_trader_analysis.apps.learning.models.ai.lstm.layered.complex_multi_layer_lstm_model import ComplexMultiLayerLstmModel

    logging.info(f"Fetching data frame for {target_currency}")
    if dataframe is None:
        dataframe = get_dataframe(target_currency,
                                  limit=training_type.value.max_rows,
                                  query_type=training_type.value.query_type or QueryType.HISTORICAL_PRICE)
    if dataframe.empty:
        logging.warning(f"No data for {target_currency}. Cannot train multi-layer model.")
        return
    logging.debug(f"Dataframe has {len(dataframe)} rows for {target_currency}.")
    preprocessor = Preprocessor()
    short_data, med_data, long_data, future_prices, input_scaler = \
        preprocessor.transform_multi_scale_with_weights(
            dataframe,
            target_currency,
            short_seq=short_seq,
            medium_seq=medium_seq,
            long_seq=long_seq
        )
    if short_data.shape[0] == 0:
        logging.warning(f"No samples produced for {target_currency} in multi-scale transform.")
        return
    target_scaler = MinMaxScaler(feature_range=(0,1))
    future_prices_scaled = target_scaler.fit_transform(future_prices.reshape(-1,1)).ravel()

    if model_type == ModelType.MULTI_LAYER:
        model_path = MultiLayerLstmModel.get_model_path(target_currency)
        model_class = MultiLayerLstmModel
    elif model_type == ModelType.COMPLEX_MULTI_LAYER:
        model_path = ComplexMultiLayerLstmModel.get_model_path(target_currency)
        model_class = ComplexMultiLayerLstmModel
    else:
        raise ValueError(f"Incompatible model type: {model_type}")

    dataset = tf.data.Dataset.from_tensor_slices((
        (short_data, med_data, long_data),
        future_prices_scaled
    ))
    dataset = dataset.cache().shuffle(1024) \
        .batch(training_type.value.batch_size) \
        .prefetch(tf.data.AUTOTUNE)

    model = get_model(model_class,
                      target_currency,
                      model_path,
                      short_data,
                      training_type.value)

    logging.info(f"Training Multi-Layer Model for {target_currency}...")
    with tf.device(f'/GPU:{gpu_id}'):
        logging.info(f"Training on GPU...")
        model.train(
            dataset,
            epochs=training_type.value.epochs,
            batch_size=training_type.value.batch_size)

    logging.info(f"Saving Multi-Layer Model to {model_path}")
    model.save_model(model_path)
    last_long, last_medium, last_short = get_last_multi_layer_elements(long_data, med_data, short_data)
    predicted_price = model.predict([last_short, last_medium, last_long],
                                    target_scaler=target_scaler)
    log_actual_vs_printed(target_currency, predicted_price)

# TODO: Create a training log API to display when a model was trained and any
#       details about the training.

#------------------------Get-Last-Multi-Layer-Elements------------------------
def get_last_multi_layer_elements(long_data, med_data, short_data):
    last_short = np.array([short_data[-1]])
    last_medium = np.array([med_data[-1]])
    last_long = np.array([long_data[-1]])
    return last_long, last_medium, last_short

#---------------------------Get-Split-Currency-List---------------------------
def get_split_currency_list():
    currency_codes = get_all_currency_codes(False)
    mid_index = len(currency_codes) // 2
    gpu_zero_currencies = currency_codes[:mid_index]
    gpu_one_currencies = currency_codes[mid_index:]
    return gpu_zero_currencies, gpu_one_currencies

#===================================-Main-====================================
def main():
    setup_logging()
    setup_tensorflow_env()

if __name__ == "__main__":
    main()