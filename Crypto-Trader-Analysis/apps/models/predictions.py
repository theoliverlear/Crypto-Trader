# predictions.py
import logging
from datetime import datetime

from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm_model import LstmModel
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.query_type import QueryType
from apps.models.train_model import get_data_frame, get_last_historical_price, \
    get_model, setup_logging
from apps.models.training.training_type import TrainingType


def predict(target_currency: str = 'BTC', training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING) -> float:
    dataframe = get_data_frame(target_currency, limit=training_type.value.max_rows, query_type=QueryType.HISTORICAL_PRICE)
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    preprocessor = Preprocessor()
    # preprocessor.future_steps = Preprocessor.get_steps_from_now(datetime(2025, 3, 14, 10, 0, 0))
    # print(preprocessor.future_steps)
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    dataframe.dropna(subset=[f"{target_currency}_price"], inplace=True)
    target_scaler = MinMaxScaler(feature_range=(0,1))
    raw_target_vals = dataframe[[f"{target_currency}_price"]].values
    target_scaler.fit(raw_target_vals)
    logging.debug(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    model_path = f"models/{target_currency}_model.keras"
    model: LstmModel = get_model(target_currency, model_path, historical_prices, TrainingType.LARGE_DATA_DETAILED_TRAINING)
    last_sequence = get_last_historical_price(historical_prices)
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

def main():
    setup_logging()
    # predict('BTC')
    # print(get_current_price("BTC"))
    actual_vs_predicted("BTC")
    actual_vs_predicted("ETH")
    actual_vs_predicted("SOL")
    actual_vs_predicted("XRP")

if __name__ == "__main__":
    main()
