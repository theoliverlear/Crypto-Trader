# Crypto Trader — Version Module
## Versioning and release intelligence for the platform

---

Crypto‑Trader‑Version centralizes version metadata and release operations for
the Crypto Trader ecosystem. It parses POMs, understands module coordinates 
and relationships, and helps produce clean, accurate release notes so the
platform ships with confidence and traceability.

Important: Past results and backtests do not guarantee future performance. 
Always start in paper mode. Liability is your own.

### What this module is
- The source of truth for version semantics and release metadata across 
  modules.
- A toolkit for parsing and reasoning about Maven projects (parents, modules,
  dependencies).
- A helper for generating human‑readable release notes and change logs.

## ⭐️ What it does
- POM parsing and analysis
  - Reads Maven descriptors to extract artifact coordinates, parents, modules,
    and dependency graphs.
- Version modeling
  - Uses shared `version‑models` to represent versions, components, and 
    release artifacts in a typed way.
- Change and release notes
  - Aggregates changes into clear, user‑facing notes suitable for Docs and 
    GitHub releases.
- Multi‑format support
  - Understands common metadata formats (YAML/TOML/JSON) used in auxiliary 
    config files.
- Automation‑friendly
  - Built to slot into CI workflows that validate, tag, and publish versions
    consistently.

## ✅ Why it matters
- Consistent releases: the entire platform advances in lockstep with clear 
  semantics.
- Traceability: every artifact and change is explained, categorized, and easy 
  to audit.
- Lower risk: automated checks reduce human error in version bumps and notes.
- Documentation synergy: release notes flow cleanly into the Docs site.

## 🔗 Where it fits in the platform
- Works with `Version‑Library` (e.g., `version‑models`) to standardize version 
  metadata.
- Supports CI pipelines for tagging, packaging, and publishing across modules.
- Feeds the documentation site and changelogs with structured release details.

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Libraries: Picocli (CLI plumbing), Jackson (JSON), JDOM2 (XML/POM), TOML4J
  (TOML), SnakeYAML (YAML), SLF4J
- Build & tooling: Maven, Dokka

## 📝 Conventions
- Follow Semantic Versioning (SemVer) for public artifacts.
- Keep parent and module versions aligned where intended; document intentional
  divergences.
- Prefer additive changes; call out breaking changes explicitly in release 
  notes.
- Use clear, categorized commit messages to improve automated note generation.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
