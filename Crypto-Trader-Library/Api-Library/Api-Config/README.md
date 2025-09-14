# Crypto Trader — Api-Config
## Shared Spring Boot configuration and infrastructure beans

---

Api‑Config centralizes common Spring configuration for Crypto Trader services:
security and web setup, messaging (e.g., Kafka), JPA/JDBC wiring, and other
infrastructure concerns that should be consistent across modules.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Security, web, and actuator configuration
- Messaging (Kafka) and serialization settings
- Common infrastructure beans for APIs and services

## ✅ Why it matters
- Consistent, auditable defaults for platform services
- Less duplication; safer changes rolled out in one place

## 🔗 Where it fits in the platform
- Used by Api, Data, Engine, and supporting services
- Part of Api‑Library and published in Docs reference

## 🛠️ Technology at a glance
- Frameworks: Spring Boot (Web, Security, JPA, WebSocket)
- Messaging: Spring Cloud Stream (Kafka)
- Build: Maven

## 📝 Conventions
- Prefer explicit configuration with clear defaults
- Keep secrets out of code; use environment/config servers

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
