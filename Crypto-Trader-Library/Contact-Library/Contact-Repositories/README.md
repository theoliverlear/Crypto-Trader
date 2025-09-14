# Crypto Trader — Contact-Repositories
## Persistence layer for notification entities

---

Contact‑Repositories provides Spring Data repository interfaces for the contact
and notification domain. It standardizes database access for recipients,
messages, templates, and delivery states.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Repository interfaces for contact/notification entities
- Common query patterns and transactions
- Integrates with JPA and Spring Data

## ✅ Why it matters
- Consistent persistence access across services
- Less duplication and safer schema evolution

## 🔗 Where it fits in the platform
- Consumed by services that send or track notifications
- Built atop Contact‑Models and Spring Data JPA

## 🛠️ Technology at a glance
- Frameworks: Spring Data JPA, JPA (provided)
- Build: Maven

## 📝 Conventions
- Keep repository APIs explicit and purposeful
- Prefer derived query methods; custom queries sparingly

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
