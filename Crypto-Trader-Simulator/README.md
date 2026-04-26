# Crypto Trader — Simulator Module
## Strategy backtesting and paper‑trading simulator

---

Crypto‑Trader‑Simulator replays historical market data and executes trading
strategies in a safe, sandboxed environment. It lets developers and traders
validate algorithms before risking real capital.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Historical replay
  - Feed recorded market data through strategies to measure performance.
- Paper trading
  - Simulate live execution with virtual balances and realistic fills.
- Strategy validation
  - Compare expected vs. actual outcomes to catch logic errors early.

## ✅ Why it matters
- Reduces financial risk by catching strategy bugs before live deployment.
- Provides reproducible, data‑driven feedback loops for algorithm tuning.

## 🔗 Where it fits in the platform
- Consumes market data from Crypto‑Trader‑Data.
- Shares strategy interfaces with Crypto‑Trader‑Engine.
- Results feed into the Admin and Analysis dashboards.

## 🛠️ Technology at a glance
- Language: Kotlin
- Build: Maven
- Dependencies: Spring Boot, shared Library modules

## 📝 Conventions
- Always seed simulations for reproducibility.
- Keep simulation logic stateless where possible for easy parallelization.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
