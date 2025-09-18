# Crypto Trader — Security-Services
## Core services for threat evaluation and security workflows

---

Security‑Services contains the core business logic that turns raw signals
(honeypot hits, suspicious requests, repeated failures) into actions and
policies. It complements the Config/Infrastructure/Repositories modules with
concrete services like threat evaluation, IP set management, and policy helpers.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Threat evaluation
  - Services for interpreting honeypot hits, request patterns, and other inputs
    to produce decisions (e.g., mark, rate‑limit, ban, notify).
- Policy helpers
  - Utilities for managing IP allow/deny sets and enforcing rules consistently.
- Integration ready
  - Emits Security‑Events and uses Security‑Repositories/Models to persist and
    share state across services.

## ✅ Why it matters
- Encapsulates security decisions in one place for easier testing and change
  control.
- Promotes consistent behavior across Api, Security app, and other modules.

## 🔗 Where it fits in the platform
- Called by API/Security services from filters and controllers.
- Works with Security‑Infrastructure at the edge and Security‑Repositories for
  persistence.

## 🛠️ Technology at a glance
- Language: Kotlin
- Frameworks: Spring Boot (as needed by services)
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Keep logic deterministic and well‑tested; avoid side effects where possible.
- Prefer explicit outcomes and event emission over silent failures.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
