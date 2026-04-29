---
name: originlang-module-routing
description: Use when changing this repository's code and you need to decide which module should own the change, how to avoid mixing MVC/WebFlux or JPA/R2DBC boundaries, and how to keep shared logic out of demo applications.
---

# OriginLang Module Routing

Use this skill when the task is "add or change code in this repo" and it is not immediately obvious which module owns the change.

## Quick routing

- Shared Java utility, ID, enum, exception, non-Spring helper:
  - `originlang-base-lib`

- Spring Boot auto-configuration, starter-style Bean wiring, shared configuration:
  - `originlang-base-spring-boot`

- Servlet MVC infrastructure, response wrapping, MVC annotations, servlet config:
  - `originlang-webmvc`

- Reactive WebFlux infrastructure:
  - `originlang-webflux`

- JPA entities, repository base classes, Hibernate-centric data abstractions:
  - `originlang-data-jpa`

- R2DBC repository base classes and reactive data abstractions:
  - `originlang-data-r2dbc`

- Resource-server auth, token parsing, current-user context:
  - `originlang-auth-resource`

- AI endpoints and Spring AI integration:
  - `originlang-ai`

- Example MVC app:
  - `originlang-app-admin-webmvc`

- Example WebFlux app:
  - `originlang-app-admin-webflux`

- Account, login, register, OAuth2 authorization-server flow:
  - `originlang-authorization-server`

## Hard boundaries

- Do not mix Servlet and Reactive APIs in one implementation path.
- Do not put shared logic into `originlang-app-*` modules if another app could reuse it.
- Do not add blocking JPA-style persistence to WebFlux / R2DBC paths.
- Do not add reactive repository types as a default pattern in MVC / JPA paths.
- Do not add dependency versions in child modules if the parent already manages them.

## Decision heuristic

Choose the module by asking, in order:

1. Can more than one app reuse this?
2. Is this framework integration or business/demo composition?
3. Is this MVC or WebFlux?
4. Is this JPA or R2DBC?
5. Is this auth, AI, cache, storage, or generic base capability?

If the answer stays "shared," move the change downward into a base module instead of upward into an app module.

## Before editing POMs

Check these first:

1. Root `pom.xml`
2. `originlang-parent/pom.xml`
3. `originlang-dependencies/pom.xml`
4. Target module `pom.xml`

Prefer extending existing parent conventions over adding one-off compiler or plugin config.
