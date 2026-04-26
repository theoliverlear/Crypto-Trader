# Crypto Trader — Logging Module
## Structured observability and consistent logs across the platform

---

Crypto‑Trader‑Logging provides a single, consistent approach to logging for
every service and tool in the Crypto Trader ecosystem. It standardizes
formats, enriches events with useful context (correlation IDs, user/session
hints, component names), and supplies helpers so teams get actionable signals
without noise.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

### What this module is
- A centralized logging toolkit used by first‑party modules (Api, Data,
  Engine, Admin, etc.).
- Conventions and utilities for structured, readable logs and health signals.
- A foundation for observability that plays well with Actuator and downstream
  collectors.

## ⭐️ What it does
- Consistent formatting and categories
  - Shared patterns and logger names keep logs scannable across services.
- Context propagation
  - Encourages use of MDC/correlation IDs to tie related events together
    across boundaries.
- Operational signals
  - Clear INFO for lifecycle events; WARN for recoverable degradation; ERROR
    for actionable faults.
- Integration‑ready
  - Designed to work alongside Spring Boot Actuator, HTTP clients, persistence
    layers, and WebSocket flows.
- Test‑friendly
  - Publishes a `test-jar` so tests can assert on log output and behaviors.

## ✅ Why it matters
- Faster triage: consistent structure reduces guesswork during incidents.
- Useful by default: context on each line improves traceability.
- Lower noise: shared severity conventions focus attention where it matters.
- Scales with the platform: the same patterns apply across all modules.

## 🚀 Support-Ready Infrastructure
When things go wrong, every second counts. Our logging module isn't just for developers—it's built to empower our support team to help you faster.
- **Instant Visibility**: Front-end errors are captured in real-time and piped directly to our admin dashboards.
- **Deep Technical Context**: We automatically collect stack traces, browser metadata, and client-side state so our team can see exactly what you saw.
- **Zero Guesswork**: Skip the "can you send us a screenshot?" or "what steps did you take?" conversation. Our support team can pinpoint the root cause before you even finish describing the issue.
- **Faster Fixes**: By bridging the gap between your browser and our backend, we turn days of troubleshooting into minutes of resolution.

## 🔗 Where it fits in the platform
- Included by services to unify logging: Api, Data, Engine, Admin, and
  internal tools.
- Complements platform monitoring via Actuator endpoints and external log
  shipping (if configured by operators).

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Frameworks: Spring Boot (Web, Actuator)
- Logging backend: Logback (classic)
- Build & tooling: Maven, Dokka

## 📝 Conventions
- Prefer structured, single‑line messages with clear keys for dynamic values
  (e.g., `currency=BTC, price=...`).
- Always include a correlation/request ID at boundaries; prefer MDC for
  propagation.
- Reserve ERROR for actionable incidents; use WARN for transient or degraded
  states; INFO for lifecycle.
- Avoid logging secrets or PII; prefer redaction when needed.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
