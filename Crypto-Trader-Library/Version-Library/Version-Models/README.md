# Crypto Trader — Version-Models
## Typed models for versions, releases, and configuration parsing

---

Version‑Models provides typed representations of versions, releases, and
artifact metadata used by Crypto Trader tooling and documentation. It also
includes helpers for parsing configuration formats used in the ecosystem.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Models for versioning and release notes
- Parsers/utilities for POM, TOML, JSON, and YAML
- Supports CLI tools and Docs pipelines

## ✅ Why it matters
- Single source of truth for version metadata across tools
- Easier to automate releases and documentation generation

## 🔗 Where it fits in the platform
- Used by the Docs site and CI/CD scripts
- Part of Version‑Library under the shared Library umbrella

## 🛠️ Technology at a glance
- Language: Java
- Libraries: Jackson, JDOM2, TOML4J, SnakeYAML, Picocli
- Build: Maven

## 📝 Conventions
- Keep models explicit and documented; prefer additive changes
- Validate inputs and handle parser errors gracefully

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
