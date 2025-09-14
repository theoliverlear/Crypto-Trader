# Crypto Trader — Api-Services
## Business logic and service layer for APIs

---

Api‑Services implements the core business logic used by Crypto Trader APIs and
microservices. It orchestrates repositories, components, and communication
layers to provide cohesive operations behind HTTP and messaging endpoints.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Encodes domain rules and invariants
- Coordinates repositories, components, and events
- Exposes use cases consumed by controllers and listeners

## ✅ Why it matters
- Keeps controllers thin and focused on transport concerns
- Central place to test business behavior thoroughly

## 🔗 Where it fits in the platform
- Used by Api, Data, and Engine endpoints and listeners
- Built atop Api‑Models/Repositories/Components/Communication/Events

## 🛠️ Technology at a glance
- Framework: Spring Boot
- Testing: JUnit Jupiter (via platform testing utilities)
- Build: Maven

## 📝 Conventions
- Keep side effects explicit; document pre/post conditions
- Favor constructor injection and pure functions where possible

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
