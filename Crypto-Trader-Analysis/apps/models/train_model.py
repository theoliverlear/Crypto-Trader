# train_model.py
import logging
import os
import numpy as np
from pandas import DataFrame
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm_model import LstmModel
from apps.models.data.preprocessor import Preprocessor


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

def get_data_frame(target_currency: str = 'BTC', limit: int = 2000):
    from apps.models.database.database import Database
    db = Database()
    df: DataFrame = db.fetch_data(target_currency, limit)
    df.rename(columns={"target_price": f"{target_currency}_price"}, inplace=True)
    return df

def get_last_historical_price(historical_prices):
    return np.array([historical_prices[-1]])

def get_last_currency_price(dataframe: DataFrame, target_currency: str):
    return dataframe[f"{target_currency}_price"].iloc[-1]

def get_model(model_target_currency: str,
              currency_model_path: str,
              historical_prices) -> LstmModel:
    if os.path.exists(currency_model_path):
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        lstm_model = LstmModel(dimension=historical_prices.shape[2],
                               target_currency=model_target_currency)
        lstm_model.load_model(currency_model_path)
        return lstm_model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return LstmModel(dimension=historical_prices.shape[2],
                         target_currency=model_target_currency)

def train_model(target_currency: str = 'BTC'):
    configure_concurrency()
    dataframe = get_data_frame(target_currency, limit=20000)
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    preprocessor = Preprocessor()
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    dataframe.dropna(subset=[f"{target_currency}_price"], inplace=True)
    target_scaler = MinMaxScaler(feature_range=(0,1))
    raw_target_vals = dataframe[[f"{target_currency}_price"]].values
    target_scaler.fit(raw_target_vals)
    future_prices_scaled = target_scaler.transform(future_prices_unscaled.reshape(-1,1)).ravel()
    logging.debug(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    model_path = f"{target_currency}_model.keras"
    model = get_model(target_currency, model_path, historical_prices)
    model.train(historical_prices, future_prices_scaled, epochs=30, batch_size=32)
    model.save_model(model_path)
    last_sequence = get_last_historical_price(historical_prices)
    predicted_price = model.predict(last_sequence, target_scaler)
    logging.debug(f"Predicted Next {target_currency} Price: {predicted_price}")

def main():
    setup_logging()
    train_model("BTC")

if __name__ == "__main__":
    main()
