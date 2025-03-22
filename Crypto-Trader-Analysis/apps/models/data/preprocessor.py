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
        import numpy as np

        if dataframe.empty:
            raise ValueError(
                "Error: The dataframe is empty. No data retrieved from database.")

        target_column = f"{target_currency}_price"

        numeric_cols = dataframe.select_dtypes(include=["number"]).columns
        raw_input_cols = [col for col in numeric_cols if col != target_column]

        dataframe.sort_values("last_updated", inplace=True)
        dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change(fill_method=None)

        dataframe.dropna(subset=raw_input_cols, inplace=True)

        target_data = dataframe[target_column].values
        raw_data = dataframe[raw_input_cols].values

        total_rows = len(dataframe)
        num_samples = total_rows - self.sequence_length - self.future_steps
        if num_samples <= 0:
            raise ValueError("Not enough data to form a single sample.")

        dimension = 3 + raw_data.shape[1]
        historical_prices = np.empty(
            (num_samples, self.sequence_length, dimension), dtype=np.float32)
        future_prices = np.empty(num_samples, dtype=np.float32)

        def mean_corr(subX, subY):
            x_mean = subX.mean(axis=0)
            y_mean = subY.mean()

            numerator = ((subX - x_mean) * (subY[:, None] - y_mean)).sum(
                axis=0)

            denom_x = ((subX - x_mean) ** 2).sum(axis=0)
            denom_y = ((subY - y_mean) ** 2).sum()
            denom = np.sqrt(denom_x * denom_y)

            valid = denom != 0
            corrs = np.zeros_like(denom, dtype=np.float32)
            corrs[valid] = numerator[valid] / denom[valid]

            return corrs.mean()

        sample_index = 0
        for i in range(self.sequence_length, total_rows - self.future_steps):
            # 1) Past trends (60%)
            past_trends = target_data[i - self.sequence_length:i]
            past_trends_weighted = past_trends * 0.60

            key_start = max(0, i - 2 * self.sequence_length)
            key_timestamps = target_data[key_start:i:5]

            if len(key_timestamps) < self.sequence_length:
                needed = self.sequence_length - len(key_timestamps)
                key_timestamps = np.hstack([key_timestamps, np.zeros(needed)])
            key_timestamps_weighted = key_timestamps[
                                      :self.sequence_length] * 0.20
            subX = raw_data[
                   i - self.sequence_length:i]
            subY = target_data[i - self.sequence_length:i]
            corr_value = mean_corr(subX, subY)
            corr_array_weighted = np.full(self.sequence_length,
                                          corr_value * 0.20, dtype=np.float32)

            raw_block = subX

            triple_weights = np.column_stack([
                past_trends_weighted,
                key_timestamps_weighted,
                corr_array_weighted
            ])
            combined_block = np.hstack([triple_weights, raw_block])

            historical_prices[sample_index] = combined_block
            future_prices[sample_index] = target_data[i + self.future_steps]
            sample_index += 1

        if len(historical_prices.shape) != 3:
            raise ValueError(
                f"Shape mismatch in historical_prices => {historical_prices.shape}")
        if len(future_prices.shape) != 1:
            raise ValueError(
                f"Shape mismatch in future_prices => {future_prices.shape}")
        num_samples, sequence_length, num_features = historical_prices.shape
        reshaped = historical_prices.reshape(num_samples * sequence_length,
                                             num_features)
        scaled_reshaped = self.input_scaler.fit_transform(reshaped)
        scaled_block = scaled_reshaped.reshape(num_samples, sequence_length,
                                               num_features)

        return scaled_block, future_prices, self.input_scaler

    def transform_multi_scale_with_weights(self,
                                           dataframe,
                                           target_currency: str,
                                           short_seq: int = 10,
                                           medium_seq: int = 50,
                                           long_seq: int = 150):
        if dataframe.empty:
            raise ValueError("Dataframe is empty. No data to process.")
        dataframe.sort_values("last_updated", inplace=True)
        target_col = f"{target_currency}_price"
        numeric_cols = dataframe.select_dtypes(include=["number"]).columns
        raw_input_cols = [col for col in numeric_cols if col != target_col]
        dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change(fill_method=None)
        dataframe.dropna(subset=raw_input_cols, inplace=True)

        target_data = dataframe[target_col].values
        raw_data = dataframe[raw_input_cols].values
        total_len = len(dataframe)
        future_steps = self.future_steps

        largest_window = max(short_seq, medium_seq, long_seq)
        if total_len < largest_window + future_steps:
            raise ValueError(
                "Not enough data to form short, medium, and long windows.")
        short_blocks = []
        medium_blocks = []
        long_blocks = []
        future_prices = []

        def mean_corr(subX, subY):
            x_mean = subX.mean(axis=0)
            y_mean = subY.mean()
            numerator = ((subX - x_mean) * (subY[:, None] - y_mean)).sum(
                axis=0)
            denom_x = ((subX - x_mean) ** 2).sum(axis=0)
            denom_y = ((subY - y_mean) ** 2).sum()
            denom = np.sqrt(denom_x * denom_y)
            valid = denom != 0
            corrs = np.zeros_like(denom, dtype=np.float32)
            corrs[valid] = numerator[valid] / denom[valid]
            return corrs.mean()
        last_index = total_len - future_steps
        for i in range(largest_window, last_index):
            short_start = i - short_seq
            short_past = target_data[short_start: i]
            short_past_w = short_past * 0.60

            short_key_start = max(0, i - 2 * short_seq)
            short_keys = target_data[short_key_start: i: 5]
            if len(short_keys) < short_seq:
                needed = short_seq - len(short_keys)
                short_keys = np.hstack([short_keys, np.zeros(needed)])
            short_keys = short_keys[-short_seq:]
            short_keys_w = short_keys * 0.20

            short_subX = raw_data[short_start: i]
            short_subY = target_data[short_start: i]
            short_corr_val = mean_corr(short_subX, short_subY)
            short_corr_array = np.full(short_seq, short_corr_val * 0.20,
                                       dtype=np.float32)

            short_block = np.column_stack(
                [short_past_w, short_keys_w, short_corr_array])
            short_block = np.hstack([short_block,
                                     short_subX])
            short_blocks.append(short_block)
            med_start = i - medium_seq
            med_past = target_data[med_start: i]
            med_past_w = med_past * 0.60

            med_key_start = max(0, i - 2 * medium_seq)
            med_keys = target_data[med_key_start: i: 5]
            if len(med_keys) < medium_seq:
                needed = medium_seq - len(med_keys)
                med_keys = np.hstack([med_keys, np.zeros(needed)])
            med_keys = med_keys[-medium_seq:]
            med_keys_w = med_keys * 0.20

            med_subX = raw_data[med_start: i]
            med_subY = target_data[med_start: i]
            med_corr_val = mean_corr(med_subX, med_subY)
            med_corr_array = np.full(medium_seq, med_corr_val * 0.20,
                                     dtype=np.float32)

            med_block = np.column_stack(
                [med_past_w, med_keys_w, med_corr_array])
            med_block = np.hstack([med_block, med_subX])
            medium_blocks.append(med_block)
            long_start = i - long_seq
            lng_past = target_data[long_start: i]
            lng_past_w = lng_past * 0.60
            lng_key_start = max(0, i - 2 * long_seq)
            lng_keys = target_data[lng_key_start: i: 5]
            if len(lng_keys) < long_seq:
                needed = long_seq - len(lng_keys)
                lng_keys = np.hstack([lng_keys, np.zeros(needed)])
            lng_keys = lng_keys[-long_seq:]
            lng_keys_w = lng_keys * 0.20
            lng_subX = raw_data[long_start: i]
            lng_subY = target_data[long_start: i]
            lng_corr_val = mean_corr(lng_subX, lng_subY)
            lng_corr_array = np.full(long_seq, lng_corr_val * 0.20,
                                     dtype=np.float32)
            lng_block = np.column_stack(
                [lng_past_w, lng_keys_w, lng_corr_array])
            lng_block = np.hstack([lng_block, lng_subX])
            long_blocks.append(lng_block)
            future_label = target_data[i + future_steps]
            future_prices.append(future_label)
        short_blocks = np.array(short_blocks,
                                dtype=np.float32)
        medium_blocks = np.array(medium_blocks,
                                 dtype=np.float32)
        long_blocks = np.array(long_blocks,
                               dtype=np.float32)
        future_prices = np.array(future_prices,
                                 dtype=np.float32)
        combined_flat = np.concatenate([
            short_blocks.flatten(),
            medium_blocks.flatten(),
            long_blocks.flatten()
        ]).reshape(-1, 1)
        combined_scaled = self.input_scaler.fit_transform(combined_flat)
        s_size = short_blocks.size
        m_size = medium_blocks.size
        short_scaled_flat = combined_scaled[:s_size]
        medium_scaled_flat = combined_scaled[s_size: s_size + m_size]
        long_scaled_flat = combined_scaled[s_size + m_size:]
        short_scaled = short_scaled_flat.reshape(short_blocks.shape)
        medium_scaled = medium_scaled_flat.reshape(medium_blocks.shape)
        long_scaled = long_scaled_flat.reshape(long_blocks.shape)
        return short_scaled, medium_scaled, long_scaled, future_prices, self.input_scaler

    @staticmethod
    def get_steps_from_now(future_time: datetime) -> int:
        future_time_seconds: float = future_time.timestamp() - datetime.now().timestamp()
        return int(future_time_seconds / 5)