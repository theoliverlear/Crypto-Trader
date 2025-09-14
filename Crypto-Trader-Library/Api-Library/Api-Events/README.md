# Crypto Trader — Api-Events
## Event contracts for inter-service messaging

---

Api‑Events defines event payloads and related contracts used for messaging
between Crypto Trader services (e.g., via Kafka). These types make service
interactions explicit and auditable.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Event payloads for cross‑service workflows
- Supports observability and audit trails
- Works with Api‑Config messaging setup

## ✅ Why it matters
- Clear, well‑typed messages reduce integration ambiguity
- Easier to evolve and monitor distributed workflows

## 🔗 Where it fits in the platform
- Emitted/consumed by Api, Data, Engine, Admin, and other services
- Lives under Api‑Library; published in Docs reference

## 🛠️ Technology at a glance
- Language: Kotlin + Java interop
- Frameworks: Spring (context), Kafka via Spring Cloud Stream
- Build: Maven

## 📝 Conventions
- Keep events backward‑compatible; use versioned topics or payloads when needed
- Include necessary context without exposing secrets

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
