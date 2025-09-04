# Crypto Trader — API Module
## Core backend for identity, portfolios, and platform orchestration

---

Crypto‑Trader‑Api is the core web backend that signs users in, manages accounts
and portfolios, serves profile media, exposes WebSocket endpoints, and glues 
the platform together. It provides the stable, session‑aware API that the UI 
and internal tools rely on — while heavy data and trading workloads run in the
Data and Engine modules.

Important: Past results and backtests do not guarantee future performance. 
Always start in paper mode. Liability is your own.

Note on scope changes: Market harvesting, news/sentiment ingestion, and 
prediction exchange now live in `Crypto‑Trader‑Data`. The live trading loop
runs in `Crypto‑Trader‑Engine`. This module focuses on the application API 
(identity, accounts, portfolios, and orchestration).

## ⭐️ What it does
- Identity and sessions (`/api/authorize/...`)
  - `POST /api/authorize/signup`, `POST /api/authorize/login`, 
    `GET /api/authorize/isloggedin`, `GET /api/authorize/logout` using 
    session‑based auth.
- Accounts and profile media (`/api/account/...`)
  - Upload and serve user profile pictures; check if a user has a profile
    image (`/image/upload`, `/get/{id}/profile-picture`, 
    `/get/{id}/has-profile-picture`).
- Portfolio management and insights (`/api/portfolio/...`)
  - Portfolio presence, history, and profit endpoints (e.g., `/history/get`, 
    `/history/profit`, `/empty`).
- Currency utilities (`/api/currency/value`)
  - Calculate asset value for a currency and share count. Disabled in docs 
    profile via `@Profile("!docs")`.
- Real‑time gateway (WebSocket)
  - Trader WebSocket endpoint at `/websocket/trader` for live UI updates.
- OpenAPI + Actuator
  - Live API docs (Springdoc) and health/metrics endpoints for observability.
- Platform glue
  - Uses shared API models, services, repositories, and components to 
    coordinate with Data/Engine without duplicating responsibilities.

## ✅ Why it matters
- One trusted backend for users, sessions, portfolios, and UI actions.
- Clear separation of concerns: Api handles application flows; Data handles 
  heavy data; Engine executes trades.
- Stable contracts and session semantics for a consistent product experience.

## 🔗 Where it fits in the platform
- Front‑end and tools call Api for authentication, accounts, portfolios, and
  real‑time updates.
- Reads/writes structured entities in PostgreSQL via JPA.
- Collaborates with:
  - Data — for market snapshots, histories, predictions, and sentiment
    (internal calls/DTOs).
  - Engine — for executing strategies under guardrails.

## 🔒 Safety, privacy, and control
- Session‑protected endpoints safeguard user actions and media.
- Follow least‑privilege principles; never log secrets.
- Read and respect the LICENSE; you are responsible for outcomes.

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Frameworks: Spring Boot (Web, Security, Data JPA, JDBC, WebSocket,
  Web Services), Actuator, Springdoc OpenAPI, Thymeleaf
- Persistence: PostgreSQL (primary), H2 (runtime/dev)
- Logging & tooling: Logback, JUnit Jupiter, Maven, Dokka/Javadoc
- Shared libraries: `api‑library` (models, components, repositories, services,
  communication)

## 📝 Conventions
- Evolve HTTP contracts additively; keep DTOs backward‑compatible when 
  possible.
- Keep endpoints session‑aware when operating on user resources.
- Use backticks for inline code and endpoint examples in docs.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
