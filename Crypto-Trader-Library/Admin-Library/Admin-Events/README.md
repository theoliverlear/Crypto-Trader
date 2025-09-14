# Crypto Trader — Admin-Events
## Event contracts for the operator console

---

Admin‑Events defines the event payloads emitted and consumed by the Crypto
Trader Admin console and related services. These messages capture user actions,
system operations, and audit signals so operator activity is observable and
traceable.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Event contracts
  - Typed messages for actions and system events in the Admin domain.
- Audit‑friendly payloads
  - Structures designed to support auditing and incident response.
- Integration points
  - Works alongside API event streams (e.g., Kafka) and Admin‑Models.

## ✅ Why it matters
- Makes operator actions transparent and reviewable.
- Encourages clear, well‑typed communication between Admin and services.

## 🔗 Where it fits in the platform
- Used by Crypto‑Trader‑Admin and services that react to operator events.
- Complements Api‑Events for cross‑service messaging.

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven, Dokka docs

## 📝 Conventions
- Keep events stable and backward compatible where possible.
- Include enough context to support auditing without overexposing sensitive data.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
