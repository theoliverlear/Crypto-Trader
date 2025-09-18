# Crypto Trader — Security-Infrastructure
## Web filters and infrastructure for enforcing security policies

---

Security‑Infrastructure contains reusable web infrastructure that applies the
platform’s security policies at the HTTP boundary. It includes servlet filters,
interceptors, and helpers that detect hostile behavior (honeypots, repeated
failures, bad token flows) and surface signals for downstream handling.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Edge protections
  - Honeypot path detection, IP allow/deny evaluation, and early request
    rejection with lightweight logging.
- Session/JWT glue
  - Helpers and filters to bridge JWT claims into session context where
    applicable and keep session semantics consistent.
- Integration hooks
  - Works with Spring Security’s filter chain; emits security events when
    suspicious activity is observed.

## ✅ Why it matters
- Stops obvious bad traffic early and cheaply.
- Standardizes edge logic so services don’t re‑implement risky code.

## 🔗 Where it fits in the platform
- Used by API/Security services near the HTTP boundary.
- Complements Security‑Config (filter chain wiring) and Security‑Services
  (threat evaluation/policy).

## 🛠️ Technology at a glance
- Language: Kotlin (and Java where needed)
- Frameworks: Spring Boot (Web, Security)
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Keep edge logic fast and side‑effect free where possible.
- Do not log secrets; redact token material.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
