# Crypto Trader — Testing Module
## Repeatable tests and fixtures powering reliable releases

---

Crypto‑Trader‑Testing is the shared test toolkit for the platform. It provides
assertions, fixtures, helpers, and conventions so Api, Data, Engine, Admin,
and supporting modules can ship confidently with fast, reliable tests.

Important: Past results and backtests do not guarantee future performance.
Always start in paper mode. Liability is your own.

### What this module is
- A centralized library of test utilities for first‑party modules.
- A consistent foundation for unit, integration, and contract tests.
- Opinionated guidance on assertions, mocking, and test data generation.

## ⭐️ What it does
- Test foundations
  - JUnit Jupiter setup and extensions for modern, parameterized tests.
  - Spring Boot testing support for slicing and integration scenarios.
- Assertions and mocking
  - Rich assertions with AssertJ; flexible mocking/stubbing with Mockito.
- Containers and environments
  - Testcontainers BOM alignment for spinning up real dependencies (e.g.,
    PostgreSQL) when needed.
- HTTP and serialization helpers
  - Utilities that simplify building requests, serializing payloads, and 
    validating responses.
- Domain builders and fixtures
  - Builders and factories for domain objects to keep tests readable and 
    intention‑revealing.

## ✅ Why it matters
- Faster feedback: consistent patterns reduce boilerplate and cognitive load.
- Realistic checks: optional containerized dependencies increase confidence 
  without brittle mocks.
- Fewer regressions: shared fixtures and conventions reduce edge‑case blind 
  spots.

## 🔗 Where it fits in the platform
- Imported by Api, Data, Engine, and other modules to standardize their tests.
- Complements the Logging module’s `test‑jar` to assert on log behavior when 
  appropriate.

## 🛠️ Technology at a glance
- JUnit Jupiter (5.x), Spring Boot Test
- AssertJ, Mockito
- Testcontainers BOM
- Java 23, Maven

## 📝 Conventions
- Prefer clear Given/When/Then naming and arrange‑act‑assert structure.
- Keep tests deterministic; control time and randomness explicitly.
- Assert behaviors and contracts, not implementation details.
- Avoid coupling tests to private/internal APIs of other modules.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): `sigwarthsoftware@gmail.com`

## 📄 License
See `LICENSE.md` in the repository root.
