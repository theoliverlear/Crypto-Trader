# Crypto Trader — Console Library
## Shared contracts for CLI interfaces and terminal interactions

---

Console‑Library aggregates the shared pieces used by the Crypto Trader Console
module. It centralizes communication protocols, components, event contracts,
domain models, and services so the CLI ecosystem evolves consistently with
the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Console‑Communication: message handling and interaction patterns.
  - Console‑Components: reusable CLI interface elements.
  - Console‑Events: user input events and terminal triggers.
  - Console‑Models: terminal states, commands, and CLI structures.
  - Console‑Services: command execution and terminal management.

## ✅ Why it matters
- Keeps console‑related types in one place, reducing drift across services.
- Enables consistent evolution of the CLI feature set.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Console and backend services.
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
