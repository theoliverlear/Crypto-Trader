# Crypto Trader — API Module
## The engine behind live prices, smart trading, and market intelligence

---

Crypto‑Trader‑Api keeps your Crypto Trader experience fast, fresh, and reliable. It gathers real‑time prices, coordinates automated trading, tracks your portfolio, and brings in predictions and news sentiment so you can act with confidence.

Important: Past results and backtests do not guarantee future performance. Always start in paper mode. Liability is your own.

## ⭐️ What it does
- Live prices and market snapshots
  - Keeps prices up to date around the clock and takes periodic “snapshots” of the market for analysis and charts.
- Automated trading coordinator
  - Runs the trading loop for each portfolio, following the strategy you choose and the guardrails you set.
- Portfolio tracking and insights
  - Records history so you can see value changes, P&L, and trends over time.
- Price predictions (ML) integration
  - Connects to the Analysis service to fetch model predictions and to record training runs.
- News and sentiment signals
  - Accepts harvested news/sentiment so strategies can react to more than just price.
- Account media
  - Securely stores and serves profile pictures for signed‑in users.

## ✅ Why it matters
- Always‑on updates: the platform watches markets 24/7 so you don’t have to.
- Consistent execution: trading routines run on schedule, even during volatile moments.
- Clear picture: your portfolio value and history stay current and visible.
- Smarter signals: predictions and sentiment help filter noise.
- Built to scale: designed to handle many currencies and portfolios.

## 🚀 How it works at a glance
1. Collects the latest prices and saves clean market snapshots.
2. Orchestrates portfolio trading based on your chosen strategies and limits.
3. Records portfolio history to surface profit, drawdown, and value changes.
4. Talks to the Analysis module to train models and fetch price predictions.
5. Receives news/sentiment data from a harvester service to enrich signals.

## 🔗 Where it fits in the platform
- Powers the Crypto Trader web app with fresh data and actions.
- Works with the Analysis module for machine learning.
- Stores structured data in PostgreSQL.
- Runs background jobs to keep everything current.

## 🔒 Safety, privacy, and control
- Start in paper mode and set limits for position size, loss, and exposure.
- Account media and user actions are session‑protected.
- Keys and sensitive settings are secured in the wider platform.

## 🛠️ Technology at a glance
- Java + Spring Boot, PostgreSQL, and background scheduling.
- Built for reliability and maintainability.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See LICENSE.md in the repository root.
