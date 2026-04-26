# Crypto Trader — Data Library
## Shared contracts for data access, persistence, and storage

---

Data‑Library aggregates the shared pieces used by the Crypto Trader Data
module. It centralizes communication DTOs, components, domain models,
repositories, and services so the data layer evolves consistently with
the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Data‑Communication: DTO data exchange models.
  - Data‑Components: reusable data handling utilities and storage adapters.
  - Data‑Models: database entities and market data structures.
  - Data‑Repositories: Spring Data repositories and custom data access.
  - Data‑Services: data processing, validation, and market data aggregation.

## ✅ Why it matters
- Keeps data‑related types in one place, reducing drift across services.
- Enables consistent evolution of persistence and storage contracts.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Data and any service that reads or writes market data.
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
