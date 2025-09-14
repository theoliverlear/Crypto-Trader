# Crypto Trader — Externals-Library
## Aggregation point for external integrations

---

Externals‑Library is the umbrella for third‑party integration wrappers used by
Crypto Trader. It provides a consistent place to add and document normalized
adapters for external services.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates normalized external adapters and utilities
- Encourages consistent error handling and logging

## ✅ Why it matters
- Reduces drift in how services talk to third‑party APIs
- Centralizes common patterns and resilience policies

## 🔗 Where it fits in the platform
- Used by services that interact with external providers
- Part of the shared Library umbrella

## 🛠️ Technology at a glance
- Language: Java/Kotlin
- Build: Maven, Dokka for docs

## 📝 Conventions
- Keep wrappers thin and well‑documented; favor composition
- Redact secrets and sensitive metadata in logs

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
