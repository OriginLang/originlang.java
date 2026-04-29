[English](./README.md) | [中文](./README_zh_CN.md)
# OriginLang

Enterprise-grade Java foundation codebase.

## Overview

OriginLang is a modular Java base project for enterprise applications. It provides reusable foundational capabilities and components to accelerate backend and admin system development.

## Tech Stack

- Spring Boot 4.x
- Hibernate 7.x
- QueryDSL (OpenFeign)

## Module Guide

This repository is a Maven multi-module codebase. New functionality should be placed in the lowest reusable layer instead of keeping shared logic inside runnable applications.

### Layer Overview

- Version and build governance: centralizes dependency versions, plugins, and release profiles.
- Foundation layer: reusable capabilities not tied to business logic.
- Framework integration layer: shared Web and data-access abstractions.
- Capability extension layer: modular integrations such as Redis, MongoDB, MinIO, Elasticsearch, Neo4j, Quartz, and AI.
- Application and composition layer: reusable RBAC capability modules plus the authorization server entry point.

### Reactor Modules

The following modules are part of the root reactor build:

| Module | Role | Description |
| --- | --- | --- |
| `originlang-dependencies` | Version governance | Manages BOMs, external dependency versions, and repository settings. |
| `originlang-parent` | Build governance | Manages plugins, compiler settings, test conventions, and release profiles. |
| `originlang-base-lib` | Foundation library | Shared utilities, base types, IDs, enums, and exceptions independent of Spring. |
| `originlang-base-spring-boot` | Foundation auto-config | Base Spring Boot starter and shared auto-configuration entry points. |
| `originlang-webmvc` | Web MVC integration | Shared capabilities for Servlet / Spring Web MVC applications. |
| `originlang-webflux` | WebFlux integration | Shared capabilities for reactive / Spring WebFlux applications. |
| `originlang-data-jpa` | JPA data layer | JPA/Hibernate base abstractions, entities, repositories, and data access extensions. |
| `originlang-data-r2dbc` | R2DBC data layer | Reactive data abstractions and R2DBC extensions. |
| `originlang-redis` | Capability extension | Redis integration module. |
| `originlang-mongodb` | Capability extension | MongoDB integration module. |
| `originlang-elasticsearch` | Capability extension | Elasticsearch integration module. |
| `originlang-neo4j` | Capability extension | Neo4j graph database integration module. |
| `originlang-minio` | Capability extension | MinIO object storage integration module. |
| `originlang-quartz` | Capability extension | Quartz scheduler integration module. |
| `originlang-ai` | Capability extension | AI-related capabilities and Spring AI integrations. |
| `originlang-auth-resource` | Security resource module | Resource-server-side authentication and authorization capabilities. |
| `originlang-authorization-server` | Security auth server module | OAuth2 / authorization server capabilities and account flows. |
| `originlang-admin-webmvc` | RBAC capability module (MVC) | Shared Servlet/WebMVC + JPA RBAC tables, repositories, controllers, and auto-configuration. |
| `originlang-admin-webflux` | RBAC capability module (WebFlux) | Shared Reactive/WebFlux + R2DBC RBAC tables, repositories, controllers, and auto-configuration. |

### Placement Guidelines

- New generic code: prefer `originlang-base-lib`.
- New starters or auto-configurations: prefer `originlang-base-spring-boot` or a dedicated capability module.
- New Servlet web capability: prefer `originlang-webmvc`.
- New reactive web capability: prefer `originlang-webflux`.
- New shared JPA capability: prefer `originlang-data-jpa`.
- New shared reactive data capability: prefer `originlang-data-r2dbc`.
- New security resource-server behavior: prefer `originlang-auth-resource`.
- New OAuth2 / login / register flow: prefer `originlang-authorization-server`.
- New Servlet/JPA RBAC behavior: prefer `originlang-admin-webmvc`.
- New Reactive/R2DBC RBAC behavior: prefer `originlang-admin-webflux`.
- New runnable authorization-server behavior: prefer `originlang-authorization-server`.

## Build

```bash
mvn clean install "-Dmaven.test.skip=true"
```

## Developer Commands

```bash
# Fast local build (skip publish, tests, GPG, and Javadoc)
mvn clean install -DskipPublish -DskipTests -Dgpg.skip=true -DskipJavadoc

# Standard local build
mvn clean install

# Deploy to Maven Central
mvn -Pcentral deploy

# Deploy to GitHub Packages
mvn -Pgithub deploy

# Alternative deploy command
# mvn clean deploy --settings .github/settings.xml
```
