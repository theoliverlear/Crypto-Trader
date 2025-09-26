---
name: Agile User Story
about: Create a user story with acceptance criteria, tasks, and a 10‑point estimate
title: "As a <role>, I want <capability> so that <benefit>."
labels: "needs triage, user story"
assignees: ""
---

<!--
This template mirrors the style of the example agile issues: clear user story, checklist of tasks, and a points estimate out of 10.
Fill out only the relevant sections; remove anything that doesn’t apply. Keep language concise and testable.
-->

## Story
As a <role>, I want <capability> so that <benefit>.

## Description / Context
- What problem does this solve? Who is impacted?
- Links to design docs, diagrams, or research.
- Constraints or important background.

## Affected Area(s)
Select the project(s)/module(s) impacted:
- [ ] Crypto-Trader-Api (REST, WebSocket)
- [ ] Crypto-Trader-Library (services, domain, utils)
- [ ] Crypto-Trader-Engine (trading logic)
- [ ] Crypto-Trader-Data (ingestion, storage)
- [ ] Crypto-Trader-Security (auth, permissions)
- [ ] Crypto-Trader-Admin (admin panel)
- [ ] Crypto-Trader-Analysis (analytics, machine learning)
- [ ] Crypto-Trader-Console (CLI tools)
- [ ] Crypto-Trader-Mobile (iOS/Android apps)
- [ ] Crypto-Trader-Website (frontend)
- [ ] Crypto-Trader-Docs (documentation)
- [ ] CI/CD (builds, deployments)
- [ ] Other (specify):

## Acceptance Criteria
Write testable criteria using Given/When/Then.
- [ ] Given <precondition>, when <action>, then <expected result>.
- [ ] Given <precondition>, when <action>, then <expected result>.
- [ ] Edge cases and error handling are covered.

## Tasks
Break the story into concrete steps. Use sub‑tasks/issues if helpful.
- [ ] Scope the change and confirm approach with maintainers.
- [ ] Implement core logic in <module>.
- [ ] Add/adjust API endpoints or contracts (if applicable).
- [ ] Update data models, migrations, or schemas (if applicable).
- [ ] Add unit tests (happy path + edge cases).
- [ ] Add integration tests (API, messaging, persistence) as needed.
- [ ] Update docs (README, API docs, ADRs, comments).
- [ ] Add observability (logs/metrics/traces) where useful.
- [ ] Perform manual validation in a dev environment.
- [ ] Prepare PR with description, screenshots, and notes.

## Non‑Goals / Out of Scope
- Items explicitly not addressed by this story.

## Dependencies / Blockers
- Upstream PRs, libraries, credentials, or other stories that must land first.

## Risks / Assumptions
- Technical, product, or operational risks and how to mitigate.
- Assumptions that, if false, will change the approach.

## Definition of Ready
- [ ] User impact and success metrics are understood.
- [ ] Acceptance criteria are clear and testable.
- [ ] Dependencies identified and feasible.
- [ ] Designs (if any) linked or approved.
- [ ] Stakeholders are aligned (labels, epic, owners set).

## Definition of Done
- [ ] All acceptance criteria satisfied.
- [ ] Tests added/updated and passing in CI.
- [ ] No critical/severe lints, security, or performance regressions.
- [ ] Documentation updated.
- [ ] Feature toggled/guarded if rollout risk exists.
- [ ] Deployed to a test/staging environment and validated.

## Estimation
Story Points (0–10): __/10

Guidance:
- 1–2: Trivial/very small
- 3–4: Small
- 5–6: Medium
- 7–8: Complex
- 9–10: Very complex/uncertain

## Labels / Metadata
- Epic / Parent: <link or ID>
- Milestone / Sprint: <name or date range>
- Assignees: <owner(s)>
- Priority: <P0/P1/P2>
- Environment(s): <dev/test/stage/prod>

## QA / Validation Notes
- Test data/scenarios to run through.
- How to verify behavior (screenshots, curl commands, Postman collection, etc.).

## Release Notes
End‑user or operator‑facing summary (if applicable).

## Rollback / Mitigation Plan
- How to disable/rollback if issues occur.
- Data migration reversal strategy (if any).

## Attachments
Add screenshots or link to images, diagrams, or recordings.
