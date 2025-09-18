# Crypto Trader — Security Library
## Shared security contracts, configuration, and infrastructure for the platform

---

Security‑Library collects the reusable building blocks that make security
consistent across Crypto Trader services. It provides configuration, models,
repositories, events, services, and web infrastructure so every module applies
security the same way.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates security submodules
  - Security‑Config — Spring Security baselines, properties, profiles, and
    helper configuration (including YAML property loading and example honeypot
    paths).
  - Security‑Events — Structured messages for security‑relevant activity
    (e.g., login success/failure, honeypot hits, IP ban/unban).
  - Security‑Models — Typed entities and value objects (IP sets, rules, tokens,
    audit records) shared by services.
  - Security‑Repositories — Spring Data repositories for the security domain
    models (e.g., IP sets/ban lists, audit records).
  - Security‑Services — Core services and helpers for threat evaluation and
    policy decisions.
  - Security‑Infrastructure — Filters and web infrastructure used by APIs
    (honeypot detection, IP evaluation, headers/converters, etc.).

## ✅ Why it matters
- One source of truth for security types and behaviors.
- Fewer foot‑guns: safe, repeatable defaults across services.
- Faster iteration: evolve security in one place and adopt everywhere.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Api, Security (app), Data, Engine, and others.
- Feeds the Docs pipeline (Dokka) for browsable API references.

## 🛠️ Technology at a glance
- Language: Kotlin (with some Java where appropriate)
- Frameworks: Spring Security, Spring Boot (where relevant per submodule)
- Build & docs: Maven multi‑module, JUnit Jupiter, Dokka

## 📝 Conventions
- Favor additive changes to shared contracts.
- Never log credentials or secrets; keep tokens ephemeral and redacted.
- Keep defaults strict; allow opt‑in relaxations via configuration.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
