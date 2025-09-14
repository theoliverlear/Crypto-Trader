# Crypto Trader — Api-Components
## Reusable Spring components for APIs (web, JDBC, HTTP clients)

---

Api‑Components provides reusable Spring‑based building blocks used across
Crypto Trader APIs: web configuration, HTTP clients, JDBC helpers, and other
infrastructure glue that promotes consistency and reduces boilerplate.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Web/Jackson configuration and utilities
- HTTP client helpers for service calls
- JDBC and data access support classes

## ✅ Why it matters
- Consistent infrastructure across services
- Less boilerplate and safer defaults for new modules

## 🔗 Where it fits in the platform
- Used by Api, Data, Engine, and Support services
- Part of Api‑Library under the Library umbrella

## 🛠️ Technology at a glance
- Framework: Spring Boot
- Extras: Apache HttpClient, Jackson
- Build: Maven

## 📝 Conventions
- Keep components generic and reusable; no service‑specific logic.
- Prefer composition over inheritance; document configuration properties.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
