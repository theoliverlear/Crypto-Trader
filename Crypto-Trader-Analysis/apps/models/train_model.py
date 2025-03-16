# train_model.py
import logging
import os
import numpy as np
import pandas as pd
from pandas import DataFrame
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm_model import LstmModel
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.query_type import QueryType
from apps.models.training.training_type import TrainingType
from currency_json_generator import get_all_currency_codes


def setup_logging():
    logging.basicConfig(
        level=logging.DEBUG,
        format='[%(asctime)s] [%(levelname)s] %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    logging.debug("Logging setup complete: Debug mode is enabled.")

def configure_concurrency():
    num_cores = str(os.cpu_count())
    os.environ["OMP_NUM_THREADS"] = num_cores
    os.environ["TF_NUM_INTRAOP_THREADS"] = num_cores
    os.environ["TF_NUM_INTEROP_THREADS"] = num_cores
    os.environ["TF_XLA_FLAGS"] = "--tf_xla_auto_jit=2"
    os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"

def get_data_frame(target_currency: str = 'BTC', limit: int = 2000, query_type=QueryType.HISTORICAL_PRICE) -> DataFrame:
    from apps.models.database.database import Database
    logging.info("Creating database connection...")
    db: Database = Database()
    logging.info("Fetching data frame...")
    df: DataFrame = db.fetch_data(target_currency, limit, query_type)
    logging.info("Handling price columns...")
    df.rename(columns={"target_price": f"{target_currency}_price"}, inplace=True)
    return df

def get_last_historical_price(historical_prices):
    return np.array([historical_prices[-1]])

def get_last_currency_price(dataframe: DataFrame, target_currency: str):
    return dataframe[f"{target_currency}_price"].iloc[-1]

def get_model(model_target_currency: str,
              currency_model_path: str,
              historical_prices,
              training_model: TrainingType) -> LstmModel:
    if os.path.exists(currency_model_path):
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        lstm_model = LstmModel(dimension=historical_prices.shape[2],
                               target_currency=model_target_currency,
                               sequence_length=training_model.value.sequence_length)
        lstm_model.load_model(currency_model_path)
        return lstm_model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return LstmModel(dimension=historical_prices.shape[2],
                         target_currency=model_target_currency)

def train_model(target_currency: str = 'BTC', training_type=TrainingType.BALANCED_MEDIUM_TRAINING):
    configure_concurrency()
    logging.info("Getting data frame...")
    dataframe = get_data_frame(target_currency, limit=training_type.value.max_rows, query_type=QueryType.HISTORICAL_PRICE)
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    if len(dataframe) < 50 and training_type.value.skip_small_samples:
        logging.warning(f"Not enough data to train model for {target_currency}. Skipping...")
        return
    logging.info("Configuring preprocessor...")
    preprocessor = Preprocessor()
    logging.info("Transforming data...")
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    logging.info("Dropping NaN values...")
    dataframe.dropna(subset=[f"{target_currency}_price"], inplace=True)
    logging.info("Scaling target values...")
    target_scaler = MinMaxScaler(feature_range=(0,1))
    logging.info("Getting raw target values...")
    raw_target_vals = dataframe[[f"{target_currency}_price"]].values
    logging.info("Fitting target scaler...")
    target_scaler.fit(raw_target_vals)
    logging.info("Scaling future prices...")
    future_prices_scaled = target_scaler.transform(future_prices_unscaled.reshape(-1,1)).ravel()
    logging.info(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    model_path = f"models/{target_currency}_model.keras"
    logging.info(f"Getting model for {target_currency} at {model_path}...")
    model = get_model(target_currency, model_path, historical_prices, training_type)
    logging.info(f"Training model for {target_currency}...")
    model.train(historical_prices, future_prices_scaled, epochs=training_type.value.epochs, batch_size=training_type.value.epochs)
    logging.info(f"Training completed for {target_currency}.")
    logging.info(f"Saving model for {target_currency}...")
    model.save_model(model_path)
    logging.info(f"Getting last sequence.")
    last_sequence = get_last_historical_price(historical_prices)
    logging.info(f"Predicting currency price.")
    predicted_price = model.predict(last_sequence, target_scaler)
    logging.debug(f"Predicted Next {target_currency} Price: {predicted_price}")

def train_all_models(training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING):
    training_type: TrainingType = TrainingType.LARGE_DATA_DETAILED_TRAINING
    currency_codes: list[str] = get_all_currency_codes(True)
    for currency_code in currency_codes:
        logging.debug(f"Training model for {currency_code}...")
        train_model(currency_code, training_type)
        logging.debug(f"Model for {currency_code} trained successfully.")


def main():
    setup_logging()
    continuous: bool = True
    while continuous:
        train_all_models()


if __name__ == "__main__":
    main()
