# Crypto Trader — Mobile Module
## Native Android client built with Jetpack Compose

---

Crypto‑Trader‑Mobile is the native Android application for the Crypto Trader
platform. It provides a modern, responsive interface built with Jetpack Compose
so users can monitor portfolios, sign up, and interact with the platform from
their mobile devices.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- User authentication
  - Sign‑up and login flows backed by the Crypto Trader API.
- Portfolio dashboard
  - Real‑time overview of holdings and market data.
- Compose UI components
  - Reusable components such as hero stripes, module badge grids, and CTA buttons.

## ✅ Why it matters
- Extends Crypto Trader to mobile users with a first‑class native experience.
- Shares authentication and data contracts with the backend via Retrofit.

## 🔗 Where it fits in the platform
- Communicates with Crypto‑Trader‑Api over HTTP/REST.
- Consumes shared models through the API layer.

## 🛠️ Technology at a glance
- Language: Kotlin
- UI: Jetpack Compose, Material 3
- Networking: Retrofit, OkHttp
- DI: Hilt / Dagger
- Build: Gradle (Kotlin DSL)

## 📝 Conventions
- Follow Material 3 design guidelines for all UI components.
- Keep ViewModels lean; push business logic into repositories.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
