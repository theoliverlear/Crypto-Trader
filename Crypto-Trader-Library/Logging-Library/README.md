# Crypto Trader — Logging Library
## Shared contracts for logging, events, and infrastructure

---

Logging‑Library aggregates the shared pieces used by the Crypto Trader Logging
module. It centralizes communication DTOs, configuration, event contracts,
infrastructure filters, domain models, repositories, and services so logging
evolves consistently with the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Logging‑Communication: logging DTOs and transport contracts.
  - Logging‑Config: auto‑configuration, filters, and logback settings.
  - Logging‑Events: Kafka event payloads for inter‑service messaging.
  - Logging‑Infrastructure: cross‑cutting beans and filters.
  - Logging‑Models: domain models and entities.
  - Logging‑Repositories: Spring Data JPA repositories.
  - Logging‑Services: business logic for logging.

## ✅ Why it matters
- Keeps logging‑related types in one place, reducing drift across services.
- Enables consistent evolution of observability contracts.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Logging and any service that emits logs.
- Built as part of the broader shared Library umbrella.

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven multi‑module
- Docs: Dokka

## 📝 Conventions
- Evolve contracts additively and maintain backward compatibility.
- Keep models minimal and well‑documented.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
