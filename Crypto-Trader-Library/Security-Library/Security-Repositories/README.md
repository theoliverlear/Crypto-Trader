# Crypto Trader — Security-Repositories
## Persistence layer for security domain models

---

Security‑Repositories contains Spring Data repositories for the security domain
(models such as IP allow/deny sets, ban lists, and audit records). It provides a
clean, testable persistence surface that other services can depend on without
re‑implementing queries.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Repository interfaces
  - Spring Data JPA repositories for Security‑Models.
- Query helpers
  - Focused finder methods to support common security workflows.
- Integration ready
  - Designed to plug into Spring Boot services with minimal configuration.

## ✅ Why it matters
- Centralizes persistence concerns for the security domain.
- Reduces duplication and keeps data access consistent and auditable.

## 🔗 Where it fits in the platform
- Used by Security‑Services and application code in API/Security modules.
- Backed by PostgreSQL in production; supports H2 for development/testing.

## 🛠️ Technology at a glance
- Language: Kotlin
- Frameworks: Spring Data JPA, Spring Boot
- Databases: PostgreSQL (runtime), H2 (dev/test)
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Keep repository interfaces small and intention‑revealing.
- Prefer derived queries and explicit method names over ambiguous custom SQL.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
