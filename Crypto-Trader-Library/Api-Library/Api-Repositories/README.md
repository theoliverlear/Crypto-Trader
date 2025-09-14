# Crypto Trader — Api-Repositories
## Spring Data repositories for domain persistence

---

Api‑Repositories provides Spring Data JPA repository interfaces for the
platform’s domain models. It standardizes persistence access patterns and
reduces duplication across services.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Repository interfaces for core entities
- Common query patterns for services
- Integrates with JPA and transaction management

## ✅ Why it matters
- Consistent, testable persistence layer
- Less boilerplate and safer refactors across modules

## 🔗 Where it fits in the platform
- Used by Api, Data, and Engine service layers
- Built atop Api‑Models; consumed by Api‑Services

## 🛠️ Technology at a glance
- Frameworks: Spring Data JPA, JPA (provided)
- Build: Maven

## 📝 Conventions
- Keep repository methods explicit and purposeful
- Prefer derived query methods first; custom queries when necessary

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
