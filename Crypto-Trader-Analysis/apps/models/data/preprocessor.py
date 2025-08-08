# preprocessor.py

import numpy as np
from attr import attr
from attrs import define
from numpy.lib.stride_tricks import sliding_window_view
from sklearn.preprocessing import MinMaxScaler
from datetime import datetime

@define
class Preprocessor:
    sequence_length: int = attr(default=10)
    future_steps: int = attr(default=1)
    input_scaler: MinMaxScaler = attr(default=MinMaxScaler(feature_range=(0, 1)))

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