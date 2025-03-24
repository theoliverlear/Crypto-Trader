# predictions.py
import logging
import os
from datetime import datetime
from pathlib import Path
from typing import Optional

import requests
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm_model import LstmModel
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.query_type import QueryType
from apps.models.prediction.prediction import Prediction
from apps.models.train_model import get_data_frame, get_last_historical_price, \
    get_lstm_model, setup_logging, configure_concurrency, setup_tensorflow_env
from apps.models.training.training_type import TrainingType


from currency_json_generator import get_all_currency_codes


def model_exists(target_currency: str, base_dir: str = None) -> bool:
    # TODO: Make this more modular.
    if base_dir is None:
        current_file_path = Path(__file__).resolve()
        base_dir = current_file_path.parents[3] / "models" / "lstm_models"

    model_path = base_dir / f"{target_currency}_model.keras"
    exists = model_path.is_file()
    logging.debug(f"Checking model at: {model_path} -> Exists: {exists}")
    return exists

def predict(target_currency: str = 'BTC', training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING) -> Optional[float]:
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
    model_path = f"models/lstm_models/{target_currency}_model.keras"
    if not model_exists(target_currency):
        logging.info(f"Model not found for {target_currency}, stopping prediction.")
        return None
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

def actual_vs_predicted(target_currency: str = 'BTC',
                        training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING) -> Optional[Prediction]:
    predicted_price = predict(target_currency)
    if predicted_price is None:
        logging.error(f"No prediction model found for {target_currency}. Stopping prediction.")
        return None
    current_price = get_current_price(target_currency)
    difference = predicted_price - current_price
    percentage_difference = (difference / current_price) * 100
    logging.debug(f"""\n
        Predicted Price: {predicted_price}
        Current Price: {current_price}
        Difference: {difference}
        Percentage Difference: {percentage_difference}%
    """)
    prediction: Prediction = Prediction(currency_code=target_currency,
                                        predicted_price=predicted_price,
                                        actual_price=current_price,
                                        price_difference=difference,
                                        percent_difference=percentage_difference,
                                        model_type="lstm",
                                        num_rows=20,
                                        last_updated=datetime.now())
    # return predicted_price, current_price, difference, percentage_difference
    return prediction

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

def predict_and_send(target_currency: str = 'BTC',
                     training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING):
    prediction: Prediction = actual_vs_predicted(target_currency, training_type)
    if prediction is None:
        logging.error(f"No prediction model found for {target_currency}. Stopping prediction.")
        return
    send_prediction_to_server(prediction)

def send_prediction_to_server(prediction: Prediction):
    try:
        response = requests.post("http://localhost:8080/api/predictions", json=prediction.to_json(), verify=False)
        print(f"[{prediction.currency_code}] Status: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Failed to send prediction for {prediction.currency_code}: {e}")

def predict_and_send_loop(target_currency: str = 'BTC',
                          training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING):
    while True:
        predict_and_send(target_currency, training_type)

from concurrent.futures import ThreadPoolExecutor, as_completed


def predict_and_send_all_loop(training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING):
    currency_list: list[str] = get_all_currency_codes(True)
    with ThreadPoolExecutor(max_workers=12) as executor:
        while True:
            futures = {executor.submit(predict_and_send, currency,
                                       training_type): currency for currency
                       in currency_list}

            for future in as_completed(futures):
                currency = futures[future]
                try:
                    future.result()
                except Exception as e:
                    logging.error(f"Error processing {currency}: {e}")


def main():
    setup_logging()
    configure_concurrency()
    setup_tensorflow_env()
    # predict('BTC')
    # print(get_current_price("BTC"))
    # actual_vs_predicted("BTC")
    # actual_vs_predicted("DOGE")
    # actual_vs_predicted("ETH")
    # actual_vs_predicted("AAVE")
    # predict_and_send_loop()
    predict_and_send_all_loop()
    # actual_vs_predicted("MOG")
    # actual_vs_predicted("FLOKI")
    # actual_vs_predicted("MOBILE")

if __name__ == "__main__":
    main()