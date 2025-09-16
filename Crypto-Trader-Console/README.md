# Crypto Trader — Console Module
## Command-line power tools for advanced users

---

Crypto‑Trader‑Console is a command‑driven interface for power users who prefer
keyboard speed over graphical menus. It opens an interactive console where you
can type commands to trigger actions across the Crypto Trader platform — from
quick lookups and diagnostics to starting, pausing, or inspecting trading
workflows.

Status: Planned. The module skeleton exists and the experience is being
designed. This README describes the intended behavior and conventions so early
adopters and contributors know what to expect.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

## ⭐️ What it will do
- Interactive command console
  - Launch an interactive REPL where you can run short, memorable commands.
- Fast actions and shortcuts
  - Trigger common operations (pause/resume trading, inspect positions, view
    latest signals) with minimal typing.
- Scriptable automation
  - Chain commands or run a command file for repeatable tasks.
- Platform introspection
  - Query engine status, recent orders, risk limits, health checks, and logs.
- Safe controls
  - Respect global guardrails, role‑based permissions, and dry‑run/paper modes.

## ✅ Why it matters
- Speed: type a command, get an answer, move on.
- Focus: stay in one pane of glass while operating or debugging.
- Repeatability: script reliable operational sequences without a GUI.
- Complements other UIs: pairs with Admin (operators) and Website (users).

## 🧑‍💻 Who it’s for
- Advanced users who prefer keyboard‑driven workflows.
- Operators who need quick toggles and inspections during incidents.
- Developers and quants who script routine diagnostics.

## 🚀 How it will work (at a glance)
1. Start the console from your terminal (see Run section below).
2. You’ll see an interactive prompt with command suggestions and help.
3. Type commands like `status`, `positions`, `pause trading`, `resume trading`,
   `signals BTC-USD`, or `logs --tail 200`.
4. Commands call into platform services (Engine, Api, Data) via HTTP/DB
   adapters and return structured output.
5. You can exit at any time with `exit` or `quit`.

Example (planned) commands:
- status — overview of trading loop, mode (paper/live), and last cycle
- positions [--open] — list open/closed positions with PnL
- pause trading | resume trading — controlled toggles with audit output
- signals <SYMBOL> [--window 24h] — latest model/price signals
- orders [--recent 50] — recent orders and fills
- logs [--tail 200] [--level warn] — quick diagnostics
- config get <KEY> | config set <KEY>=<VALUE> — view/update safe properties

Note: Exact command names and flags may evolve as the implementation lands.

## 🔗 Where it fits in the platform
- Talks to Crypto‑Trader‑Engine for execution state and controls under policy.
- Reads signals and data surfaced by Analysis/Data modules (read‑only by
  default).
- Complements Admin (desktop) and Website (web) for different user personas.

## 🔒 Safety, privacy, and control
- Starts in paper/safe mode by default; sensitive actions require explicit
  confirmation.
- Enforces guardrails: position limits, exposure caps, and kill switches remain
  authoritative.
- AuthN/Z: console commands will require authenticated access matching user
  roles.
- Never store exchange API keys in plaintext; use the platform’s secret
  management.

## 🛠️ Technology at a glance (planned)
- Language & runtime: Java 23
- Frameworks: Spring Boot (CLI) and/or PicoCLI for command parsing (to be
  finalized)
- Integrations: Spring Web/HTTP clients for platform APIs; PostgreSQL driver
  for read‑only diagnostics where appropriate
- Build & tooling: Maven, JUnit Jupiter, Dokka

## 📝 Conventions
- Favor short, discoverable commands with clear, consistent flags.
- Provide `--help` for every command and friendly error messages.
- Make dangerous actions explicit: confirmations and `--force` when necessary.
- Default to read‑only queries; require roles for mutative operations.

## ▶️ Run (when available)
- Dev: `mvn -q -pl Crypto-Trader-Console -am -DskipTests spring-boot:run`
- Jar: `java -jar target/crypto-trader-console.jar`
- Help: `help` inside the console or `--help` for any command

Note: Until implementation lands, these commands are placeholders.

## 🤝 Contributing
Have ideas for commands or ergonomics? Open an issue or PR. Please keep
security, guardrails, and clarity front‑and‑center.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
