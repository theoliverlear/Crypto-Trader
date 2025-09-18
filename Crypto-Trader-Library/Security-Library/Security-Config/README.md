# Crypto Trader — Security-Config
## Spring Security baselines, properties, and helper configuration

---

Security‑Config centralizes common Spring Security setup used across the
platform. It defines conventional beans and properties, provides profile‑aware
settings, and includes helpers like a YAML property source factory and example
honeypot paths so services can adopt safe defaults quickly.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Baseline Spring Security configuration
  - Establishes headers, CORS/CSRF posture by profile, and a conventional
    `SecurityFilterChain` layout used by services.
- Property binding and profiles
  - Strongly‑typed security properties; supports profile‑specific overrides
    (e.g., `application-secure.yml`).
- YAML and externalized config helpers
  - `YamlPropertySourceFactory` for loading supplemental YAML files; includes
    environment variables for quick opt‑in to honeypot endpoints.

## ✅ Why it matters
- Consistent, safe defaults across all services.
- Less boilerplate in apps; fewer mistakes when configuring security.

## 🔗 Where it fits in the platform
- Consumed by Crypto‑Trader‑Api, Security app, and other Spring services.
- Backed by Security‑Models/Repositories/Services where deeper behavior is needed.

## 🛠️ Technology at a glance
- Language: Kotlin
- Frameworks: Spring Boot (Security, Web)
- Build & docs: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Keep defaults strict; relax via explicit profiles or properties.
- Do not log secrets or long‑lived tokens; use redaction.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
