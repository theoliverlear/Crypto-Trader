# Crypto Trader — Library Module
## Shared contracts and building blocks that keep the platform consistent

---

Crypto‑Trader‑Library is the set of shared Java libraries that power
consistency across the platform. It defines common models, interfaces, 
communication types, and reusable UI components so every module (Api, Data,
Engine, Admin, Docs) can move faster without reinventing fundamentals.

Important: Past results and backtests do not guarantee future performance. 
Always start in paper mode. Liability is your own.

### What this module is
- An umbrella for the platform’s shared code: `Api‑Library`, `Desktop‑Library`,
  `Version‑Library`, and `Externals‑Library`.
- The stable backbone for domain models, service/repository interfaces, and 
  inter‑service communication types.
- A curated toolkit of reusable UI components (desktop) and version metadata 
  models.

## ⭐️ What it does
- Domain models and contracts
  - Typed models that represent currencies, portfolios, predictions, 
    snapshots, and more.
  - Service and repository interfaces that keep persistence and orchestration 
    consistent.
- Communication artifacts
  - Request/response DTOs and client interfaces for inter‑module communication.
  - Encourages additive evolution of APIs with backwards compatibility.
- Desktop components
  - Reusable JavaFX components and helpers used by desktop‑facing tools (e.g.,
    Admin).
- Version models
  - Typed representations of versions, releases, and notes used across CI/docs
    and tooling.
- External integrations surface
  - A place to normalize third‑party utility wrappers used across modules.

## ✅ Why it matters
- One source of truth: shared types and interfaces reduce drift between 
  modules.
- Safer changes: strong typing and shared contracts catch breaking changes
  early.
- Faster delivery: less boilerplate, more focus on value.
- Consistent UX: shared components produce familiar experiences across tools.

## 🔗 Where it fits in the platform
- Consumed by `Api`, `Data`, `Engine`, `Admin`, and other modules.
- Feeds the Docs pipeline (Dokka) to publish consistent reference material.
- Anchors communication and persistence semantics used across services.

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Build: Maven multi‑module aggregator
- Docs: Dokka HTML generation for reference
- Testing: JUnit Jupiter (where applicable in submodules)

## 📝 Conventions
- Evolve contracts additively; avoid breaking changes to shared types.
- Prefer explicit, well‑named fields; document timezones/units on temporal and
  numeric values.
- Keep UI components generic and reusable; avoid app‑specific logic here.
- No secrets or environment‑specific configuration in this module.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
