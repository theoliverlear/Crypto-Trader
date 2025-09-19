# Crypto Trader

A modular, end‑to‑end platform for building, testing, and running crypto trading systems. This documentation site brings together interactive APIs, module references, and library docs so you can explore and integrate quickly.

> Tip: Use the navigation bar at the top to jump to API explorers (CT, Data, Engine, Contact), deep references, or library docs.

---

## At a glance

- CT API – orchestration and application-facing endpoints. See: [CT API](api/index.md)
- Data API – market data ingestion, storage, and access. See: [Data API](data/index.md)
- Engine API – execution, strategies, and order routing. See: [Engine API](engine/index.md)
- Contact API – notifications, alerts, and user messaging. See: [Contact API](contact/index.md)
- Health, Logging, Security, Testing, Version – full module references under [References](#module-references)
- Libraries – reusable packages used across services under [Library](library/index.md)

---

## Quickstart

Choose how you want to begin.

### 1) Explore APIs in the browser

- Open any API section from the top nav: [CT API](api/index.md), [Data API](data/index.md), [Engine API](engine/index.md), or [Contact API](contact/index.md).
- Use the interactive explorer to list endpoints, inspect schemas, and try requests.

> Note: Some endpoints require authentication. See Authentication below.

### 2) Run the platform locally (developers)

- Clone the repository: https://github.com/theoliverlear/Crypto-Trader
- Build the project (pick one):
  - Gradle (Windows PowerShell): `./gradlew build` or `gradlew.bat build`
  - Maven (Windows PowerShell): `mvn -q -DskipTests package`
- Start the services you need (API, Data, Engine, Contact). Refer to the README files within each module for specific run instructions.
- Once running, navigate back here to use the API explorers against your local services.

---

## Authentication

Most protected endpoints expect a bearer token.

- Send the header: `Authorization: Bearer <your-token>`
- Obtain tokens via your configured Security/Auth flow (see Security module under References), or use an admin‑issued token in development.

Example curl (replace values appropriately):

```bash
curl -H "Authorization: Bearer <token>" -H "Content-Type: application/json" <your-api-base>/...endpoint
```

If you receive 401/403 responses, verify your token, scopes/roles, and target environment.

---

## Navigation guide

- Getting started guides: Guides → [Guides](guides/index.md)
- API explorers:
  - [CT API](api/index.md)
  - [Data API](data/index.md)
  - [Engine API](engine/index.md)
  - [Contact API](contact/index.md)
- Module References (generated docs):
  - Admin: [README](reference/Admin/README.md) · [Complete Docs](reference/Admin/api/index.html)
  - Api: [README](reference/Api/README.md) · [Complete Docs](reference/Api/api/index.html)
  - Assets: [README](reference/Assets/README.md) · [Complete Docs](reference/Assets/api/index.html)
  - Contact: [README](reference/Contact/README.md) · [Complete Docs](reference/Contact/api/index.html)
  - Data: [README](reference/Data/README.md) · [Complete Docs](reference/Data/api/index.html)
  - Engine: [README](reference/Engine/README.md) · [Complete Docs](reference/Engine/api/index.html)
  - Health: [README](reference/Health/README.md) · [Complete Docs](reference/Health/api/index.html)
  - Logging: [README](reference/Logging/README.md) · [Complete Docs](reference/Logging/api/index.html)
  - Security: [README](reference/Security/README.md) · [Complete Docs](reference/Security/api/index.html)
  - Testing: [README](reference/Testing/README.md) · [Complete Docs](reference/Testing/api/index.html)
  - Version: [README](reference/Version/README.md) · [Complete Docs](reference/Version/api/index.html)
- Libraries:
  - [Overview](library/index.md)
  - Explore individual library packages from the Library section in the navigation.

---

## Development notes

- JDK: Use a modern LTS JDK (17+) compatible with your build tooling.
- Build: The repository supports Gradle and Maven in most modules.
- Frontend: The Website (Angular) lives under `Crypto-Trader-Website` if you’re working on UI components.
- CI and coverage: See [Coverage report](coverage/index.html) for recent test coverage snapshots.

---

## Troubleshooting

- 401/403 from API calls: Check bearer token and roles; ensure you’re pointing to the right environment/base URL.
- CORS when testing from browser: Confirm the API’s CORS configuration in the Security module.
- Build failures: Clean and retry (`gradlew clean build` or `mvn clean package`), then consult module READMEs.

---

## Contributing & support

- Issues and feature requests: use GitHub Issues in the main repository.
- See CHANGELOG.md in the repo root for release notes.
- For security‑related topics, consult the Security module under References and follow responsible disclosure guidelines.

Next steps: Head to the [Guides](guides/index.md) or open an API explorer (e.g., [CT API](api/index.md)) to start building.
