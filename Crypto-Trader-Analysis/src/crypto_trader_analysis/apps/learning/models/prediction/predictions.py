# predictions.py
import logging
import time
from datetime import datetime
from typing import Optional

import requests

from src.crypto_trader_analysis.apps.learning.models.ai.model_type import ModelType
from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType
from src.crypto_trader_analysis.apps.learning.models.prediction.prediction import Prediction
from src.crypto_trader_analysis.apps.learning.models.training.train_model import get_dataframe, \
    setup_logging, configure_concurrency, setup_tensorflow_env
from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel
from src.crypto_trader_analysis.apps.learning.models.training.training_type import TrainingType


from src.crypto_trader_analysis.apps.learning.models.currency_json_generator import get_all_currency_codes



def predict(target_currency: str = 'BTC',
            training_type: TrainingModel = TrainingType.DETAILED_SHORT_TRAINING.value,
            model_type: ModelType = ModelType.LSTM) -> Optional[float]:
    from src.crypto_trader_analysis.apps.learning.models.training.training_session import TrainingSession
    training_session: TrainingSession = TrainingSession(target_currency=target_currency,
                                                        training_model=training_type,
                                                        model_type=model_type)
    predicted_price: float = training_session.predict_without_dataframe()
    prediction: Prediction = actual_vs_predicted(target_currency,
                                                training_model=training_type,
                                                model_type=model_type,
                                                predicted_price=predicted_price,
                                                with_logging=True)
    if prediction is None:
        logging.error(f"No prediction model found for {target_currency}. Stopping prediction.")
        return None
    return predicted_price


def get_current_price(target_currency: str = 'BTC') -> Optional[float]:
    dataframe = get_dataframe(target_currency, 1, QueryType.CURRENT_PRICE)
    if dataframe.empty:
        logging.error(f"No data found for {target_currency}.")
        return None
    current_price = dataframe.iloc[0]['currency_value']
    logging.debug(f"Current {target_currency} Price: ${current_price:,}")
    return current_price

def actual_vs_predicted(target_currency: str = 'BTC',
                        training_model: TrainingModel = TrainingType.DETAILED_SHORT_TRAINING.value,
                        model_type: ModelType = ModelType.LSTM,
                        predicted_price: float | None = None,
                        with_logging = False) -> Optional[Prediction]:
    if predicted_price is None:
        predicted_price = predict(target_currency, training_model, model_type)
        if predicted_price is None:
            logging.error(f"No prediction model found for {target_currency}. Stopping prediction.")
            return None
    current_price = get_current_price(target_currency)
    if current_price is None:
        logging.error(f"No current price found for {target_currency}. Stopping prediction.")
        return None
    difference = predicted_price - current_price
    difference_str: str = f"${difference:,}"
    percentage_difference = (difference / current_price) * 100
    if difference < 0:
        difference_str = f"-${abs(difference):,}"
    if with_logging:
        logging.debug(f"""\n
            Predicted Price: ${predicted_price:,}
            Current Price: ${current_price:,}
            Difference: {difference_str}
            Percentage Difference: {percentage_difference}%
        """)
    if model_type == ModelType.LSTM:
        model_name: str = "lstm"
    elif model_type == ModelType.COMPLEX_LSTM:
        model_name: str = "complex_lstm"
    elif model_type == ModelType.MULTI_LAYER:
        model_name: str = "multi_layer"
    elif model_type == ModelType.COMPLEX_MULTI_LAYER:
        model_name: str = "complex_multi_layer"
    else:
        raise ValueError(f"Unknown model type: {model_type}")
    prediction: Prediction = Prediction(currency_code=target_currency,
                                        predicted_price=predicted_price,
                                        actual_price=current_price,
                                        price_difference=difference,
                                        percent_difference=percentage_difference,
                                        model_type=model_name,
                                        num_rows=training_model.max_rows,
                                        last_updated=datetime.now())

    return prediction

def predict_and_send(target_currency: str = 'BTC',
                     training_type: TrainingModel = TrainingType.DETAILED_SHORT_TRAINING.value,
                     model_type: ModelType = ModelType.LSTM) -> Optional[Prediction]:
    prediction: Prediction = actual_vs_predicted(target_currency, training_type, model_type)
    if prediction is None:
        logging.error(f"No prediction model found for {target_currency}. Stopping prediction.")
        return None
    prediction_id: int = send_prediction_to_server(prediction)
    if prediction_id is not None:
        logging.info(f"Prediction for {target_currency} sent successfully with ID: {prediction_id}")
        prediction.prediction_id = prediction_id
    return prediction

def send_prediction_to_server(prediction: Prediction) -> Optional[int]:
    try:
        response = requests.post("http://localhost:8085/data/predictions/add", json=prediction.to_json(), verify=False)
        print(f"[{prediction.currency_code}] Status: {response.status_code} - {response.text}")
        payload: dict = response.json()
        return int(payload.get("predictionId"))
    except Exception as e:
        print(f"Failed to send prediction for {prediction.currency_code}: {e}")

def predict_and_send_loop(target_currency: str = 'BTC',
                          training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING):
    while True:
        predict_and_send(target_currency, training_type.value)

from concurrent.futures import ThreadPoolExecutor, as_completed


def predict_and_send_all_loop(
        training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING) -> None:
    currency_list: list[str] = get_all_currency_codes(True)
    with ThreadPoolExecutor(max_workers=36) as executor:
        while True:
            futures = {executor.submit(predict_and_send, currency,
                                       training_type.value): currency for currency
                       in currency_list}

            for future in as_completed(futures):
                currency = futures[future]
                try:
                    future.result()
                except Exception as e:
                    logging.error(f"Error processing {currency}: {e}")
                time.sleep(0.5)

def main():
    setup_logging()
    configure_concurrency()
    setup_tensorflow_env()
    # predict('BTC')
    # print(get_current_price("BTC"))
    predict("BTC")
    # actual_vs_predicted("DOGE")
    # actual_vs_predicted("ETH")
    # actual_vs_predicted("AAVE")
    # predict_and_send_loop()
    # actual_vs_predicted("AAVE", model_type=ModelType.COMPLEX_LSTM)
    # predict_and_send_all_loop()
    # actual_vs_predicted("MOG")
    # actual_vs_predicted("FLOKI")
    # actual_vs_predicted("MOBILE")

if __name__ == "__main__":
    main()

# TODO: Make a @predict_saving decorator that applies predictions to the call
#       of the function.