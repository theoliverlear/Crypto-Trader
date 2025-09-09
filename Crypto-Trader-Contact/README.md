# Crypto Trader — Contact Module
## Outbound messaging and support communications service

---

Crypto‑Trader‑Contact is the platform’s communications service, responsible for
outbound user messaging and support interactions. It centralizes email delivery
(transactional and lifecycle), HTML template rendering, and future channels like
push or SMS behind a clean API. The service runs independently alongside other
modules, consumes domain events (e.g., `user-registered`, `portfolio-alert`) to
trigger messages, and integrates with providers such as Amazon WorkMail/SES for
reliable delivery. It standardizes branding and content via reusable templates,
enforces sending policies, and provides observability for message health and
delivery outcomes.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Message delivery API
  - A small, consistent API for sending transactional and lifecycle messages.
  - Abstracts providers so the app code doesn’t change when providers do.
- Event‑driven triggers
  - Consumes platform domain events like `user-registered`, `password-reset`,
    `portfolio-alert`, and `maintenance-notice`.
  - Routes events through policy and template engines to produce messages.
- Templates and branding
  - Reusable, branded HTML templates using server‑side rendering (Thymeleaf).
  - Shared partials for headers, footers, and styles ensure consistent look.
- Providers and deliverability
  - First‑class email via Amazon WorkMail/SES (or compatible SMTP).
  - Pluggable provider interfaces for future channels (push, SMS, chat).
- Safety and policy enforcement
  - Global and per‑user send limits, quiet hours, and opt‑out preferences.
  - Idempotency and deduplication for event fan‑out.
- Observability and health
  - Delivery status tracking, bounce/complaint handling, and metrics.
  - Actuator health, logs, and OpenAPI docs for operational visibility.

## ✅ Why it matters
- One place for all outbound communications and support messaging.
- Consistent brand and voice across all templates and channels.
- Safer messaging with enforceable policies and opt‑out compliance.
- Clear operational insight into delivery, failures, and complaints.

## 🔗 Where it fits in the platform
- Runs alongside Api, Data, Engine, and other services as an independent module.
- Listens to domain events published by platform services (e.g., user/account
  lifecycle events and portfolio alerts).
- Sends messages to users and operators; records delivery outcomes for audits.

### Common domain events
- `user-registered` → welcome email with onboarding steps.
- `password-reset` → secure reset link with expiry.
- `email-verify` → verification link for new or changed email.
- `portfolio-alert` → threshold/guardrail notifications to the user.
- `maintenance-notice` → scheduled downtime and status updates.

## 🔒 Safety, privacy, and control
- Respect user preferences (opt‑in/opt‑out) and local regulations.
- Never log secrets or PII; avoid storing message content beyond what’s needed
  for delivery status/audit.
- Use paper/test modes and sandbox credentials before production sending.
- Maintain idempotency when processing events to prevent duplicate sends.

## 🛠️ Technology at a glance
- Language & runtime: Java 23 (with Kotlin stdlib present) and Spring Boot
- Frameworks: Spring Boot (Web, Mail, Thymeleaf, Security, Data JPA, JDBC,
  WebSocket, Web Services), Actuator, Springdoc OpenAPI
- Persistence: PostgreSQL (primary), H2 (runtime/dev)
- Logging & tooling: Logback, Kotlin Logging, JUnit Jupiter, Maven, Dokka
- Testing aids: GreenMail for mail testing, Crypto‑Trader‑Testing toolkit

## 📝 Conventions
- Keep message contracts stable; evolve additively and version templates.
- Centralize styles and partials; avoid inline ad‑hoc HTML per message.
- Enforce opt‑out and sending policies in the service, not callers.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
