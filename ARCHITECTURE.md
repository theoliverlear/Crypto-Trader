# Architecture of Crypto Trader

The highly modular nature of Crypto Trader is purposeful. It provides many
benefits.
- **Scalability**: Modules can be scaled independently based on demand.
- **Maintainability**: Isolated modules are easier to maintain and debug.
- **Flexibility**: New features can be added without affecting existing functionality.
- **Reusability**: Developers can reuse existing code without rewriting it.

There are costs associated with this architecture.
- **Performance**: The application is forced to do more name resolutions
                   during compile time.
- **Complexity**: The application is more complex, especially in terms of
                  managing dependency issues like circular dependencies or
                  version conflicts.

## APIs
Crypto Trader has dozens of functional APIs. Three of them are pure REST APIs,
and the others are sourced from event-driven modules.

The API to only ever reach a user is `Crypto-Trader-Api`.

Crypto Trader has internal systems that are not exposed to the user. This is
typically for data harvesting and communication with our machine learning
systems.

**Internal REST APIs**

`Crypto-Trader-Analysis`
- Multimedia intelligence
- Data analysis

`Crypto-Trader-Data`
- Finds currency data
- Accepts intelligence from `Crypto-Trader-Analysis`

---

## Event-driven Modules

Our binding between modules is Kafka. With Kafka, we publish and listen for
events occurring in the app. User-facing API data can be directly pulled from
the database or be sent as events by
`Crypto-Trader-Api`.

**Publishers** sends events without the expectation of a response.

**Gateways** send events with the expectation of a response.