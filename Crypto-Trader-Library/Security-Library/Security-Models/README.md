# Crypto Trader — Security-Models
## Shared entities and value types for security concerns

---

Security‑Models defines the typed entities and value objects used for security
across the platform. These models include IP allow/deny sets, rule definitions,
user/session‑related records, and other primitives services use to reason about
risk and access.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Domain models
  - IP address sets (CIDR/range aware), ban/allow lists, rule snapshots, and
    related security state.
- Serialization‑ready types
  - Friendly to JSON and messaging while keeping a clear internal API.
- Foundation for repositories/services
  - Powers Security‑Repositories and Security‑Services implementations.

## ✅ Why it matters
- One source of truth for security semantics and invariants.
- Safer refactors by sharing strongly‑typed models across modules.

## 🔗 Where it fits in the platform
- Consumed by API/Security services and stored via Security‑Repositories.
- Referenced in Security‑Events to keep payloads consistent.

## 🛠️ Technology at a glance
- Language: Kotlin and Java (as needed)
- Libraries: ipaddress (CIDR math), Jackson annotations (where applicable)
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Prefer explicit naming and documented units/timezones.
- Keep models minimal and focused; add behaviors in services, not entities.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
