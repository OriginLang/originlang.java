[English](./rules.md) | [中文](./rules_zh_CN.md)
# OriginLang Rules

This document defines how this repository should be implemented. New modules, new features, refactors, and AI-agent changes should follow these rules by default unless a clear architectural decision overrides them.

## 1. Technical Baseline

- Use JDK `25`.
- Use Spring Boot `4.0.2`.
- Use Maven multi-module build.
- Manage versions through `${revision}` and flatten them with `flatten-maven-plugin`.
- Keep web capabilities split between `originlang-webmvc` and `originlang-webflux`.
- Keep data capabilities split between `originlang-data-jpa` and `originlang-data-r2dbc`.
- The default code-generation toolchain includes Lombok, MapStruct, and Record Builder.
- Keep JPA metamodel generation only in modules that truly need it. Do not enable `hibernate-processor` broadly in application modules.
- Use `spring-javaformat-maven-plugin` for formatting.
- Use `jacoco-maven-plugin` for coverage reporting.
- Do not treat Spring AOT as a default requirement for day-to-day development in this repository.

## 2. Module Layering Rules

- `originlang-dependencies` is only for dependency versions and release profiles. It must not contain business code.
- `originlang-parent` is only for plugins, compilation, testing, source jars, and documentation rules. It must not contain business code.
- `originlang-base-lib` contains Spring-independent shared types, exceptions, and utilities.
- `originlang-base-spring-boot` contains shared Spring Boot auto-configuration and starter behavior.
- `originlang-webmvc` and `originlang-webflux` contain framework integration, not business logic.
- `originlang-data-jpa` and `originlang-data-r2dbc` contain shared data abstractions, base classes, and data-side extensions.
- `originlang-admin-webmvc` and `originlang-admin-webflux` are shared RBAC capability modules.
- `originlang-authorization-server` is the runnable authorization application module.
- Put new capabilities in shared modules or starter modules first. Use runnable application modules only when the behavior is clearly entrypoint-specific or standalone service behavior.

Choose the target module in this order:

1. Can multiple apps reuse this behavior?
2. Is it framework integration or auto-configuration?
3. Is it only demo wiring, entrypoint behavior, or sample data?
4. Would the change blur MVC/WebFlux or JPA/R2DBC boundaries?

## 3. Java And Code Style Rules

- Source encoding must be `UTF-8`.
- All modules should keep `-parameters` enabled, and new code should use clear constructor and method parameter names.
- Prefer Java 25 syntax only when it clearly improves readability.
- Default to **singular naming** for domain-facing model elements:
  - entity class names should be singular
  - table names should be singular
  - field names should be singular
  - REST resource segments should be singular
  - prefer `/user`, `/role`, `/permission` over `/users`, `/roles`, `/permissions`
- Public APIs should keep return contracts, exception types, and naming stable. Avoid unnecessary compatibility breaks.
- Shared utilities belong in foundational modules. Do not duplicate them across modules.
- Comments should explain non-obvious design choices, not restate the code.
- Prefer small, explicit types and methods over hiding complex behavior in annotations or static helpers.
- If a change affects public APIs, config keys, exception structure, or response contracts, evaluate compatibility first.

## 4. Spring Boot Rules

- Prefer Spring Boot auto-configuration instead of requiring large amounts of manual bean wiring.
- Keep shared auto-configuration in starter or Spring Boot modules, not in application modules.
- Config properties must have clear prefixes. Avoid vague top-level configuration keys.
- Keep MVC and WebFlux capabilities separate. Do not mix Servlet and Reactive APIs in one module.
- Code in `originlang-webmvc` must not depend on WebFlux runtime types, and vice versa.
- Application modules should mostly host entrypoints, sample controllers, sample configuration, and composition of existing shared modules.
- Before adding new Spring configuration, check whether shared auto-configuration, existing `application-*.yml`, or shared config classes already cover the need.

## 5. Data Access Rules

- Shared JPA base classes, repository extensions, and entity capabilities belong in `originlang-data-jpa`.
- Shared R2DBC behavior belongs in `originlang-data-r2dbc`.
- Prefer **Jakarta Data** contracts first when designing new repository APIs.
- Only fall back to Spring Data-specific repository types when the target module already standardizes them or when a required feature is not practical through Jakarta Data alone.
- Reuse existing entity bases such as `BaseEntity` and `BaseIdEntity` whenever possible.
- Application modules should enable `hibernate-processor` only when they truly depend on generated Hibernate metadata.
- For ordinary entity mapping, repository, or service development, do not enable extra Hibernate metamodel generation in application modules.
- Querydsl, Jakarta Data, and JPA metamodel generation must be enabled only when needed. Avoid stacking multiple processors that conflict.
- Do not push blocking persistence behavior into WebFlux / R2DBC paths.
- Do not introduce reactive repository behavior as the default in MVC / JPA paths.

## 6. Code Generation Rules

- Use Lombok only to reduce boilerplate, not to hide complex business logic.
- Use MapStruct for DTO / VO / Entity mapping. Keep complex aggregation logic in explicit Java code.
- Use Record Builder only where immutable modeling clearly helps. It is not a repository-wide mandatory style.
- If annotation processors conflict, remove unnecessary ones instead of layering more configuration everywhere.
- When changing `pom.xml`, reuse compiler and processor conventions from `originlang-parent` first.

## 7. Dependency And Version Rules

- Child-module dependency versions should be managed by parent dependency management, not hardcoded locally.
- Internal module dependencies must not declare their own version numbers.
- External dependency versions should be centralized in `originlang-dependencies`.
- Before adding a dependency, check whether it is already covered by the Spring Boot BOM, Spring AI BOM, or an existing repository dependency.
- Do not introduce multiple frameworks for the same capability unless compatibility or migration needs clearly justify it.
- Do not spread heavyweight dependencies across multiple modules because of a single sample use case.
- After dependency changes, ensure affected modules still build correctly inside the reactor.

## 8. Build And Testing Rules

- Default to **test-driven development**:
  - write a failing test first
  - implement the minimum code to make it pass
  - refactor while keeping tests green
- Local development defaults to the `dev` profile.
- `originlang-authorization-server` defaults to the `dev` profile for local development.
- GitHub Packages should publish only `-SNAPSHOT` versions via `-Pgithub`.
- Maven Central should publish only release versions via `-Pcentral`.
- Change versions only through `${revision}` instead of editing many modules separately.
- After adding a new module, `mvn clean install` should still pass in the full reactor.
- New features should add corresponding tests. Bug fixes should preferably add regression tests.
- If full testing is not available, provide at least one repeatable smoke-test path.
- If Java 25, Maven, Ollama, Redis, or database prerequisites are missing, say so explicitly.

Default testing style by module:

- Base modules:
  - use plain JUnit Jupiter unit tests
  - suitable for modules such as `originlang-base-lib` and utility-heavy shared behavior
  - do not introduce `@SpringBootTest` for pure utility logic

- Spring base modules:
  - prefer the smallest Spring-aware test that proves the behavior
  - use `spring-boot-starter-test` only when behavior depends on application context or Boot wiring

- JPA modules:
  - default to `@DataJpaTest`
  - prefer Spring Boot 4's `spring-boot-starter-data-jpa-test`
  - do not replace repository or entity slice tests with `@SpringBootTest` unless full application wiring is the actual target

- MVC web modules:
  - controller tests should default to `@WebMvcTest`
  - prefer Spring Boot 4's `spring-boot-starter-webmvc-test`
  - prefer `@MockitoBean` or `@Import` for collaborators rather than legacy `@MockBean`

- WebFlux web modules:
  - controller tests should default to `@WebFluxTest`
  - use the matching Spring Boot 4 WebFlux test module
  - prefer `WebTestClient` for endpoint verification

Testing granularity rules:

- one controller should have one corresponding controller test class
- repository query, mapping, or constraint changes should come with `@DataJpaTest` coverage
- utility, assertion, or converter classes should have dedicated JUnit tests
- do not use one broad `@SpringBootTest` to replace behavior that should be covered by a narrower slice test

Spring Boot 4 testing migration rules:

- Spring Boot 4 splits testing support into more focused feature-specific `-test` modules
- `@WebMvcTest` maps to the `spring-boot-webmvc-test` module and is often pulled in via `spring-boot-starter-webmvc-test`
- `@DataJpaTest` maps to the `spring-boot-data-jpa-test` module and is often pulled in via `spring-boot-starter-data-jpa-test`
- `@WebFluxTest` maps to the `spring-boot-webflux-test` module
- older `org.springframework.boot.test.autoconfigure.*` package paths are no longer always the preferred imports in Boot 4
- prefer `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`
- prefer `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`
- prefer `org.springframework.boot.webflux.test.autoconfigure.WebFluxTest`
- do not treat Spring Boot `@MockBean` / `@SpyBean` as the default for new code; prefer Spring Framework `@MockitoBean` / `@MockitoSpyBean`

## 9. Release Rules

- `dev` is for local builds and does not require GPG or Javadoc publishing.
- `github` is only for SNAPSHOT publishing and must not receive release versions.
- `central` requires GPG signing, Javadoc jars, and release versions.
- Do not overwrite published versions. Every formal release must increment the version.

## 10. Documentation Rules

- Every new module should get at least a short responsibility description.
- When adding a new profile, release flow, or code-generation rule, update `docs`.
- Starter modules, sample apps, and application modules that are meant for external use should explain their purpose and dependencies in README or docs.
- If module names, ports, default profiles, or runtime instructions change, update docs at the same time.
- For AI-facing documentation, prefer explaining where code belongs, how it runs, and how it is verified instead of only writing high-level concepts.

## 11. New Module Checklist

- Is the module a foundational library, starter, or application module?
- Should it be added to root `pom.xml` `<modules>`?
- Should it be added to `originlang-parent` dependency management?
- Does it truly need annotation processors? If so, which ones?
- Does it need MVC or WebFlux, rather than both?
- Does it need tests, sample configuration, and documentation entrypoints?
- Is there already an existing module that should be extended instead of creating a new one?

## 12. Runtime And Entrypoint Rules

Default runnable entrypoints:

- `originlang-admin-webmvc`
  - shared MVC/JPA RBAC tables, repositories, controllers, and auto-configuration
  - import into a host MVC application to expose `/admin/rbac/**`

- `originlang-admin-webflux`
  - shared WebFlux/R2DBC RBAC tables, repositories, controllers, schema init, and auto-configuration
  - import into a host WebFlux application to expose `/admin/rbac/**`

- `originlang-authorization-server`
  - port `8080`
  - profile `dev`
  - default H2 in-memory database

If the goal is the fastest repository smoke test, start with `originlang-authorization-server`.

## 13. Security And AI Rules

- Auth and authorization-chain changes should generally land in `originlang-auth-resource` or `originlang-authorization-server`.
- When changing whether an endpoint is anonymous, inspect both controller annotations and filter-chain behavior.
- If local debugging depends on special headers, default tokens, sample users, or custom anonymous behavior, document it explicitly.
- `originlang-ai` defaults to Spring AI + Ollama and depends on `spring.ai.ollama.base-url`.
- AI endpoint changes should account for clear failure paths when model services are unavailable.
