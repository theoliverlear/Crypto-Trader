# Crypto Trader — Health Library
## Shared contracts for health monitoring, probes, and dependency checks

---

Health‑Library aggregates the shared pieces used by the Crypto Trader Health
module. It centralizes communication protocols, components, configuration,
domain models, repositories, and services so health monitoring evolves
consistently with the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Health‑Communication: health check transport contracts.
  - Health‑Components: reusable Spring beans for monitoring.
  - Health‑Config: auto‑configuration and properties.
  - Health‑Models: domain models and entities.
  - Health‑Repositories: Spring Data JPA repositories.
  - Health‑Services: business logic for health monitoring.

## ✅ Why it matters
- Keeps health‑related types in one place, reducing drift across services.
- Enables consistent evolution of monitoring and probing contracts.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Health and services that report status.
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
