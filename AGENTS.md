[English](./AGENTS.md) | [中文](./AGENTS_zh_CN.md)
# AGENTS.md

This file is for AI coding agents working in this repository. Its goal is not to introduce the product. Its goal is to help agents make correct changes in `originlang.java` quickly and avoid preventable mistakes.

## 1. Repository Shape

This is a **Maven multi-module Java repository** with three broad categories of modules.

- **Runnable application modules**
  - `originlang-authorization-server`

- **RBAC capability modules**
  - `originlang-admin-webmvc`
  - `originlang-admin-webflux`

- **Shared capability modules**
  - `originlang-base-lib`
  - `originlang-base-spring-boot`
  - `originlang-webmvc`
  - `originlang-webflux`
  - `originlang-data-jpa`
  - `originlang-data-r2dbc`
  - `originlang-auth-resource`
  - `originlang-ai`
  - `originlang-redis`
  - `originlang-minio`
  - `originlang-mongodb`
  - `originlang-elasticsearch`
  - `originlang-neo4j`
  - `originlang-quartz`

Default placement logic:

- Put reusable capability changes in shared modules first.
- Put composition or host-app-specific behavior in runnable application modules.
- Do not leave reusable RBAC logic in host applications.

## 2. Tech Stack Snapshot

- **Java**: 25
- **Build**: Maven reactor multi-module build
- **BOM / Parent**
  - `originlang-dependencies` manages dependency versions
  - `originlang-parent` manages plugins, compilation, testing, and release rules
- **Spring Boot**: 4.0.2
- **ORM / Data**
  - Hibernate 7.2.4.Final
  - JPA / Jakarta Persistence
  - R2DBC
  - OpenFeign Querydsl 7.1
- **Code Generation**
  - Lombok
  - MapStruct
  - Record Builder
  - some modules enable Hibernate processor or Querydsl APT only when needed
- **Security**
  - Spring Security
  - OAuth2 Authorization Server
  - JWT
- **AI**
  - Spring AI 2.0.0-M4
  - Ollama
  - pgvector starter
- **Infra / Storage**
  - H2
  - Redis
  - PostgreSQL
  - MinIO
  - MongoDB
  - Elasticsearch
  - Quartz
- **Testing**
  - JUnit Jupiter
  - JaCoCo
- **Formatting**
  - `spring-javaformat-maven-plugin`

## 3. What To Read First

Build context in this order:

1. root `pom.xml`
2. `originlang-parent/pom.xml`
3. `originlang-dependencies/pom.xml`
4. the target module `pom.xml`
5. the target module `application.yml` / `application-*.yml` when the module is runnable
6. the module entrypoint `*Application` / `*App` when one exists
7. related Controller / Service / Repository / AutoConfiguration classes

If the task is just "how does this repo run?" or "where does this feature live?", start here:

- Servlet RBAC module: `originlang-admin-webmvc`
- Reactive RBAC module: `originlang-admin-webflux`
- Authentication and authorization flows: `originlang-authorization-server`

## 4. Module Boundaries

These boundaries are mandatory:

- `originlang-dependencies` must not contain business code.
- `originlang-parent` must not contain business code.
- `originlang-base-lib` is for non-Spring foundational utilities and shared types.
- `originlang-base-spring-boot` is for shared Spring Boot auto-configuration and starter-style wiring.
- `originlang-webmvc` and `originlang-webflux` must stay separate. Do not mix Servlet and Reactive APIs.
- `originlang-data-jpa` and `originlang-data-r2dbc` must stay separate. Do not mix blocking and reactive persistence abstractions.
- Application modules should compose shared modules rather than duplicate them.

When deciding where a change belongs:

- If multiple applications can reuse it, move it into a shared module.
- If it is only demo wiring, controller entrypoint behavior, or sample configuration, keep it in the app module.
- If it is starter or auto-configuration behavior, prefer `originlang-base-spring-boot` or the relevant shared capability module.

## 5. Common Commands

Run commands from the repository root.

Build the entire repo:

```bash
mvn clean install -DskipTests
```

Compile one target module and its dependencies:

```bash
mvn -pl originlang-admin-webmvc -am clean compile
mvn -pl originlang-admin-webflux -am clean compile
mvn -pl originlang-authorization-server -am clean compile
```

Run the authorization server:

```bash
mvn -pl originlang-authorization-server -am spring-boot:run
```

Run tests:

```bash
mvn test
mvn -pl <module> -am test
```

## 6. Runtime Profiles

### `originlang-admin-webmvc`

- Type: reusable RBAC capability module
- Stack: `webmvc + data-jpa`
- Provides: shared RBAC entities, repositories, controllers, and Boot auto-configuration
- Import outcome: host MVC/JPA applications get RBAC tables and `/admin/rbac/**` endpoints

Useful when:

- changing shared Servlet/JPA RBAC behavior
- exposing reusable RBAC admin endpoints from MVC applications

### `originlang-admin-webflux`

- Type: reusable RBAC capability module
- Stack: `webflux + data-r2dbc`
- Provides: shared reactive RBAC tables, repositories, controllers, schema initializer, and Boot auto-configuration
- Import outcome: host WebFlux/R2DBC applications get RBAC tables and `/admin/rbac/**` endpoints

Useful when:

- changing shared reactive RBAC behavior

### `originlang-authorization-server`

- Port: `8080`
- Profile: `dev`
- Default database: H2 in-memory
- Depends on: `auth-resource + data-jpa + webmvc + redis`
- Provides register, login, JWT, and OAuth2 authorization behavior

Useful when:

- changing account flows
- changing token creation or parsing
- changing OAuth client or authorization-server behavior

## 7. Implementation Rules

### 7.1 Web

- MVC changes belong in `originlang-webmvc`, `originlang-admin-webmvc`, or MVC host applications.
- WebFlux changes belong in `originlang-webflux`, `originlang-admin-webflux`, or reactive host applications.
- Do not mix `jakarta.servlet.*` and `reactor.*` in the same implementation path.
- Keep controllers thin. Push non-trivial logic into services or shared modules.

### 7.2 Data

- JPA entities, repository base classes, and Hibernate-related abstractions belong in `originlang-data-jpa`.
- R2DBC repository base classes and reactive data access belong in `originlang-data-r2dbc`.
- Prefer Jakarta Data contracts first for new repository APIs.
- Use Spring Data-specific repository types only when the module already standardizes them or the required behavior is tightly coupled to Spring Data features.
- Reuse existing shared entity bases such as `BaseEntity` and `BaseIdEntity` when possible.
- Do not introduce new persistence abstractions in app modules for simple cases.

### 7.3 Security

- JWT, current-user context, and resource-server filter chain behavior mostly belongs in `originlang-auth-resource`.
- Register, login, token issuing, and authorization-server behavior mostly belongs in `originlang-authorization-server`.
- When changing auth behavior, inspect annotations and filter-chain behavior together. Do not change only a controller and assume the job is done.

### 7.4 AI

- AI behavior belongs primarily in `originlang-ai`.
- `originlang-admin-webmvc` and `originlang-admin-webflux` are not AI sample apps.

## 8. Annotation Processors And Generated Code

This repository uses several annotation processors. Change compiler configuration carefully.

Common processors:

- Lombok
- MapStruct
- Record Builder

Processors that should only be enabled when necessary:

- Hibernate processor
- Querydsl APT

Rules:

- Do not enable Hibernate processor in every application module by default.
- Do not add Querydsl APT to modules that do not need it.
- If processors conflict, prefer removing unnecessary processors rather than layering more config on top.
- Before changing a `pom.xml`, inspect what `originlang-parent` already standardizes.

## 8.1 Naming Conventions

- Prefer singular names for entity classes, table names, field names, and REST resource paths.
- Default resource paths should look like `/user`, `/role`, `/permission`, not `/users`, `/roles`, `/permissions`.
- Keep relationship names singular as well. Prefer `roleId` over plural forms unless the field actually stores a collection.

## 9. Formatting, Testing, And Coverage

- `spring-javaformat-maven-plugin` runs `apply` in the `validate` phase.
- Maven runs may therefore rewrite formatting automatically.
- JUnit Jupiter is the default test framework.
- JaCoCo generates coverage reports during the `test` phase.

### 9.1 TDD Is The Default

Follow test-driven development by default:

1. write a failing test first
2. implement the minimum change to make it pass
3. refactor while keeping tests green

Do not default to "implement first, test later".

### 9.2 Preferred Test Type By Module

- Base utility modules:
  - use plain JUnit Jupiter tests
  - avoid `@SpringBootTest` for simple utility logic

- Spring base modules:
  - prefer the smallest Spring-aware test that proves the behavior
  - only use broader Boot context tests when the behavior truly depends on Boot wiring

- JPA modules:
  - default to `@DataJpaTest`
  - prefer Spring Boot 4's `spring-boot-starter-data-jpa-test`

- MVC controller tests:
  - default to `@WebMvcTest`
  - prefer Spring Boot 4's `spring-boot-starter-webmvc-test`
  - use `@MockitoBean` or `@Import` for collaborators

- WebFlux controller tests:
  - default to `@WebFluxTest`
  - use `WebTestClient`
  - use the matching Spring Boot 4 WebFlux test module

### 9.3 Spring Boot 4 Testing Changes

Keep these Boot 4 rules in mind:

- testing support is split into more focused feature-specific `-test` modules
- `@WebMvcTest` should come from `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`
- `@DataJpaTest` should come from `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`
- `@WebFluxTest` should come from `org.springframework.boot.webflux.test.autoconfigure.WebFluxTest`
- new tests should prefer Spring Framework `@MockitoBean` / `@MockitoSpyBean`
- do not default to legacy `@MockBean` / `@SpyBean` patterns for new code

### 9.4 Minimum Testing Expectation

After a code change, do at least one of the following:

- run the target module tests
- add a minimal regression test if none exists
- clearly state what environment blocker prevented verification

## 10. Common Pitfalls

- **Use real module names**: the shared RBAC modules are `originlang-admin-webmvc` and `originlang-admin-webflux`.
- **Java must match the repo baseline**: root POM requires Java 25.
- **There is no Maven wrapper**: rely on system or SDK-managed Maven.
- **AOT is disabled by default**: parent POM disables `process-aot` and `process-test-aot`.
- **Profiles differ by application**:
  - MVC app defaults to `dev`
  - WebFlux app defaults to `h2`
  - authorization server defaults to `dev`
- **Many local flows use H2 by default**: do not assume MySQL or PostgreSQL is required.
- **Docker Compose is not always automatically used**: some modules explicitly disable `spring.docker.compose.enabled`.
- **Auth behavior is not only in controllers**: filter-chain and token parsing logic matter.

## 11. Recommended Workflow

### When fixing a bug

1. Identify whether the bug is in MVC, WebFlux, JPA, R2DBC, Security, or AI.
2. Find the owning shared module first, then check whether app modules add composition-specific behavior.
3. Add the smallest useful regression test.
4. Change only the necessary modules. Do not expand into broad refactors by default.

### When adding a new capability

1. Decide whether it is shared capability or sample/app behavior.
2. Put shared behavior into a shared module or starter first.
3. Put only entrypoint wiring, demo exposure, or sample behavior into application modules.
4. Update the relevant docs.

### When changing dependencies or compiler configuration

1. Check `originlang-dependencies`.
2. Check `originlang-parent`.
3. Avoid hardcoding versions in child modules.
4. Avoid adding annotation processors unless there is a clear need.

## 12. Minimum Standard For Agents

- Use the real module names and real ports.
- Do not mix MVC and WebFlux stacks.
- Do not bypass parent POM dependency and plugin conventions.
- Do not duplicate shared logic inside application modules.
- Always provide a minimal verification path for behavior changes.
- If Java 25, Maven, or other required local prerequisites are missing, say so explicitly instead of implying verification succeeded.
