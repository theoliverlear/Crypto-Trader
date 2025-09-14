# Crypto Trader — Admin-Models
## Domain models for the operator console

---

Admin‑Models contains the typed entities used by the Crypto Trader Admin
console: users, roles, jobs, configuration, audit records, and more. These
models are shared between the desktop UI and backend services to ensure
consistency.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Core entities for Admin
  - Users, roles/permissions, jobs/tasks, configuration, and audit trail.
- Shared contracts
  - One schema of truth for serialization, persistence, and UI binding.

## ✅ Why it matters
- Reduces duplication and drift between UI and services.
- Strong typing prevents subtle regressions in operator workflows.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Admin and services that read/write admin state.
- Complements Admin‑Events for auditability.

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven

## 📝 Conventions
- Keep field names explicit; document timezones/units.
- Evolve models additively to maintain backward compatibility.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
