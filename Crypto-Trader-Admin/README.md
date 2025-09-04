# Crypto Trader — Admin Module
## Desktop portal for secure platform operations

---

Crypto‑Trader‑Admin is the desktop console used by project owners and operators
to oversee and control the Crypto Trader platform. It brings together 
operational dashboards and controls in one place so you can monitor data
harvesting, review logs, manage user access, view internal trends (including
signals from the Analysis module), and toggle critical system properties like
harvesting and trading kill‑switches.

Important: Past results and backtests do not guarantee future performance. 
Always start in paper mode. Liability is your own.

### The command center for your trading platform
- Move fast during incidents with one‑click pause/kill switches.
- See everything that matters: harvesting health, logs, user status, and 
  signals — all in one place.
- Designed for confidence: auditable actions, least‑privilege, and clear 
  guardrails.

> Built for operators who value clarity, control, and speed.

## ⭐️ What it does
- Operational oversight
  - Monitor data harvesting status, job health, and recent activity.
- Logs and diagnostics
  - Inspect recent error logs and warnings to triage issues quickly.
- User administration
  - Search users and terminate or disable access when required.
- Internal trends
  - View platform metrics and internal trends, including model predictions 
    received from Crypto‑Trader‑Analysis.
- Global controls
  - Flip major properties in a safe, auditable way: pause/cease harvesting, 
    pause/cease trading, broadcast alerts to users (maintenance, risk notices,
    etc.).

## ✅ Why it matters
- Single source of truth for platform operators.
- Fast response during incidents with one‑click kill‑switches.
- Clear visibility into data pipelines and strategy signals.
- Consistent, auditable administration workflows.

### Who it’s for
- Platform operators who need authority and speed without guesswork.
- Builders who want safe, auditable control over live systems.
- Analysts and leads who monitor ML signals and platform health.

## 🚀 How it works at a glance
1. Connects to the platform’s PostgreSQL database and services to fetch
   operational data.
2. Renders a JavaFX desktop UI with dashboards, tables, and dialogs for 
   administrative actions.
3. Invokes platform APIs and executes privileged operations behind secure
   operator authentication.

## 🔗 Where it fits in the platform
- Complements the web application by providing a dedicated operator console.
- Surfaces model predictions and training status from the Analysis module.
- Reads/writes operational state and history stored in PostgreSQL.
- Uses platform APIs for coordinated actions (alerts, user operations, 
  toggles).

## ⚡ Operator superpowers
- Instant authority: pause harvesting/trading with a single, audited action.
- Clarity at a glance: dashboards surface errors, backoffs, and trends so you
  act, not guess.
- Human‑first workflows: confirm dialogs, safe defaults, and reversible steps
  where possible.
- Built for scale: designed to handle many currencies, users, and concurrent
  jobs without losing signal.

## 🔒 Safety, privacy, and control
- Intended for trusted administrators only; access is restricted and 
  monitored.
- Kill‑switches (pause harvesting/trading) provide rapid risk mitigation.
- Follow least‑privilege and maintain audit trails for sensitive actions.
- Never store exchange API keys in plaintext; platform secrets are managed 
  elsewhere in the system.

## 🛠️ Technology at a glance
- Language & runtime: Java 23
- Frameworks: Spring Boot, JavaFX (desktop)
- UI libraries: ControlsFX, FormsFX, ValidatorFX, Ikonli, BootstrapFX,
  TilesFX, CSSFX, Scenic View, JavaFX SVG, ReactFX
- Data & integration: Spring JDBC, PostgreSQL driver, Jackson, Apache 
  HttpClient 5
- Integration glue: JavaFX Weaver for Spring + JavaFX wiring
- Build & tooling: Maven, OpenJFX Maven Plugin, JUnit Jupiter,
  Dokka


## 📝 Conventions
- Keep operator actions minimal, auditable, and reversible when possible.
- Use paper/safe modes first before enabling trading on production.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
