# Crypto Trader — Api-Communication
## DTOs and communication types for API requests and responses

---

Api‑Communication defines request/response DTOs and supporting utilities used
by Crypto Trader APIs and clients. These types keep serialization stable and
explicit across services and UIs.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Defines DTOs
  - Clear, versionable payloads for REST/WebSocket/service calls.
- Serialization support
  - Jackson annotations where needed to ensure stable JSON contracts.

## ✅ Why it matters
- A single source of truth for payload shapes reduces integration bugs.
- Enables additive evolution of APIs without breaking consumers.

## 🔗 Where it fits in the platform
- Used by Api, Data, Engine, Admin, and external clients.
- Part of Api‑Library; consumed widely across services.

## 🛠️ Technology at a glance
- Language: Java/Kotlin
- Dependencies: Jackson annotations (provided)
- Build: Maven

## 📝 Conventions
- Keep DTOs small and explicit; document units/timezones.
- Use additive changes and deprecations for safer evolution.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
