# Crypto Trader — Agent Library
## Shared contracts for autonomous agents (communication, components, and models)

---

Agent‑Library aggregates the shared pieces used by the Crypto Trader Agent
module. It centralizes communication protocols, reusable components, configuration,
infrastructure, and domain models so the agent ecosystem evolves consistently
with the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Agent‑Communication: message formats and interaction patterns.
  - Agent‑Components: reusable building blocks for automated tasks.
  - Agent‑Config: agent‑specific settings and operational parameters.
  - Agent‑Infrastructure: core runtime services and utilities.
  - Agent‑Models: data structures for agent state and task history.
- Keeps agent semantics consistent
  - One place for types that the Agent module and backend share.

## ✅ Why it matters
- Reduces drift between agent services by sharing stable types.
- Makes agent workflows easier to reason about and test.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Agent and services that interact with agents.
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
