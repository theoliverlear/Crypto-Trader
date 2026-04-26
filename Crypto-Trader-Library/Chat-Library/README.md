# Crypto Trader — Chat Library
## Shared contracts for AI‑driven chat interfaces

---

Chat‑Library aggregates the shared pieces used by the Crypto Trader Chat
module. It centralizes communication protocols, components, configuration,
event contracts, domain models, repositories, and services so the chat
ecosystem evolves consistently with the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Chat‑Communication: message handling and transport contracts.
  - Chat‑Components: reusable chat UI building blocks.
  - Chat‑Config: settings for AI models, prompts, and usage.
  - Chat‑Events: event payloads for chat interactions.
  - Chat‑Models: domain entities for conversations and messages.
  - Chat‑Repositories: data access for chat persistence.
  - Chat‑Services: business logic for chat operations.

## ✅ Why it matters
- Keeps chat‑related types in one place, reducing drift across services.
- Enables consistent evolution of the chat feature set.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Chat and backend services.
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
