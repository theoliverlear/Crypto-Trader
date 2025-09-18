# Crypto Trader — Security Module
## Centralized security services, filters, and guardrails for the platform

---

Crypto‑Trader‑Security provides the platform’s common security surface: base
security configuration, request filters, threat‑signal plumbing, and helper
services. It standardizes how applications in the stack apply Spring Security,
identify hostile traffic (honeypots, IP sets), and publish security events so
defense‑in‑depth stays consistent everywhere.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Security baseline for services
  - Spring Security auto‑config and conventions (headers, CORS, CSRF posture by
    profile), consistent SecurityFilterChain wiring.
- Request safeguards and signals
  - Honeypot endpoints/paths, IP allow/deny set evaluation, JWT↔session glue,
    and threat logging for follow‑up.
- Security events & metrics
  - Emits structured events to the platform event bus (Kafka) and integrates
    with Actuator where available.
- Shared configuration
  - Centralizes security properties and profiles and builds on Security‑Library
    submodules.

## ✅ Why it matters
- Consistency: one place to evolve security decisions and defaults.
- Visibility: standard events and logs make incidents easier to investigate.
- Defense in depth: lightweight filters and checks catch bad traffic early.

## 🔗 Where it fits in the platform
- Used alongside Api/Data/Engine services to enforce common guardrails.
- Depends on API shared libraries and the Security‑Library submodules.
- Publishes security events that other modules can consume.

## 🛠️ Technology at a glance
- Language: Kotlin
- Frameworks: Spring Boot (Web, Security), Spring Cloud Stream (Kafka)
- Shared libs: Api‑Library, Security‑Library (Config, Events, Models, etc.)
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Prefer least‑privilege; never log secrets or token material.
- Keep defaults safe; relax only via explicit configuration and profiles.
- Treat honeypot hits and repeated violations as high‑signal events.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
