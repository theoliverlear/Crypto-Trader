---
name: General Issue
about: File a Bug, Feature Request, Improvement, or Task for Crypto-Trader
title: "[Type] Short, descriptive title"
labels: "needs triage"
assignees: ""
---

<!--
Thank you for taking the time to open an issue! This template is designed to work for both bugs and feature requests across the Crypto-Trader monorepo.
Please fill out all relevant sections. Irrelevant sections can be removed.
-->

## Issue Type
- [ ] Bug
- [ ] Feature Request
- [ ] Improvement / Refactor
- [ ] Documentation
- [ ] Security
- [ ] Performance
- [ ] Task / Chore

## Summary
A clear and concise description of the problem or the desired outcome.

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

## Version and Environment
- Commit/Tag: <commit SHA or tag>
- Branch: <branch name>
- App/Module Version: <version if applicable>
- OS: <Windows/Linux/macOS + version>
- JDK: <e.g., Temurin 21>
- Gradle: <e.g., 8.x>
- Database/Cache/Broker: <e.g., Postgres 15, Redis 7, Kafka, etc.>
- Cloud/Runtime: <e.g., Docker, K8s, bare metal>

## For Bugs
### Current Behavior
What happens currently. Include error messages.

### Expected Behavior
What you expected to happen.

### Steps To Reproduce
Provide a minimal, complete reproduction. Be specific.
1. 
2. 
3. 

### Logs / Stack Traces
Paste relevant logs. Use code blocks. Redact secrets.
```
<logs here>
```

### Screenshots / Visuals (optional)
If applicable, add screenshots to help explain the problem.

### Is this a Regression?
- [ ] Yes (worked before)
- [ ] No
If yes, the last known good version/commit:

### Impact
Describe severity (e.g., blocks deployments, data corruption risk, minor inconvenience, etc.).

## For API/WebSocket Issues (if applicable)
- Endpoint/Channel: <e.g., POST /api/v1/users, ws://.../trader>
- Method / Operation: <GET/POST/PUT/DELETE or subscribe/unsubscribe>
- Request: (headers/body)
```
{
}
```
- Response (status/body):
```
{
}
```
- Auth Context: <e.g., user vs admin, JWT scopes/roles>

## For Feature Requests / Improvements
### Motivation / Problem
What problem are you trying to solve? Why is this valuable?

### Proposed Solution
Describe the desired solution or API. Include examples, acceptance criteria, and non-goals.

### Alternatives Considered
List any alternative solutions you’ve considered.

### Backward Compatibility
- [ ] Breaking Change
If breaking, describe migration steps.

## Performance Considerations (if applicable)
Provide metrics, datasets, or scenarios where performance is a concern.

## Security Considerations (if applicable)
Does this involve auth, secrets, PII, or permissions? Outline potential risks and mitigations.

## Additional Context / References
Link related issues, PRs, discussions, or external references.

## Checklist
- [ ] I searched existing open and closed issues and didn’t find a duplicate.
- [ ] I included environment details and reproduction steps (for bugs).
- [ ] I added logs or screenshots where helpful.
- [ ] I included clear acceptance criteria (for features/improvements).
- [ ] I am willing to submit a PR to help fix/implement this.
