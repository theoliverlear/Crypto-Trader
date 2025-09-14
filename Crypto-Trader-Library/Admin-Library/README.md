# Crypto Trader — Admin Library
## Shared contracts for the operator console (models and events)

---

Admin‑Library aggregates the shared pieces used by the Crypto Trader Admin
console. It centralizes the models and event contracts that power the JavaFX
operations UI so Admin can evolve consistently with the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Admin‑Models: users, roles, jobs, configuration, and audit entities.
  - Admin‑Events: user actions, system operations, and audit‑related messages.
- Keeps operator semantics consistent
  - One place for types that the Admin console and backend share.

## ✅ Why it matters
- Reduces drift between UI and backend by sharing stable types.
- Makes admin workflows easier to reason about and test.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Admin and backend services that emit/consume admin events.
- Built as part of the broader shared Library umbrella.

## 🛠️ Technology at a glance
- Language: Kotlin and Java (where applicable)
- Build: Maven multi‑module
- Docs: Dokka

## 📝 Conventions
- Evolve contracts additively and maintain backward compatibility.
- Keep models minimal and well‑documented.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
