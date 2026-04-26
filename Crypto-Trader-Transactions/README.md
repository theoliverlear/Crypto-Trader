# Crypto Trader — Transactions Module
## Order execution, historical records, and financial auditing

---

Crypto‑Trader‑Transactions manages the full lifecycle of trade orders within
the Crypto Trader platform. It handles order execution, maintains historical
transaction records, and provides the data needed for financial auditing and
compliance.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Order execution
  - Submit, validate, and execute buy/sell orders against exchange APIs.
- Transaction history
  - Persist every executed trade for reporting, analytics, and audit trails.
- Financial auditing
  - Expose structured records that support compliance and reconciliation.

## ✅ Why it matters
- Provides an authoritative record of all platform trading activity.
- Decouples order management from the Engine so each can evolve independently.

## 🔗 Where it fits in the platform
- Receives execution requests from Crypto‑Trader‑Engine.
- Stores records consumed by Admin, Analysis, and Data modules.
- Integrates with exchange adapters via the API layer.

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven
- Dependencies: Spring Boot, shared Library modules

## 📝 Conventions
- Treat all transaction writes as idempotent where possible.
- Never silently discard failed orders; always record the outcome.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
