# predictions.py
import logging
from datetime import datetime

from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.lstm_model import LstmModel
from apps.models.data.preprocessor import Preprocessor
from apps.models.train_model import get_data_frame, get_last_historical_price, \
    get_model
def predict(target_currency: str = 'BTC'):
    dataframe = get_data_frame(target_currency, 2000)
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    preprocessor = Preprocessor()
    preprocessor.future_steps = Preprocessor.get_steps_from_now(datetime(2025, 3, 14, 10, 0, 0))
    print(preprocessor.future_steps)
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    dataframe.dropna(subset=[f"{target_currency}_price"], inplace=True)
    target_scaler = MinMaxScaler(feature_range=(0,1))
    raw_target_vals = dataframe[[f"{target_currency}_price"]].values
    target_scaler.fit(raw_target_vals)
    logging.debug(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    model_path = f"{target_currency}_model.keras"
    model: LstmModel = get_model(target_currency, model_path, historical_prices)
    last_sequence = get_last_historical_price(historical_prices)
    predicted_price = model.predict(last_sequence, target_scaler)
    print(predicted_price)

def main():
    predict('BTC')

if __name__ == "__main__":
    main()
