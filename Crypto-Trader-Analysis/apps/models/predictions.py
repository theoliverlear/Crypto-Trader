# predictions.py
import logging
from datetime import datetime

from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm_model import LstmModel
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.query_type import QueryType
from apps.models.train_model import get_data_frame, get_last_historical_price, \
    get_lstm_model, setup_logging, configure_concurrency, setup_tensorflow_env
from apps.models.training.training_type import TrainingType
import tensorflow as tf


def predict(target_currency: str = 'BTC', training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING) -> float:
    logging.info("Getting data frame...")
    dataframe = get_data_frame(target_currency, limit=20, query_type=QueryType.HISTORICAL_PRICE)
    logging.debug(f"Data frame generated for {target_currency}")
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    logging.info("Configuring preprocessor...")
    preprocessor = Preprocessor()

    # preprocessor.future_steps = Preprocessor.get_steps_from_now(datetime(2025, 3, 14, 10, 0, 0))
    # print(preprocessor.future_steps)Sho
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
    logging.debug(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    logging.info("Getting model...")
    model_path = f"models/{target_currency}_model.keras"
    model: LstmModel = get_lstm_model(target_currency, model_path, historical_prices, training_type)
    logging.info("Getting last sequence...")
    last_sequence = get_last_historical_price(historical_prices)
    logging.info("Predicting currency price...")
    predicted_price = model.predict(last_sequence, target_scaler)
    print(predicted_price)
    logging.debug(f"Predicted Next {target_currency} Price: {predicted_price}")
    return predicted_price

def get_current_price(target_currency: str = 'BTC') -> float:
    dataframe = get_data_frame(target_currency, 1, QueryType.CURRENT_PRICE)
    print(dataframe)
    current_price = dataframe.iloc[0]['currency_value']
    logging.debug(f"Current {target_currency} Price: {current_price}")
    return current_price

def actual_vs_predicted(target_currency: str = 'BTC') -> tuple:
    predicted_price = predict(target_currency)
    current_price = get_current_price(target_currency)
    difference = predicted_price - current_price
    percentage_difference = (difference / current_price) * 100
    logging.debug(f"""
        Predicted Price: {predicted_price}
        Current Price: {current_price}
        Difference: {difference}
        Percentage Difference: {percentage_difference}%
    """)
    return predicted_price, current_price, difference, percentage_difference

def log_actual_vs_printed(target_currency: str = 'BTC', predicted_price: float = 0.0):
    current_price = get_current_price(target_currency)
    difference = predicted_price - current_price
    percentage_difference = (difference / current_price) * 100
    logging.debug(f"""
        Predicted Price: {predicted_price}
        Current Price: {current_price}
        Difference: {difference}
        Percentage Difference: {percentage_difference}%
    """)

def main():
    setup_logging()
    configure_concurrency()
    setup_tensorflow_env()
    gpus = tf.config.experimental.list_physical_devices('GPU')
    if gpus:
        print(gpus)
    else:
        print("No GPU found.")
    # predict('BTC')
    # print(get_current_price("BTC"))
    # actual_vs_predicted("BTC")
    # actual_vs_predicted("DOGE")
    # actual_vs_predicted("ETH")
    actual_vs_predicted("AAVE")

if __name__ == "__main__":
    main()