# Crypto Trader — Api-Models
## Core domain models and entities for APIs

---

Api‑Models contains the typed domain models used across Crypto Trader services
and APIs. These classes represent users, portfolios, currencies, snapshots,
predictions, and related entities that bind UI, services, and persistence
consistently.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Defines core entities and value objects
- Supports serialization for REST/WebSocket
- Integrates with JPA for persistence (where applicable)

## ✅ Why it matters
- One source of truth for domain types reduces drift across modules
- Strong typing catches breaking changes early

## 🔗 Where it fits in the platform
- Used by Api, Data, Engine, Admin, and libraries
- Forms the base for repositories and services

## 🛠️ Technology at a glance
- Frameworks: Spring Boot (provided), Hibernate/JPA (provided)
- Serialization: Jackson (provided)
- Build: Maven

## 📝 Conventions
- Document units and timezones on temporal/numeric fields
- Evolve models additively; avoid breaking changes

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
