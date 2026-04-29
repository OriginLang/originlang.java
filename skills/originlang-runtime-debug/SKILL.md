---
name: originlang-runtime-debug
description: Use when you need to build, run, smoke-test, or debug this repository's applications, including knowing the right module, profile, port, and common local prerequisites like H2, Ollama, Redis, or Docker Compose.
---

# OriginLang Runtime Debug

Use this skill when the task is to run the repo, verify a module, or debug startup/runtime behavior.

## Main runnable modules

### `originlang-app-admin-webmvc`

- Purpose: fastest local smoke test for servlet stack
- Port: `10000`
- Profile: `dev`
- Storage: H2 in-memory
- Includes: `originlang-ai`
- Notes:
  - creates default user `admin/admin`
  - exposes `/ai/generate`

Run:

```bash
mvn -pl originlang-app-admin-webmvc -am spring-boot:run
```

### `originlang-app-admin-webflux`

- Purpose: minimal reactive sample
- Port: `8889`
- Profile: `h2`
- Storage: R2DBC H2

Run:

```bash
mvn -pl originlang-app-admin-webflux -am spring-boot:run
```

### `originlang-authorization-server`

- Purpose: account register/login/JWT/OAuth2 flow
- Port: `8080`
- Profile: `dev`
- Storage: H2 in-memory

Run:

```bash
mvn -pl originlang-authorization-server -am spring-boot:run
```

## Build and test

Compile one target with dependencies:

```bash
mvn -pl <module> -am clean compile
```

Run tests:

```bash
mvn -pl <module> -am test
```

Whole repo build:

```bash
mvn clean install -DskipTests
```

Preferred test strategy while debugging changes:

- base / utility modules: JUnit Jupiter unit tests
- JPA modules: `@DataJpaTest`
- MVC controllers: `@WebMvcTest`
- WebFlux controllers: `@WebFluxTest`

## Common local blockers

- Java must match the repo baseline: `25`
- There is no Maven wrapper in this repo
- `originlang-ai` expects Ollama at `http://localhost:11434`
- Some optional infra lives in root `docker-compose.yml`
- Many local flows use H2 by default; do not assume MySQL or Postgres is required
- Spring Boot 4 testing uses focused `-test` modules; do not assume older imports or annotations still live in the same packages

## Smoke-test order

Use this order unless the task targets a specific subsystem:

1. `originlang-app-admin-webmvc`
2. `GET /ai/generate` if Ollama is available
3. `originlang-authorization-server` for auth flows
4. `originlang-app-admin-webflux` only when debugging reactive paths

## Failure triage

- Startup fails immediately:
  - check Java version, module name, active profile, and missing local services

- Auth request fails:
  - inspect both controller annotations and security/filter logic

- AI request fails:
  - confirm Ollama is up and the configured model exists

- Data-related failure:
  - confirm whether the module is H2/JPA or H2/R2DBC before changing code
