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

    # def transform(self, dataframe, target_currency):
    #     import numpy as np
    #
    #     if dataframe.empty:
    #         raise ValueError(
    #             "Error: The dataframe is empty. No data retrieved from database.")
    #
    #     target_column = f"{target_currency.lower()}_price"
    #
    #     numeric_cols = dataframe.select_dtypes(include=["number"]).columns
    #     raw_input_cols = [col for col in numeric_cols if col != target_column]
    #
    #     dataframe.sort_values("last_updated", inplace=True)
    #     dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change(fill_method=None)
    #
    #     dataframe.dropna(subset=raw_input_cols, inplace=True)
    #
    #     target_data = dataframe[target_column].values
    #     raw_data = dataframe[raw_input_cols].values
    #
    #     total_rows = len(dataframe)
    #     num_samples = total_rows - self.sequence_length - self.future_steps
    #     if num_samples <= 0:
    #         raise ValueError("Not enough data to form a single sample.")
    #
    #     dimension = 3 + raw_data.shape[1]
    #     historical_prices = np.empty(
    #         (num_samples, self.sequence_length, dimension), dtype=np.float32)
    #     future_prices = np.empty(num_samples, dtype=np.float32)
    #
    #     def mean_corr(subX, subY):
    #         x_mean = subX.mean(axis=0)
    #         y_mean = subY.mean()
    #
    #         numerator = ((subX - x_mean) * (subY[:, None] - y_mean)).sum(
    #             axis=0)
    #
    #         denom_x = ((subX - x_mean) ** 2).sum(axis=0)
    #         denom_y = ((subY - y_mean) ** 2).sum()
    #         denom = np.sqrt(denom_x * denom_y)
    #
    #         valid = denom != 0
    #         corrs = np.zeros_like(denom, dtype=np.float32)
    #         corrs[valid] = numerator[valid] / denom[valid]
    #
    #         return corrs.mean()
    #
    #     sample_index = 0
    #     for i in range(self.sequence_length, total_rows - self.future_steps):
    #         # 1) Past trends (60%)
    #         past_trends = target_data[i - self.sequence_length:i]
    #         past_trends_weighted = past_trends * 0.60
    #
    #         key_start = max(0, i - 2 * self.sequence_length)
    #         key_timestamps = target_data[key_start:i:5]
    #
    #         if len(key_timestamps) < self.sequence_length:
    #             needed = self.sequence_length - len(key_timestamps)
    #             key_timestamps = np.hstack([key_timestamps, np.zeros(needed)])
    #         key_timestamps_weighted = key_timestamps[
    #                                   :self.sequence_length] * 0.20
    #         subX = raw_data[
    #                i - self.sequence_length:i]
    #         subY = target_data[i - self.sequence_length:i]
    #         corr_value = mean_corr(subX, subY)
    #         corr_array_weighted = np.full(self.sequence_length,
    #                                       corr_value * 0.20, dtype=np.float32)
    #
    #         raw_block = subX
    #
    #         triple_weights = np.column_stack([
    #             past_trends_weighted,
    #             key_timestamps_weighted,
    #             corr_array_weighted
    #         ])
    #         combined_block = np.hstack([triple_weights, raw_block])
    #
    #         historical_prices[sample_index] = combined_block
    #         future_prices[sample_index] = target_data[i + self.future_steps]
    #         sample_index += 1
    #
    #     if len(historical_prices.shape) != 3:
    #         raise ValueError(
    #             f"Shape mismatch in historical_prices => {historical_prices.shape}")
    #     if len(future_prices.shape) != 1:
    #         raise ValueError(
    #             f"Shape mismatch in future_prices => {future_prices.shape}")
    #     num_samples, sequence_length, num_features = historical_prices.shape
    #     reshaped = historical_prices.reshape(num_samples * sequence_length,
    #                                          num_features)
    #     scaled_reshaped = self.input_scaler.fit_transform(reshaped)
    #     scaled_block = scaled_reshaped.reshape(num_samples, sequence_length,
    #                                            num_features)
    #
    #     return scaled_block, future_prices, self.input_scaler

    def transform(self, dataframe, target_currency):
        import numpy as np
        import pandas as pd
        from numpy.lib.stride_tricks import sliding_window_view

        if dataframe.empty:
            raise ValueError(
                "Error: The dataframe is empty. No data retrieved from database.")

        target_column = f"{target_currency.lower()}_price"

        # ---------- robust sort by datetime ----------
        df = dataframe.copy()
        if "last_updated" not in df.columns:
            raise ValueError("Missing 'last_updated' column.")
        if not np.issubdtype(df["last_updated"].dtype, np.datetime64):
            df["last_updated"] = pd.to_datetime(df["last_updated"],
                                                errors="coerce")
        df = df.sort_values("last_updated").reset_index(drop=True)

        # ---------- select numeric columns ----------
        numeric_cols = df.select_dtypes(include=["number"]).columns
        if target_column not in numeric_cols:
            raise ValueError(
                f"Missing target column '{target_column}' in numeric columns.")
        raw_input_cols = [c for c in numeric_cols if c != target_column]

        # ---------- raw features as returns; keep target as price ----------
        df[raw_input_cols] = df[raw_input_cols].pct_change(fill_method=None)
        df = df.dropna(subset=raw_input_cols).reset_index(drop=True)

        target_price = df[target_column].to_numpy(
            dtype=np.float32)  # prices for engineered channels + label
        raw_data = df[raw_input_cols].to_numpy(
            dtype=np.float32)  # returns features

        # Target returns for correlation channel (align with already-trimmed df)
        tgt_ret = df[target_column].pct_change(fill_method=None).to_numpy(
            dtype=np.float32)
        if np.isnan(tgt_ret[
                        0]):  # first diff is NaN; set to 0 (or drop a row earlier)
            tgt_ret[0] = 0.0

        T = len(df)
        seq = self.sequence_length
        fwd = self.future_steps
        N = T - seq - fwd
        if N <= 0:
            raise ValueError("Not enough data to form a single sample.")

        F = raw_data.shape[1]

        # ---------- scale ONLY the raw returns (preserve engineered weights) ----------
        if hasattr(self.input_scaler, "scale_"):
            raw_scaled_all = self.input_scaler.transform(raw_data)  # (T, F)
        else:
            raw_scaled_all = self.input_scaler.fit_transform(raw_data)

        # ---------- build windows on the time axis (axis=0) ----------
        prefix_len = T - fwd  # windows must end before the future label point

        # Windows for raw returns (2D -> (N+1, F, seq) -> (N, seq, F))
        raw_win_all = sliding_window_view(raw_data[:prefix_len],
                                          window_shape=seq, axis=0)
        raw_win = raw_win_all[:N].transpose(0, 2, 1).astype(
            np.float64)  # (N, seq, F)

        # Scaled raw windows for final X
        raw_scaled_win_all = sliding_window_view(raw_scaled_all[:prefix_len],
                                                 window_shape=seq, axis=0)
        raw_scaled_win = raw_scaled_win_all[:N].transpose(0, 2, 1).astype(
            np.float32)  # (N, seq, F)

        # Target price windows for 'past' engineered channel (1D -> (N+1, seq) -> (N, seq))
        tgt_price_win_all = sliding_window_view(target_price[:prefix_len],
                                                window_shape=seq, axis=0)
        tgt_price_win = tgt_price_win_all[:N].astype(np.float64)  # (N, seq)

        # Target return windows for correlation (1D -> (N+1, seq) -> (N, seq))
        tgt_ret_win_all = sliding_window_view(tgt_ret[:prefix_len],
                                              window_shape=seq, axis=0)
        tgt_ret_win = tgt_ret_win_all[:N].astype(np.float64)  # (N, seq)

        # ---------- vectorized Pearson corr(raw_feature, target_return) per window ----------
        A = raw_win  # (N, seq, F)
        B = tgt_ret_win  # (N, seq)
        A_mean = A.mean(axis=1, keepdims=True)  # (N, 1, F)
        B_mean = B.mean(axis=1, keepdims=True)  # (N, 1)
        A_c = A - A_mean  # (N, seq, F)
        B_c = (B - B_mean)[..., None]  # (N, seq, 1)
        if A_c.shape[:2] != B_c.shape[:2]:
            raise ValueError(
                f"Window shape mismatch: raw {A_c.shape} vs tgt {B_c.shape}")
        num = (A_c * B_c).sum(axis=1)  # (N, F)
        den = np.sqrt((A_c ** 2).sum(axis=1) * (B_c ** 2).sum(axis=1)) + 1e-12
        corr = np.clip(num / den, -1.0, 1.0)  # (N, F)
        corr_mean = corr.mean(axis=1).astype(np.float32)  # (N,)
        corr_seq = np.repeat(corr_mean[:, None], seq, axis=1)  # (N, seq)

        # ---------- engineered channels from PRICES (standardize, then weight) ----------
        # 1) past_trends: direct from (N, seq)
        past_all = tgt_price_win  # (N, seq)

        # 2) key_timestamps: take last 2*seq prices with left padding to ensure availability
        #    Build padded series so every sample has a 2*seq history aligned to the same endpoints.
        pad_left = np.repeat(target_price[:1], seq)  # (seq,)
        padded = np.concatenate(
            [pad_left, target_price[:prefix_len]])  # (seq + prefix_len,)
        two_seq_all = sliding_window_view(padded, window_shape=2 * seq,
                                          axis=0)  # (prefix_len - seq + 1, 2*seq) == (N+1, 2*seq)
        two_seq_all = two_seq_all[:N]  # (N, 2*seq), aligned with past windows

        # stride by 5, then pad/trim to seq using edge replication
        keys = two_seq_all[:, ::5]  # (N, ceil(2*seq/5))
        if keys.shape[1] < seq:
            pad = np.repeat(keys[:, -1:], seq - keys.shape[1], axis=1)
            keys = np.concatenate([keys, pad], axis=1)
        elif keys.shape[1] > seq:
            keys = keys[:, -seq:]
        key_all = keys.astype(np.float64)  # (N, seq)

        # z-score per engineered channel across samples×time
        def zscore_channel(C: np.ndarray) -> np.ndarray:
            m = C.mean()
            s = C.std()
            if not np.isfinite(s) or s == 0.0:
                s = 1.0
            return ((C - m) / s).astype(np.float32)

        past_z = zscore_channel(past_all)  # (N, seq)
        key_z = zscore_channel(key_all)  # (N, seq)
        corr_z = corr_seq.astype(np.float32)  # already bounded [-1,1]

        # Apply your weights AFTER standardization; do not rescale later
        past_w = 0.60 * past_z
        key_w = 0.20 * key_z
        corr_w = 0.20 * corr_z

        # final shape check to avoid the stack error you saw
        if not (past_w.shape == key_w.shape == corr_w.shape == (N, seq)):
            raise ValueError(
                f"Engineered channel shape mismatch: past{past_w.shape} key{key_w.shape} corr{corr_w.shape} expected {(N, seq)}")

        triple = np.stack([past_w, key_w, corr_w], axis=-1)  # (N, seq, 3)

        # ---------- assemble X and y ----------
        X = np.concatenate([triple, raw_scaled_win], axis=-1)  # (N, seq, 3+F)
        y = target_price[seq + fwd: seq + fwd + N].astype(
            np.float32)  # future price label

        # ---------- sanity ----------
        if X.ndim != 3:
            raise ValueError(f"Shape mismatch in X => {X.shape}")
        if y.ndim != 1 or y.shape[0] != N:
            raise ValueError(
                f"Shape mismatch in y => {y.shape}, expected ({N},)")

        return X, y, self.input_scaler

    # def transform_multi_scale_with_weights(self,
    #                                        dataframe,
    #                                        target_currency: str,
    #                                        short_seq: int = 10,
    #                                        medium_seq: int = 50,
    #                                        long_seq: int = 150):
    #     if dataframe.empty:
    #         raise ValueError("Dataframe is empty. No data to process.")
    #     dataframe.sort_values("last_updated", inplace=True)
    #     target_col = f"{target_currency.lower()}_price"
    #     numeric_cols = dataframe.select_dtypes(include=["number"]).columns
    #     raw_input_cols = [col for col in numeric_cols if col != target_col]
    #     dataframe[raw_input_cols] = dataframe[raw_input_cols].pct_change(fill_method=None)
    #     dataframe.dropna(subset=raw_input_cols, inplace=True)
    #
    #     target_data = dataframe[target_col].values
    #     raw_data = dataframe[raw_input_cols].values
    #     total_len = len(dataframe)
    #     future_steps = self.future_steps
    #
    #     largest_window = max(short_seq, medium_seq, long_seq)
    #     if total_len < largest_window + future_steps:
    #         raise ValueError(
    #             "Not enough data to form short, medium, and long windows.")
    #     short_blocks = []
    #     medium_blocks = []
    #     long_blocks = []
    #     future_prices = []
    #
    #     def mean_corr(subX, subY):
    #         x_mean = subX.mean(axis=0)
    #         y_mean = subY.mean()
    #         numerator = ((subX - x_mean) * (subY[:, None] - y_mean)).sum(
    #             axis=0)
    #         denom_x = ((subX - x_mean) ** 2).sum(axis=0)
    #         denom_y = ((subY - y_mean) ** 2).sum()
    #         denom = np.sqrt(denom_x * denom_y)
    #         valid = denom != 0
    #         corrs = np.zeros_like(denom, dtype=np.float32)
    #         corrs[valid] = numerator[valid] / denom[valid]
    #         return corrs.mean()
    #     last_index = total_len - future_steps
    #     for i in range(largest_window, last_index):
    #         short_start = i - short_seq
    #         short_past = target_data[short_start: i]
    #         short_past_w = short_past * 0.60
    #
    #         short_key_start = max(0, i - 2 * short_seq)
    #         short_keys = target_data[short_key_start: i: 5]
    #         if len(short_keys) < short_seq:
    #             needed = short_seq - len(short_keys)
    #             short_keys = np.hstack([short_keys, np.zeros(needed)])
    #         short_keys = short_keys[-short_seq:]
    #         short_keys_w = short_keys * 0.20
    #
    #         short_subX = raw_data[short_start: i]
    #         short_subY = target_data[short_start: i]
    #         short_corr_val = mean_corr(short_subX, short_subY)
    #         short_corr_array = np.full(short_seq, short_corr_val * 0.20,
    #                                    dtype=np.float32)
    #
    #         short_block = np.column_stack(
    #             [short_past_w, short_keys_w, short_corr_array])
    #         short_block = np.hstack([short_block,
    #                                  short_subX])
    #         short_blocks.append(short_block)
    #         med_start = i - medium_seq
    #         med_past = target_data[med_start: i]
    #         med_past_w = med_past * 0.60
    #
    #         med_key_start = max(0, i - 2 * medium_seq)
    #         med_keys = target_data[med_key_start: i: 5]
    #         if len(med_keys) < medium_seq:
    #             needed = medium_seq - len(med_keys)
    #             med_keys = np.hstack([med_keys, np.zeros(needed)])
    #         med_keys = med_keys[-medium_seq:]
    #         med_keys_w = med_keys * 0.20
    #
    #         med_subX = raw_data[med_start: i]
    #         med_subY = target_data[med_start: i]
    #         med_corr_val = mean_corr(med_subX, med_subY)
    #         med_corr_array = np.full(medium_seq, med_corr_val * 0.20,
    #                                  dtype=np.float32)
    #
    #         med_block = np.column_stack(
    #             [med_past_w, med_keys_w, med_corr_array])
    #         med_block = np.hstack([med_block, med_subX])
    #         medium_blocks.append(med_block)
    #         long_start = i - long_seq
    #         lng_past = target_data[long_start: i]
    #         lng_past_w = lng_past * 0.60
    #         lng_key_start = max(0, i - 2 * long_seq)
    #         lng_keys = target_data[lng_key_start: i: 5]
    #         if len(lng_keys) < long_seq:
    #             needed = long_seq - len(lng_keys)
    #             lng_keys = np.hstack([lng_keys, np.zeros(needed)])
    #         lng_keys = lng_keys[-long_seq:]
    #         lng_keys_w = lng_keys * 0.20
    #         lng_subX = raw_data[long_start: i]
    #         lng_subY = target_data[long_start: i]
    #         lng_corr_val = mean_corr(lng_subX, lng_subY)
    #         lng_corr_array = np.full(long_seq, lng_corr_val * 0.20,
    #                                  dtype=np.float32)
    #         lng_block = np.column_stack(
    #             [lng_past_w, lng_keys_w, lng_corr_array])
    #         lng_block = np.hstack([lng_block, lng_subX])
    #         long_blocks.append(lng_block)
    #         future_label = target_data[i + future_steps]
    #         future_prices.append(future_label)
    #     short_blocks = np.array(short_blocks,
    #                             dtype=np.float32)
    #     medium_blocks = np.array(medium_blocks,
    #                              dtype=np.float32)
    #     long_blocks = np.array(long_blocks,
    #                            dtype=np.float32)
    #     future_prices = np.array(future_prices,
    #                              dtype=np.float32)
    #     combined_flat = np.concatenate([
    #         short_blocks.flatten(),
    #         medium_blocks.flatten(),
    #         long_blocks.flatten()
    #     ]).reshape(-1, 1)
    #     combined_scaled = self.input_scaler.fit_transform(combined_flat)
    #     s_size = short_blocks.size
    #     m_size = medium_blocks.size
    #     short_scaled_flat = combined_scaled[:s_size]
    #     medium_scaled_flat = combined_scaled[s_size: s_size + m_size]
    #     long_scaled_flat = combined_scaled[s_size + m_size:]
    #     short_scaled = short_scaled_flat.reshape(short_blocks.shape)
    #     medium_scaled = medium_scaled_flat.reshape(medium_blocks.shape)
    #     long_scaled = long_scaled_flat.reshape(long_blocks.shape)
    #     return short_scaled, medium_scaled, long_scaled, future_prices, self.input_scaler

    def transform_multi_scale_with_weights(
            self,
            dataframe,
            target_currency: str,
            short_seq: int = 10,
            medium_seq: int = 50,
            long_seq: int = 150,
    ):
        import numpy as np
        import pandas as pd
        from numpy.lib.stride_tricks import sliding_window_view

        if dataframe.empty:
            raise ValueError("Dataframe is empty. No data to process.")

        target_col = f"{target_currency.lower()}_price"

        # --- robust sort by datetime ---
        df = dataframe.copy()
        if "last_updated" not in df.columns:
            raise ValueError("Missing 'last_updated' column.")
        if not np.issubdtype(df["last_updated"].dtype, np.datetime64):
            df["last_updated"] = pd.to_datetime(df["last_updated"],
                                                errors="coerce")
        df = df.sort_values("last_updated").reset_index(drop=True)

        # --- numeric columns & returns for raw inputs ---
        numeric_cols = df.select_dtypes(include=["number"]).columns
        if target_col not in numeric_cols:
            raise ValueError(f"Missing target column '{target_col}'.")
        raw_input_cols = [c for c in numeric_cols if c != target_col]

        # raw inputs as pct-change returns (your original choice)
        df[raw_input_cols] = df[raw_input_cols].pct_change(fill_method=None)
        df = df.dropna(subset=raw_input_cols).reset_index(drop=True)

        target_price = df[target_col].to_numpy(
            dtype=np.float32)  # prices for engineered channels + label
        raw_data = df[raw_input_cols].to_numpy(
            dtype=np.float32)  # returns features (T, F)

        # target returns (for correlation)
        tgt_ret = df[target_col].pct_change(fill_method=None).to_numpy(
            dtype=np.float32)
        if np.isnan(tgt_ret[0]):
            tgt_ret[0] = 0.0

        T = len(df)
        fwd = self.future_steps
        L = max(short_seq, medium_seq, long_seq)  # longest window
        prefix_len = T - fwd
        if prefix_len < L:
            raise ValueError(
                "Not enough data to form short, medium, and long windows.")

        # Common N across all scales (align endpoints to the longest window)
        # Endpoints i run from (L-1) .. (prefix_len-1) inclusive
        N = prefix_len - L + 1
        F = raw_data.shape[1]

        # --- scale ONLY raw returns once; reuse across scales ---
        if hasattr(self.input_scaler, "scale_"):
            raw_scaled_all = self.input_scaler.transform(raw_data)  # (T, F)
        else:
            raw_scaled_all = self.input_scaler.fit_transform(raw_data)

        # ---- helpers ----
        def make_windows_2d(X, s):
            """X:(T,F) -> (N, s, F) aligned to endpoints of longest window."""
            w_all = sliding_window_view(X[:prefix_len], window_shape=s,
                                        axis=0)  # (prefix_len - s + 1, F, s)
            w_all = w_all.transpose(0, 2, 1)  # (.., s, F)
            start = L - s
            end = start + N
            return w_all[start:end]  # (N, s, F)

        def make_windows_1d(x, s):
            """x:(T,) -> (N, s) aligned to endpoints of longest window."""
            w_all = sliding_window_view(x[:prefix_len], window_shape=s,
                                        axis=0)  # (prefix_len - s + 1, s)
            start = L - s
            end = start + N
            return w_all[start:end].astype(np.float64)  # (N, s)

        def make_key_windows(price_series, s, stride=5):
            """
            Build (N, 2*s) price windows with left padding so early samples have history,
            then stride by 'stride' and pad/trim to length s.
            """
            pad_left = np.repeat(price_series[:1], s)  # (s,)
            padded = np.concatenate(
                [pad_left, price_series[:prefix_len]])  # (s + prefix_len,)
            two_all = sliding_window_view(padded, window_shape=2 * s,
                                          axis=0)  # (prefix_len - s + 1, 2*s)
            start = L - s
            end = start + N
            two = two_all[start:end]  # (N, 2*s)
            keys = two[:, ::stride]  # (N, ceil(2*s/stride))
            if keys.shape[1] < s:
                pad = np.repeat(keys[:, -1:], s - keys.shape[1], axis=1)
                keys = np.concatenate([keys, pad], axis=1)
            elif keys.shape[1] > s:
                keys = keys[:, -s:]
            return keys.astype(np.float64)  # (N, s)

        def zscore_channel(C):
            """C: (N, s) -> z-scored over samples×time (use TRAIN stats in your pipeline)."""
            m = C.mean()
            s = C.std()
            if not np.isfinite(s) or s == 0.0:
                s = 1.0
            return ((C - m) / s).astype(np.float32)

        def build_scale(s):
            """Return X_s (N, s, 3+F) and re-use y later."""
            # raw windows (unscaled for correlation), scaled windows for final X
            raw_win = make_windows_2d(raw_data, s).astype(
                np.float64)  # (N, s, F)
            raw_scaled_win = make_windows_2d(raw_scaled_all, s).astype(
                np.float32)  # (N, s, F)

            # target windows
            tgt_price_win = make_windows_1d(target_price, s)  # (N, s)
            tgt_ret_win = make_windows_1d(tgt_ret, s)  # (N, s)

            # vectorized Pearson corr(raw_feature, target_return) per window
            A = raw_win  # (N, s, F)
            B = tgt_ret_win  # (N, s)
            A_mean = A.mean(axis=1, keepdims=True)  # (N,1,F)
            B_mean = B.mean(axis=1, keepdims=True)  # (N,1)
            A_c = A - A_mean  # (N,s,F)
            B_c = (B - B_mean)[..., None]  # (N,s,1)
            num = (A_c * B_c).sum(axis=1)  # (N,F)
            den = np.sqrt(
                (A_c ** 2).sum(axis=1) * (B_c ** 2).sum(axis=1)) + 1e-12
            corr = np.clip(num / den, -1.0, 1.0)  # (N,F)
            corr_mean = corr.mean(axis=1).astype(np.float32)  # (N,)
            corr_seq = np.repeat(corr_mean[:, None], s, axis=1)  # (N, s)

            # engineered channels from PRICES
            past_all = tgt_price_win  # (N, s)
            key_all = make_key_windows(target_price, s)  # (N, s)

            # z-score engineered channels per scale, then apply weights
            past_w = 0.60 * zscore_channel(past_all)
            key_w = 0.20 * zscore_channel(key_all)
            corr_w = 0.20 * corr_seq.astype(
                np.float32)  # already bounded [-1,1]

            # stack engineered channels and concat with scaled raw returns
            triple = np.stack([past_w, key_w, corr_w], axis=-1)  # (N, s, 3)
            X_s = np.concatenate([triple, raw_scaled_win],
                                 axis=-1)  # (N, s, 3+F)
            return X_s

        # Build each scale
        X_short = build_scale(short_seq)
        X_medium = build_scale(medium_seq)
        X_long = build_scale(long_seq)

        # labels aligned to endpoints i in [L-1, prefix_len-1]
        y = target_price[(L - 1) + fwd: (L - 1) + fwd + N].astype(
            np.float32)  # (N,)

        # sanity
        if y.ndim != 1 or y.shape[0] != X_long.shape[0]:
            raise ValueError(
                f"Label shape mismatch: y{y.shape} vs N{X_long.shape[0]}")
        if not (X_short.shape[0] == X_medium.shape[0] == X_long.shape[
            0] == N):
            raise ValueError("N mismatch among scales.")

        return X_short, X_medium, X_long, y, self.input_scaler

    @staticmethod
    def get_steps_from_now(future_time: datetime) -> int:
        future_time_seconds: float = future_time.timestamp() - datetime.now().timestamp()
        return int(future_time_seconds / 5)