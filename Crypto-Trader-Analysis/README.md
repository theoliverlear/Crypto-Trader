# Crypto Trader — Analysis Module
## Machine learning and market analytics engine

---

The Analysis module powers Crypto Trader’s price prediction and model 
training. It ingests market data from PostgreSQL, engineers features, trains
LSTM‑based models (including multi‑layer variants), generates predictions, and
reports results back to the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What this module is
- A Python analytics service used by Crypto Trader’s core platform.
- Strategic ML engine: LSTM, complex LSTM, and multi‑layer LSTM variants.
- Feature engineering pipeline with robust scaling and time‑windowed sequences.
- Training, prediction, and reporting utilities that integrate with the Django
  API.

## 🧭 Key capabilities
- Data access
  - Pulls market snapshots from PostgreSQL via SQLAlchemy; bulk `COPY` for
    speed.
  - Supports multiple query modes: `current_price`, `historical`,
    `historical_spaced`.
- Feature engineering
  - Sliding time windows (configurable `sequence_length`).
  - Engineered channels: past trends, key timestamps, correlation with target 
    returns.
  - Multi‑scale preprocessing for multi‑layer models (short/medium/long
    horizons).
- Models and training
  - LSTM models with Keras/TensorFlow; early stopping, LR scheduling,
    checkpoints.
  - Multi‑layer models combine short, medium, and long sequences.
  - GPU execution when available; CPU fallback supported.
- Predictions and reporting
  - Generates next‑step price predictions for a target currency.
  - Posts predictions and training session summaries to the platform API.
  - TensorBoard logs for model inspection.

## 🔗 How it fits into Crypto Trader
- Input: Market snapshot data from the platform database (PostgreSQL).
- Output: Trained model artifacts under `models/...`, predicted prices,
  metrics.
- Integration: Sends structured payloads to the Django API.

## 🧰 Operation
This module is operated by project owners alongside the Django API and two
GPUs for training.

- Environment and data
  - Database: PostgreSQL
  - Market snapshots table must be populated; `currencies.json` is used for
    price column discovery.
- GPU setup
  - Two‑GPU operation is supported. Utilities in the codebase and the scripts
    under `gpu/` demonstrate splitting currency workloads across devices.
  - Example GPU launcher scripts live under `gpu/` (e.g.,
    `gpu/run_gpu_zero.py`).
- Common operator actions
  - Train a single‑currency LSTM model
    - Entry: `src/apps/learning/models/training/training_session.py`
  - Train a multi‑layer model (short/medium/long sequences)
    - Entry: `src/apps/learning/models/training/training_session.py`
  - Make a one‑off prediction for a currency
    - Entry: `src/apps/learning/models/training/training_session.py`
  - Prediction and reporting lifecycle
    - Entry: `src/apps/learning/models/training/training_session.py`
    - Note: `TrainingSession.train()` will compute a prediction after training
      and report it via the platform integration.
    - Standalone utilities for sending predictions also exist under 
      `apps/learning/models/prediction/predictions.py` if needed.
  - Run a GPU‑oriented launcher
    - Scripts under `gpu/` (e.g., `gpu/run_gpu_zero.py`, 
     `gpu/run_gpu_one.py`).

Notes:
- Training hyperparameters can be chosen via `TrainingType` 
  (`src/apps/learning/models/training/training_type.py`) or by building a 
  `TrainingModel`.
- Multi‑layer models live under `src/apps/learning/models/ai/lstm/layered/...`.

## 🔒 Safety, privacy, and control
- This module does not manage exchange API keys directly; it trains/predicts 
  from database data.
- Start with small datasets and paper trading.
- Guardrails (e.g., position sizing, stop loss) are enforced by the trading
  engine, not this module.

## 🛠️ Technology in this module
- Python 3
- TensorFlow + Keras
- NumPy, Pandas, scikit‑learn
- SQLAlchemy (COPY to CSV optimization)
- Requests (HTTP)
- `attrs` / `typing_extensions`

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
