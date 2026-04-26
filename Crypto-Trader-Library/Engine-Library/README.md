# Crypto Trader — Engine Library
## Shared contracts for the trading engine (algorithms and services)

---

Engine‑Library aggregates the shared pieces used by the Crypto Trader Engine
module. It centralizes the service contracts that power the core trading
algorithms and strategy execution logic.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Engine‑Services: core trading logic, strategy execution, and portfolio management.
- Keeps trading semantics consistent
  - One place for types that the Engine module and backend share.

## ✅ Why it matters
- Reduces drift between trading services by sharing stable types.
- Makes engine workflows easier to reason about and test.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Engine and services that execute trades.
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
