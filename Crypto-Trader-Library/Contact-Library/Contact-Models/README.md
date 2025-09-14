# Crypto Trader — Contact-Models
## Domain models for notifications

---

Contact‑Models defines the entities used by the notification system: recipients,
templates, delivery state, provider metadata, and related configuration. These
models are shared by services that send or track messages.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Recipient, template, and delivery state models
- Provider metadata and configuration surfaces
- Serialization for APIs and storage bindings

## ✅ Why it matters
- Single source of truth for notification data shapes
- Enables consistent persistence and inter‑service communication

## 🔗 Where it fits in the platform
- Consumed by Contact‑Repositories and sending services
- Part of Contact‑Library under the shared Library umbrella

## 🛠️ Technology at a glance
- Language: Kotlin/Java
- Persistence: JPA/Hibernate (where applicable)
- Build: Maven

## 📝 Conventions
- Document units/timezones and redaction of sensitive fields
- Prefer additive changes to maintain compatibility

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
