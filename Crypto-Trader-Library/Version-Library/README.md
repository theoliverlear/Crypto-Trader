# Crypto Trader — Version-Library
## Parent library for versioning tools and models

---

Version‑Library aggregates models and utilities related to version management
across the Crypto Trader project. It provides a stable surface for parsing and
emitting version information used by tooling and docs.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates Version‑Models and related utilities
- Supports CLI/tools and Docs pipelines that read version metadata

## ✅ Why it matters
- Single place to evolve version representations and parsing rules
- Reduces duplication in build and documentation tooling

## 🔗 Where it fits in the platform
- Used by Docs, CI/CD tooling, and release scripts
- Part of the shared Library umbrella

## 🛠️ Technology at a glance
- Language: Java
- Libraries: Jackson, Picocli, TOML/YAML parsers (via Version‑Models)
- Build: Maven

## 📝 Conventions
- Keep model fields explicit and documented
- Prefer additive changes; maintain backward compatibility for parsers

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
