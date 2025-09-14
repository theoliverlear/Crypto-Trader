# Crypto Trader — Api-Library
## Composite API library for shared API building blocks

---

Api‑Library aggregates the API submodules used across the platform. It brings
together communication DTOs, reusable components, domain models, repositories,
services, and events into one dependency for consumers that need most API
capabilities without wiring each artifact individually.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Api‑Communication, Api‑Components, Api‑Models, Api‑Repositories,
    Api‑Services, Api‑Events.
- Provides a single import
  - Pull one dependency to access the majority of API foundations.

## ✅ Why it matters
- Faster development: fewer moving parts for app modules.
- Consistent, compatible versions of all API building blocks.

## 🔗 Where it fits in the platform
- Consumed by Api, Data, Engine, Admin, and tools needing shared API contracts.
- Sits under the broader Library umbrella.

## 🛠️ Technology at a glance
- Language: Java + Kotlin
- Build: Maven, Dokka for reference docs

## 📝 Conventions
- Keep this aggregator lean; avoid adding app‑specific code here.
- Maintain backward‑compatible changes to shared contracts.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
