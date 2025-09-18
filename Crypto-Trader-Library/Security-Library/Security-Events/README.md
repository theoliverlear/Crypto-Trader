# Crypto Trader — Security-Events
## Structured events for security‑relevant activity

---

Security‑Events defines the event payloads used to describe security activity
across the platform. These messages capture authentication outcomes, honeypot
hits, IP ban state changes, and other signals so services can react and
operators can audit behavior.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Event contracts
  - Typed payloads for login success/failure, suspicious requests, honeypot
    triggers, IP ban/unban, and related actions.
- Integration points
  - Designed for Kafka/Spring Cloud Stream usage; easy to serialize and evolve.
- Audit‑friendly
  - Minimal, privacy‑aware fields suitable for review and incident response.

## ✅ Why it matters
- Makes security posture observable across services.
- Encourages consistent, well‑typed communication for security concerns.

## 🔗 Where it fits in the platform
- Emitted by API/Security services and consumed by monitoring/ops jobs.
- Complements Security‑Models and Security‑Services for end‑to‑end flows.

## 🛠️ Technology at a glance
- Language: Kotlin
- Messaging: Spring Cloud Stream (Kafka), Jackson annotations where relevant
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Keep events stable and evolve additively.
- Avoid sensitive data; include only what’s necessary for response and audit.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
