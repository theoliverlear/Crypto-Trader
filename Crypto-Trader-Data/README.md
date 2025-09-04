# Crypto Trader — Data Module
## The data backbone powering real‑time markets, signals, and scale

---

Crypto‑Trader‑Data is the platform’s source‑of‑truth for market intelligence. 
It harvests live cryptocurrency prices and maintains clean historical series,
ingests news sentiment, and operates an internal API that moves high‑volume
data to and from other services — especially the Analysis module for
machine‑learning predictions. Built for 24/7 operation, it keeps the rest of
Crypto Trader fast, consistent, and informed.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

### What this module is
- The system of record for market snapshots, price history, and signal inputs.
- A high‑throughput internal API for data exchange across the platform.
- The bridge between harvested raw data and actionable intelligence for 
  trading.

## ⭐️ What it does
- Market data harvesting and snapshots
  - Collects current prices and maintains historical series for supported 
    currencies.
  - Serves snapshots and histories to downstream components.
- News and sentiment ingestion
  - Accepts sentiment events from harvesters to enrich market context for
    strategies.
- Predictions and training lifecycle exchange
  - Receives price predictions and persists training session summaries coming 
    back from the Analysis service.
- Internal data API (built for volume)
  - Clean endpoints purpose‑built for bulk data transfer within the platform 
    (UI/API/Engine/Analysis).
- Operational readiness
  - Runs scheduled and asynchronous jobs, provides health and docs endpoints.

## ✅ Why it matters
- Always‑on truth: one place that keeps prices, histories, and signals 
  authoritative.
- Built for scale: designed to move large datasets reliably across services.
- Decouples compute from storage: Analysis trains and predicts while Data 
  guarantees availability and structure.
- Consistent contracts: shared models and stable endpoints across the platform.

## 🚀 How it works at a glance
1. Harvest and normalize market data (prices) and store to PostgreSQL.
2. Expose fast read endpoints for snapshots and historical series.
3. Ingest additional signals (news sentiment) from harvester services.
4. Receive predictions and training session metadata from the Analysis module.
5. Operate on schedules/async to keep data fresh around the clock.

Notes:
- On startup, the module can generate/update supported currencies via 
  `CurrencyJsonGenerator`; control with env var 
  `CRYPTO_TRADER_LOAD_CURRENCIES` or JVM flag 
  `-Dcryptotrader.loadCurrencies=false`.
- OpenAPI docs and Actuator endpoints are available for internal use.

## 🔗 Where it fits in the platform
- Feeds the web/API layer with up‑to‑date market snapshots and histories.
- Supplies and receives data from the Analysis module (training, predictions).
- Supports the trading Engine with reliable data contracts.
- Persists structured data in PostgreSQL (H2 available at runtime for 
  dev/test).

## 🔌 Internal API highlights
These endpoints are consumed by first‑party services and tools inside Crypto 
Trader.

- Currency data
  - `GET /data/currency/all`
    - Returns a snapshot of current values for tracked currencies
      (`DisplayCurrencyListResponse`).
  - `GET /data/currency/history/{code}?hours=24&intervalSeconds=60`
    - Returns a time‑series list of `TimeValueResponse` objects for the given 
      currency code. Responds `204 No Content` when no data.

- News sentiment ingestion
  - `POST /data/news-sentiment/add`
    - Accepts a `NewsSentimentRequest` and returns `200 OK` with 
      `OperationSuccessfulResponse`.

- Predictions
  - `POST /data/predictions/add`
    - Accepts a `PricePredictionRequest` and returns `202 Accepted` with 
      `PredictionIdResponse` containing the saved prediction id.

- Training sessions
  - `POST /data/training-session/add`
    - Accepts a `TrainingSessionRequest` and returns `200 OK` with 
     `OperationSuccessfulResponse`.


## 🔒 Safety, privacy, and control
- Internal‑only consumption: endpoints are intended for first‑party modules and
  operators.
- Governed access: align with platform authentication/authorization (Spring 
  Security present in the stack).
- Operational guardrails: start in paper/test modes; never expose secrets in 
  logs; prefer least‑privilege configs.

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Frameworks: Spring Boot (Web, Data JPA, JDBC, WebSocket, Thymeleaf, 
  Security), Actuator, Springdoc OpenAPI
- Persistence: PostgreSQL (primary); H2 (runtime/dev)
- Messaging/HTTP: Apache HttpClient (internal integrations)
- Logging & tooling: Logback, JUnit Jupiter, Maven, Dokka
- Runtime features: `@EnableAsync`, `@EnableScheduling`, OpenAPI UI


## 📝 Conventions
- Keep data contracts stable; evolve via additive changes when possible.
- Prefer batched/bulk operations for high‑volume transfers.
- Validate and timestamp incoming data at the edge of the API.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
