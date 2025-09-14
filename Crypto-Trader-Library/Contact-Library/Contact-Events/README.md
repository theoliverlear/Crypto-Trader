# Crypto Trader — Contact-Events
## Notification lifecycle events (queued, sent, delivered, failed)

---

Contact‑Events defines event payloads that describe the lifecycle of
notifications across the platform. These messages let services observe and
react to delivery progress and failures.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Event contracts for queued/sent/delivered/failed
- Supports retries and audit trails
- Works with messaging infrastructure configured in Api‑Config

## ✅ Why it matters
- Improves reliability and visibility of notification flows
- Enables reactive behaviors (e.g., fallback channels on failure)

## 🔗 Where it fits in the platform
- Emitted by services that send messages; consumed by observers/metrics
- Part of Contact‑Library under the shared Library umbrella

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven

## 📝 Conventions
- Keep payloads minimal but sufficient for troubleshooting
- Avoid including PII; reference IDs and metadata securely

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
