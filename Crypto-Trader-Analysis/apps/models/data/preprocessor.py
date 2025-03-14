# preprocessor.py

import numpy as np
from attr import attr
from attrs import define
from sklearn.preprocessing import MinMaxScaler
from datetime import datetime

@define
class Preprocessor:
    sequence_length: int = attr(default=10)
    future_steps: int = attr(default=1)
    input_scaler: MinMaxScaler = attr(default=MinMaxScaler(feature_range=(0, 1)))

    def transform(self, dataframe, target_currency):
        """
           - Past trends (60%)
           - Key pattern times (20%)
           - Relationship/correlation (20%)
        """
        if dataframe.empty:
            raise ValueError("Error: The dataframe is empty. No data retrieved from database.")
        target_column = f"{target_currency}_price"
        numeric_cols = dataframe.select_dtypes(include=["number"]).columns
        raw_input_cols = [col for col in numeric_cols if col != target_column]
        dataframe.sort_values("last_updated", inplace=True)
        dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change()
        dataframe.dropna(subset=raw_input_cols, inplace=True)
        historical_prices, future_prices = [], []
        for i in range(self.sequence_length, len(dataframe) - self.future_steps):
            past_trends = dataframe[target_column].iloc[i - self.sequence_length:i].values
            past_trends_weighted = past_trends[:self.sequence_length] * 0.60
            key_range_start = max(0, i - self.sequence_length * 2)
            key_timestamps = dataframe[target_column].iloc[key_range_start:i].values[::5]
            if len(key_timestamps) < self.sequence_length:
                
                padding = np.zeros(self.sequence_length - len(key_timestamps))
                key_timestamps = np.hstack([key_timestamps, padding])
            key_timestamps_weighted = key_timestamps[:self.sequence_length] * 0.20
            sub_df = dataframe[raw_input_cols].iloc[i - self.sequence_length:i]
            target_slice = dataframe[target_column].iloc[i - self.sequence_length:i]
            corr_series = sub_df.corrwith(target_slice).fillna(0).values
            corr_value = np.mean(corr_series)
            corr_array = np.ones(self.sequence_length) * corr_value
            corr_array_weighted = corr_array * 0.20
            raw_block = dataframe[raw_input_cols].iloc[i - self.sequence_length:i].values

            triple_weights = np.column_stack([
                past_trends_weighted,
                key_timestamps_weighted,
                corr_array_weighted
            ])  

            combined_block = np.hstack([triple_weights, raw_block])
            historical_prices.append(combined_block)
            future_prices.append(dataframe[target_column].iloc[i + self.future_steps])
        historical_prices = np.array(historical_prices, dtype=np.float32)  
        future_prices = np.array(future_prices, dtype=np.float32)
        if len(historical_prices.shape) != 3:
            raise ValueError(f"Shape mismatch in historical_prices => {historical_prices.shape}")
        if len(future_prices.shape) != 1:
            raise ValueError(f"Shape mismatch in future_prices => {future_prices.shape}")
        num_samples, sequence_length, num_features = historical_prices.shape
        reshaped = historical_prices.reshape(num_samples * sequence_length, num_features)
        scaled_reshaped = self.input_scaler.fit_transform(reshaped)
        scaled_block = scaled_reshaped.reshape(num_samples, sequence_length, num_features)
        return scaled_block, future_prices, self.input_scaler

    @staticmethod
    def get_steps_from_now(future_time: datetime) -> int:
        future_time_seconds: float = future_time.timestamp() - datetime.now().timestamp()
        return int(future_time_seconds / 5)