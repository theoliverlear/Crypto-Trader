# preprocessor.py

import numpy as np
from attr import attr
from attrs import define
from numpy.lib.stride_tricks import sliding_window_view
from sklearn.preprocessing import MinMaxScaler
from datetime import datetime

@define
class Preprocessor:
    # TODO: Replace with Enum for sequence lengths with goals.
    #       - 5-10 Steps: Ultra short-term
    #       - 10-30 Steps: Short-term
    #       - 30-60 Steps: Hourly-to-daily
    #       - 60-120 Steps: Multi-daily-to-weekly
    #       - 120-300 Steps: Extended historical
    sequence_length: int = attr(default=10)
    future_steps: int = attr(default=1)
    input_scaler: MinMaxScaler = attr(default=MinMaxScaler(feature_range=(0, 1)))

    # def transform(self, dataframe, target_currency):
    #     """
    #        - Past trends (60%)
    #        - Key pattern times (20%)
    #        - Relationship/correlation (20%)
    #     """
    #     if dataframe.empty:
    #         raise ValueError("Error: The dataframe is empty. No data retrieved from database.")
    #     target_column = f"{target_currency}_price"
    #     numeric_cols = dataframe.select_dtypes(include=["number"]).columns
    #     raw_input_cols = [col for col in numeric_cols if col != target_column]
    #     dataframe.sort_values("last_updated", inplace=True)
    #     dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change()
    #     dataframe.dropna(subset=raw_input_cols, inplace=True)
    #     historical_prices, future_prices = [], []
    #     for i in range(self.sequence_length, len(dataframe) - self.future_steps):
    #         past_trends = dataframe[target_column].iloc[i - self.sequence_length:i].values
    #         past_trends_weighted = past_trends[:self.sequence_length] * 0.60
    #         key_range_start = max(0, i - self.sequence_length * 2)
    #         key_timestamps = dataframe[target_column].iloc[key_range_start:i].values[::5]
    #         if len(key_timestamps) < self.sequence_length:
    #             padding = np.zeros(self.sequence_length - len(key_timestamps))
    #             key_timestamps = np.hstack([key_timestamps, padding])
    #         key_timestamps_weighted = key_timestamps[:self.sequence_length] * 0.20
    #         sub_df = dataframe[raw_input_cols].iloc[i - self.sequence_length:i]
    #         target_slice = dataframe[target_column].iloc[i - self.sequence_length:i]
    #         corr_series = sub_df.corrwith(target_slice).fillna(0).values
    #         corr_value = np.mean(corr_series)
    #         corr_array = np.ones(self.sequence_length) * corr_value
    #         corr_array_weighted = corr_array * 0.20
    #         raw_block = dataframe[raw_input_cols].iloc[i - self.sequence_length:i].values
    #         triple_weights = np.column_stack([
    #             past_trends_weighted,
    #             key_timestamps_weighted,
    #             corr_array_weighted
    #         ])
    #         combined_block = np.hstack([triple_weights, raw_block])
    #         historical_prices.append(combined_block)
    #         future_prices.append(dataframe[target_column].iloc[i + self.future_steps])
    #     historical_prices = np.array(historical_prices, dtype=np.float32)
    #     future_prices = np.array(future_prices, dtype=np.float32)
    #     if len(historical_prices.shape) != 3:
    #         raise ValueError(f"Shape mismatch in historical_prices => {historical_prices.shape}")
    #     if len(future_prices.shape) != 1:
    #         raise ValueError(f"Shape mismatch in future_prices => {future_prices.shape}")
    #     num_samples, sequence_length, num_features = historical_prices.shape
    #     reshaped = historical_prices.reshape(num_samples * sequence_length, num_features)
    #     scaled_reshaped = self.input_scaler.fit_transform(reshaped)
    #     scaled_block = scaled_reshaped.reshape(num_samples, sequence_length, num_features)
    #     return scaled_block, future_prices, self.input_scaler

    def transform(self, dataframe, target_currency):
        """
        - Past trends (60%)
        - Key pattern times (20%)
        - Relationship/correlation (20%)
        """
        import numpy as np

        if dataframe.empty:
            raise ValueError(
                "Error: The dataframe is empty. No data retrieved from database.")

        target_column = f"{target_currency}_price"

        # Identify numeric columns except the target column
        numeric_cols = dataframe.select_dtypes(include=["number"]).columns
        raw_input_cols = [col for col in numeric_cols if col != target_column]

        # Sort by time and compute % change
        dataframe.sort_values("last_updated", inplace=True)
        dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change()

        # Drop rows with NaN in the raw_input_cols
        dataframe.dropna(subset=raw_input_cols, inplace=True)

        # Convert price and raw features to NumPy arrays
        target_data = dataframe[target_column].values
        raw_data = dataframe[raw_input_cols].values

        # Number of training samples
        total_rows = len(dataframe)
        num_samples = total_rows - self.sequence_length - self.future_steps
        if num_samples <= 0:
            raise ValueError("Not enough data to form a single sample.")

        # Pre-allocate final arrays
        # Dimension = 3 weighted columns (past_trends_weighted, key_timestamps_weighted,
        # correlation_array_weighted) + number of raw input features
        dimension = 3 + raw_data.shape[1]
        historical_prices = np.empty(
            (num_samples, self.sequence_length, dimension), dtype=np.float32)
        future_prices = np.empty(num_samples, dtype=np.float32)

        # A small helper to compute average correlation for each time step
        def mean_corr(subX, subY):
            """
            subX: shape (sequence_length, n_features)
            subY: shape (sequence_length,)
            Returns the average correlation across n_features.
            """
            x_mean = subX.mean(axis=0)
            y_mean = subY.mean()
            # Numerator: sum( (X - mu_X) * (Y - mu_Y) ) across the seq_length
            numerator = ((subX - x_mean) * (subY[:, None] - y_mean)).sum(
                axis=0)
            # Denominator: sqrt( sum((X-mu_X)^2) * sum((Y - mu_Y)^2) )
            denom_x = ((subX - x_mean) ** 2).sum(axis=0)
            denom_y = ((subY - y_mean) ** 2).sum()
            denom = np.sqrt(denom_x * denom_y)
            # Avoid division by zero => set correlation to 0 if denom == 0
            valid = denom != 0
            corrs = np.zeros_like(denom, dtype=np.float32)
            corrs[valid] = numerator[valid] / denom[valid]
            # Return the average correlation across all features
            return corrs.mean()

        # Single pass to build samples
        sample_index = 0
        for i in range(self.sequence_length, total_rows - self.future_steps):
            # 1) Past trends (60%)
            past_trends = target_data[i - self.sequence_length:i]
            past_trends_weighted = past_trends * 0.60

            # 2) Key timestamps (every 5 steps from up to 2x seq_length) (20%)
            key_start = max(0, i - 2 * self.sequence_length)
            key_timestamps = target_data[key_start:i:5]
            # Pad if not enough
            if len(key_timestamps) < self.sequence_length:
                needed = self.sequence_length - len(key_timestamps)
                key_timestamps = np.hstack([key_timestamps, np.zeros(needed)])
            key_timestamps_weighted = key_timestamps[
                                      :self.sequence_length] * 0.20

            # 3) Average correlation (20%)
            subX = raw_data[
                   i - self.sequence_length:i]  # shape (seq_len, n_features)
            subY = target_data[i - self.sequence_length:i]  # shape (seq_len,)
            corr_value = mean_corr(subX, subY)
            corr_array_weighted = np.full(self.sequence_length,
                                          corr_value * 0.20, dtype=np.float32)

            # 4) Raw block
            raw_block = subX  # shape (seq_len, n_features)

            # Stack them horizontally => final shape (seq_len, 3 + n_features)
            triple_weights = np.column_stack([
                past_trends_weighted,
                key_timestamps_weighted,
                corr_array_weighted
            ])
            combined_block = np.hstack([triple_weights, raw_block])

            historical_prices[sample_index] = combined_block
            future_prices[sample_index] = target_data[i + self.future_steps]
            sample_index += 1

        # Double check final shape
        if len(historical_prices.shape) != 3:
            raise ValueError(
                f"Shape mismatch in historical_prices => {historical_prices.shape}")
        if len(future_prices.shape) != 1:
            raise ValueError(
                f"Shape mismatch in future_prices => {future_prices.shape}")

        # Scale the historical prices
        num_samples, sequence_length, num_features = historical_prices.shape
        reshaped = historical_prices.reshape(num_samples * sequence_length,
                                             num_features)
        scaled_reshaped = self.input_scaler.fit_transform(reshaped)
        scaled_block = scaled_reshaped.reshape(num_samples, sequence_length,
                                               num_features)

        return scaled_block, future_prices, self.input_scaler

    @staticmethod
    def get_steps_from_now(future_time: datetime) -> int:
        future_time_seconds: float = future_time.timestamp() - datetime.now().timestamp()
        return int(future_time_seconds / 5)