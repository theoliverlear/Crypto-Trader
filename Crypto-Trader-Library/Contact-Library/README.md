# Crypto Trader — Contact Library
## Shared contracts for notifications (models, repos, events)

---

Contact‑Library aggregates the pieces used by the Contact module to manage
notifications: recipient/models, repositories, and lifecycle events. It
provides a consistent surface for services that produce or consume messages.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Contact‑Models, Contact‑Repositories, Contact‑Events.
- Provides shared types
  - One place for notification entities and lifecycle messaging.

## ✅ Why it matters
- Reduces duplication in services that need to send/track messages.
- Makes notification workflows easier to test and evolve.

## 🔗 Where it fits in the platform
- Used by Api, Admin, and background services that notify users.
- Lives under the broader Library umbrella.

## 🛠️ Technology at a glance
- Language: Kotlin/Java
- Frameworks: Spring Data JPA (repos)
- Build: Maven

## 📝 Conventions
- Keep message metadata minimal but sufficient for auditing.
- Avoid storing secrets; use provider tokens via secure configuration.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
