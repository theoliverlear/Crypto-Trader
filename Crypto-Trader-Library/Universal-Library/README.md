# Crypto Trader — Universal Library
## Shared components and configurations for module environments

---

Universal‑Library aggregates platform‑agnostic building blocks used across
every Crypto Trader module. It centralizes shared components, configuration,
extension utilities, and common models so cross‑cutting concerns evolve
consistently with the rest of the platform.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Aggregates submodules
  - Universal‑Components: module‑agnostic reusable components.
  - Universal‑Config: shared settings and environment‑agnostic parameters.
  - Universal‑Extensions: platform‑agnostic tools and common integration points.
  - Universal‑Models: shared data structures and entities.

## ✅ Why it matters
- Eliminates duplication of truly cross‑cutting types and utilities.
- Provides a stable foundation that every module can depend on.

## 🔗 Where it fits in the platform
- A transitive dependency of most Library modules.
- Built as part of the broader shared Library umbrella.

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven multi‑module
- Docs: Dokka

## 📝 Conventions
- Keep this module minimal; only truly universal code belongs here.
- Evolve contracts additively and maintain backward compatibility.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
