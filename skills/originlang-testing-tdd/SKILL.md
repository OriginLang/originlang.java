---
name: originlang-testing-tdd
description: Use when implementing or fixing code in this repository and you need the correct test-first strategy for base modules, JPA modules, MVC controllers, WebFlux controllers, and Spring Boot 4 test-slice changes.
---

# OriginLang Testing TDD

Use this skill whenever you touch code in this repository. The default workflow is test-driven development.

## TDD loop

Follow this order unless the task is strictly documentation-only:

1. write or update a failing test that captures the intended behavior
2. run the narrowest relevant test scope
3. implement the smallest change that makes the test pass
4. refactor while keeping tests green

Do not start with broad implementation and "add tests later" unless the user explicitly asks for a spike.

## Choose the right test shape

### Base modules

Examples:

- `originlang-base-lib`
- pure utility code
- non-Spring helpers
- assertions, IDs, converters, enums

Use:

- JUnit Jupiter unit tests
- no Spring context unless absolutely necessary

Avoid:

- `@SpringBootTest` for plain utility logic

### Spring base modules

Examples:

- `originlang-base-spring-boot`
- shared auto-configuration and Spring integration helpers

Use:

- JUnit first when logic is isolated
- narrow Spring Boot tests only when behavior depends on application context or auto-configuration

### JPA modules

Examples:

- `originlang-data-jpa`
- repositories
- entity mapping behavior
- persistence rules

Use:

- `@DataJpaTest`
- embedded database defaults unless the task explicitly requires otherwise

Boot 4 note:

- prefer the Boot 4 Data JPA test slice and matching dependency setup
- prefer imports from `org.springframework.boot.data.jpa.test.autoconfigure.*`

Avoid:

- `@SpringBootTest` for ordinary repository and mapping verification

### MVC controller tests

Examples:

- controllers in `originlang-webmvc`
- controllers in `originlang-app-admin-webmvc`
- servlet security edge behavior that can be expressed as an MVC slice

Use:

- `@WebMvcTest`
- `MockMvc`
- `@MockitoBean` or `@Import` for controller collaborators

Boot 4 note:

- prefer `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`
- repo MVC modules already align with Boot 4 `spring-boot-starter-webmvc-test`

Avoid:

- loading the whole app for a single controller unless the test truly needs full integration
- defaulting to old `@MockBean` patterns in new tests

### WebFlux controller tests

Examples:

- controllers in `originlang-webflux`
- controllers in `originlang-app-admin-webflux`

Use:

- `@WebFluxTest`
- `WebTestClient`
- `@MockitoBean` or imported collaborators as needed

Boot 4 note:

- prefer imports from `org.springframework.boot.webflux.test.autoconfigure.*`

## Controller-to-test mapping

Default expectation:

- one controller class -> one corresponding controller test class

Examples:

- `ChatController` -> `ChatControllerTest`
- `SysUserController` -> `SysUserControllerTest`

Repository or entity changes should be accompanied by repository or slice tests, not only endpoint tests.

## Narrowest-first rule

Pick the smallest test that proves the behavior:

1. JUnit unit test
2. slice test (`@DataJpaTest`, `@WebMvcTest`, `@WebFluxTest`)
3. full `@SpringBootTest`

Move to a broader test only if the narrower one cannot represent the behavior under change.

## Spring Boot 4 migration notes

When writing or updating tests, remember:

- Boot 4 split test support into focused feature-specific modules
- package names for test slices are now feature-specific
- `@WebMvcTest` lives under `org.springframework.boot.webmvc.test.autoconfigure`
- `@DataJpaTest` lives under `org.springframework.boot.data.jpa.test.autoconfigure`
- `@WebFluxTest` lives under `org.springframework.boot.webflux.test.autoconfigure`
- Spring Framework's `@MockitoBean` / `@MockitoSpyBean` replace old Boot-managed mocking as the default new-code pattern

If you see older imports or older mocking annotations, treat migration as a possible follow-up concern when touching that test.
