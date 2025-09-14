# Crypto Trader — Coverage Module
## Aggregated test coverage across all modules

---

Crypto‑Trader‑Coverage collects and publishes a single, consolidated JaCoCo
report for the entire platform. It aggregates coverage from core apps and
libraries so you can track trends and identify gaps at a glance.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates module reports
  - Merges JaCoCo execution data for apps and libraries into one HTML/XML site.
- Produces human‑readable output
  - Generates an aggregate HTML report for browsing by package/class.
- Supports CI visibility
  - XML output can be fed into code‑quality dashboards and PR checks.

## ✅ Why it matters
- One place to understand test completeness across 30+ subprojects.
- Encourages healthy testing practices by making gaps obvious.
- Helps prevent regressions by tracking coverage trends over time.

## 🔗 Where it fits in the platform
- Depends on many submodules (Api, Data, Engine, Admin, Libraries, etc.).
- Runs at build time (Maven verify) to produce a unified report.
- The generated site is typically published under the Docs/Coverage section.

## 🛠️ Technology at a glance
- Build & reports: Maven, JaCoCo report‑aggregate goal
- Languages covered: Java and Kotlin sources across modules

## 📝 Conventions
- Keep module list in the POM current so aggregation stays accurate.
- Treat coverage as a guide — quality of tests matters more than raw %.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
