# Crypto Trader — Health Module
## Lightweight health monitoring, probes, and dependency checks

---

Crypto‑Trader‑Health provides simple utilities and models to run health checks
against Crypto Trader services and dependencies. It’s designed to back liveness
and readiness probes, expose basic uptime metrics, and help operators quickly
see whether critical services are reachable.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Service status checks
  - Ping HTTP endpoints and dependencies to determine up/down state.
- Liveness/readiness signaling
  - Build blocks for probes used by orchestrators and dashboards.
- Simple models
  - Typed representation of services and current/last‑seen status.

## ✅ Why it matters
- Quick signal for operators and CI to verify platform availability.
- Encourages clear separation between functionality and health concerns.
- Works across modules without pulling heavy frameworks.

## 🔗 Where it fits in the platform
- Can be called from Admin tools, Services, and CI checks.
- Complements Spring Boot Actuator health where used; does not require it.

## 🛠️ Technology at a glance
- Language: Kotlin
- Dependencies: Apache HttpClient, SLF4J, JUnit Jupiter (tests)
- Build: Maven

## 📝 Conventions
- Prefer fast, lightweight checks; avoid expensive network calls in frequent probes.
- Timebox external calls and handle failures gracefully.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
