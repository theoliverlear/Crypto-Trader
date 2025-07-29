# predictions.py
import logging
import time
from datetime import datetime
from pathlib import Path
from typing import Optional

import requests
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm.base_model import BaseModel
from apps.models.ai.model_type import ModelType
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.query_type import QueryType
from apps.models.ai.model_retriever import get_model, model_exists
from apps.models.prediction.prediction import Prediction
from apps.models.training.train_model import get_dataframe, get_last_historical_price, \
    setup_logging, configure_concurrency, setup_tensorflow_env
from apps.models.training.training_type import TrainingType


from currency_json_generator import get_all_currency_codes



# TODO: Add multi-layer model support.
def predict(target_currency: str = 'BTC',
            training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING,
            model_type: ModelType = ModelType.LSTM) -> Optional[float]:
    logging.info("Getting data frame...")
    dataframe = get_dataframe(target_currency, limit=20, query_type=QueryType.HISTORICAL_PRICE)
    logging.debug(f"Data frame generated for {target_currency}")
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    logging.info("Configuring preprocessor...")
    preprocessor = Preprocessor()
    logging.info("Transforming data...")
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    logging.info("Dropping NaN values...")
    dataframe.dropna(subset=[f"{target_currency.lower()}_price"], inplace=True)
    logging.info("Scaling target values...")
    target_scaler = MinMaxScaler(feature_range=(0,1))
    logging.info("Getting raw target values...")
    raw_target_vals = dataframe[[f"{target_currency.lower()}_price"]].values
    logging.info("Fitting target scaler...")
    target_scaler.fit(raw_target_vals)
    logging.debug(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    logging.info("Getting model...")
    if not model_exists(target_currency, model_type):
        logging.info(f"Model not found for {target_currency}, stopping prediction.")
        return None
    model_path: str = model_type.value.get_model_path(target_currency)
    model: BaseModel = get_model(model_type.value,
                                 target_currency,
                                 model_path,
                                 historical_prices,
                                 training_type)

    logging.info("Getting last sequence...")
    last_sequence = get_last_historical_price(historical_prices)
    logging.info("Predicting currency price...")
    predicted_price = model.predict(last_sequence, target_scaler)
    logging.debug(f"Predicted Next {target_currency} Price: {predicted_price}")
    return predicted_price

def get_current_price(target_currency: str = 'BTC') -> Optional[float]:
    dataframe = get_dataframe(target_currency, 1, QueryType.CURRENT_PRICE)
    if dataframe.empty:
        logging.error(f"No data found for {target_currency}.")
        return None
    print(dataframe)
    current_price = dataframe.iloc[0]['currency_value']
    logging.debug(f"Current {target_currency} Price: {current_price}")
    return current_price

def actual_vs_predicted(target_currency: str = 'BTC',
                        training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING,
                        model_type: ModelType = ModelType.LSTM) -> Optional[Prediction]:
    predicted_price = predict(target_currency, training_type, model_type)
    if predicted_price is None:
        logging.error(f"No prediction model found for {target_currency}. Stopping prediction.")
        return None
    current_price = get_current_price(target_currency)
    if current_price is None:
        logging.error(f"No current price found for {target_currency}. Stopping prediction.")
        return None
    difference = predicted_price - current_price
    percentage_difference = (difference / current_price) * 100
    logging.debug(f"""\n
        Predicted Price: {predicted_price}
        Current Price: {current_price}
        Difference: {difference}
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
                                        num_rows=20,
                                        last_updated=datetime.now())

    return prediction

def log_actual_vs_printed(target_currency: str = 'BTC',
                          predicted_price: float = 0.0,
                          model_type: ModelType = ModelType.LSTM) -> None:
    current_price: Optional[float] = get_current_price(target_currency)
    if current_price is None:
        logging.error(f"No current price found for {target_currency}. Stopping prediction.")
        return None
    difference: float = predicted_price - current_price
    percentage_difference: float = (difference / current_price) * 100
    logging.debug(f"""
        Predicted Price: {predicted_price}
        Current Price: {current_price}
        Difference: {difference}
        Percentage Difference: {percentage_difference}%
        Model Type: {str(model_type)}
    """)

def predict_and_send(target_currency: str = 'BTC',
                     training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING,
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
        response = requests.post("http://localhost:8080/api/predictions/add", json=prediction.to_json(), verify=False)
        print(f"[{prediction.currency_code}] Status: {response.status_code} - {response.text}")
        payload: dict = response.json()
        return int(payload.get("predictionId"))
    except Exception as e:
        print(f"Failed to send prediction for {prediction.currency_code}: {e}")

def predict_and_send_loop(target_currency: str = 'BTC',
                          training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING):
    while True:
        predict_and_send(target_currency, training_type)

from concurrent.futures import ThreadPoolExecutor, as_completed


def predict_and_send_all_loop(
        training_type: TrainingType = TrainingType.DETAILED_SHORT_TRAINING) -> None:
    currency_list: list[str] = get_all_currency_codes(True)
    with ThreadPoolExecutor(max_workers=36) as executor:
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
                time.sleep(0.5)

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
    # actual_vs_predicted("AAVE", model_type=ModelType.COMPLEX_LSTM)
    # predict_and_send_all_loop()
    # actual_vs_predicted("MOG")
    # actual_vs_predicted("FLOKI")
    # actual_vs_predicted("MOBILE")

if __name__ == "__main__":
    main()

# TODO: Make a @predict_saving decorator that applies predictions to the call
#       of the function.