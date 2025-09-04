# Crypto Trader — Engine Module
## The execution core that turns signals into live trades

---

Crypto‑Trader‑Engine is the platform’s main trading engine. It runs the trading
loop, sizes positions under strict guardrails, talks to exchanges, and 
executes entries/exits with discipline and speed.

"Crypto Trader is a platform that allows users to trade cryptocurrencies with
two approaches. One approach is the tried and true "buy low, sell high" 
method." Using this automated approach, Crypto Trader outperforms the market.
This is because buying low and selling high is a time-based endeavor. Since
computers can make mathematical decisions much faster than humans, our users 
are given an edge. The other approach is using Crypto Trader's deep learning 
models. With a GPU farm constantly updating the models, Crypto Trader can 
predict supported currencies’ price over time with high accuracy.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## 🧭 Plans and execution tiers (Engine benefits)

| Feature               | 🆓 Free                                                                     | 🧠 Pro                                                                      | 📰 Elite                                                                    |
|-----------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| Execution cadence     | Every `20s`                                                                 | Every `10s`                                                                 | Every `5s`                                                                  |
| Strategy modes        | Buy Low / Sell High                                                         | Algorithmic, ML, or Hybrid                                                  | Algorithmic, ML, or Hybrid                                                  |
| Signals used          | Price action                                                                | Price action + optional ML predictions from Analysis                        | Multi‑modal: price + ML + news/sentiment                                    |
| Risk controls         | Position sizing limits; stop‑loss / take‑profit; exposure caps; kill switch | Position sizing limits; stop‑loss / take‑profit; exposure caps; kill switch | Position sizing limits; stop‑loss / take‑profit; exposure caps; kill switch |
| Paper/live + pause    | Paper or Live; one‑click pause                                              | Paper or Live; one‑click pause                                              | Paper or Live; one‑click pause                                              |
| Scheduling & async    | Scheduled trading loop with `@EnableScheduling` + `@EnableAsync`            | Scheduled trading loop with `@EnableScheduling` + `@EnableAsync`            | Scheduled trading loop with `@EnableScheduling` + `@EnableAsync`            |
| Scale & observability | —                                                                           | Advanced risk tuning; throttled order placement; cool‑offs                  | Portfolio‑level guardrails; concurrency‑aware scheduling; health checks     |
| Ideal for             | New users validating in paper mode                                          | Active users seeking consistency with smarter signals                       | Power users running complex, multi‑signal strategies                        |

## ⭐️ What the Engine does
- Trading loop and scheduling
  - Runs 24/7 with `@EnableScheduling` and `@EnableAsync` to evaluate and act
    quickly.
- Position sizing and guardrails
  - Enforces max position size, stop‑loss/take‑profit, exposure caps, and kill
    switches.
- Exchange orchestration
  - Prepares, routes, and audits orders; integrates via HTTP/REST clients.
- Strategy integration
  - Executes classic buy‑low/sell‑high and consumes ML predictions from the 
    Analysis module.
- Paper and live modes
  - Start in paper; flip to live with the same guardrails when ready.

## ✅ Why it matters
- Time back: automation evaluates markets continuously, reacting within 
  milliseconds.
- Discipline by default: guardrails, throttles, and cool‑offs reduce emotional
  and impulsive trades.
- Built for volatility: consistent execution even when markets move fast.
- Proven architecture: decoupled data (Data module), analysis (Analysis), and 
  execution (Engine).

## 🚀 How it trades (at a glance)
1. Pulls latest market snapshots and signals (from Data/Analysis).
2. Evaluates strategy rules (price thresholds or ML signals) with risk checks.
3. Sizes positions based on confidence and configured limits.
4. Places orders via exchange adapter and tracks fills.
5. Records outcomes and schedules the next evaluation cycle.

Tip: You can disable the trading loop to run in a safe, docs/paper mode using 
the flag below.

## 🔗 Where it fits in the platform
- Consumes market data and predictions from the Data and Analysis modules.
- Drives the final step: turns signals into orders under strict risk 
  management.
- Surfaces health and docs endpoints (OpenAPI via Springdoc) for internal 
  observability.

## 🔒 Safety, privacy, and control
- Start in paper mode and set strict limits.
- Use kill switches: you can disable the scheduler (trading loop) at runtime
  with a single flag.
- Do not store exchange API keys in plaintext; manage secrets via platform 
  configuration.
- You are responsible for outcomes. Read and respect the LICENSE.

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Frameworks: Spring Boot (Web, Actuator), Springdoc OpenAPI
- Scheduling & async: `@EnableScheduling`, `@EnableAsync`
- Persistence/DB: PostgreSQL (primary), H2 (runtime/dev)
- HTTP/Integrations: Apache HttpClient
- Build & tooling: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Always validate in paper mode first; promote incrementally to live.
- Keep guardrails on: max position, stop‑loss, exposure caps, and kill 
  switches.
- Favor deterministic, replayable strategies; log decisions and outcomes.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
