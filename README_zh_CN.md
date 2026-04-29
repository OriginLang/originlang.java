[English](./README.md) | [中文](./README_zh_CN.md)
# OriginLang

企业级 Java 基础代码库。

## 项目简介

OriginLang 是一个面向企业场景的 Java 基础代码库，提供了常见业务开发所需的基础能力与模块化组件，便于快速搭建中后台与服务端应用。

## 技术栈

- Spring Boot 4.x
- Hibernate 7.x
- QueryDSL (OpenFeign)

## 模块说明

这是一个 Maven 多模块仓库。新增功能时，优先放到可复用的下层模块，而不是把共享逻辑直接堆到应用模块里。

### 分层总览

- 版本与构建治理层：统一依赖版本、插件和发布规则。
- 基础能力层：提供与业务无关的通用能力。
- 框架集成层：封装 Web 与数据访问技术栈能力。
- 场景扩展层：对 Redis、MongoDB、MinIO、Elasticsearch、Neo4j、Quartz、AI 等能力做模块化扩展。
- 组合与应用层：提供可复用的 RBAC 能力模块，以及授权服务入口。

### Reactor 正式模块

以下模块参与根工程构建：

| 模块 | 定位 | 说明 |
| --- | --- | --- |
| `originlang-dependencies` | 版本治理 | 管理 BOM、外部依赖版本和仓库配置。 |
| `originlang-parent` | 构建治理 | 管理插件、编译参数、测试约定和发布 profile。 |
| `originlang-base-lib` | 基础库 | 与 Spring 无关的通用类型、ID、枚举、异常和工具能力。 |
| `originlang-base-spring-boot` | 基础自动配置 | Spring Boot 基础 starter 与共享自动配置入口。 |
| `originlang-webmvc` | Web MVC 能力 | 基于 Servlet / Spring Web MVC 的通用能力封装。 |
| `originlang-webflux` | WebFlux 能力 | 基于 Reactive / Spring WebFlux 的通用能力封装。 |
| `originlang-data-jpa` | JPA 数据层 | JPA/Hibernate 基类、实体、仓储与数据访问扩展。 |
| `originlang-data-r2dbc` | R2DBC 数据层 | Reactive 数据访问与 R2DBC 扩展。 |
| `originlang-redis` | 场景扩展 | Redis 集成能力封装。 |
| `originlang-mongodb` | 场景扩展 | MongoDB 集成能力封装。 |
| `originlang-elasticsearch` | 场景扩展 | Elasticsearch 集成能力封装。 |
| `originlang-neo4j` | 场景扩展 | Neo4j 图数据库集成能力封装。 |
| `originlang-minio` | 场景扩展 | MinIO 对象存储能力封装。 |
| `originlang-quartz` | 场景扩展 | Quartz 定时任务能力封装。 |
| `originlang-ai` | 场景扩展 | AI 相关能力与 Spring AI 集成封装。 |
| `originlang-auth-resource` | 安全资源模块 | 资源服务侧鉴权与安全能力。 |
| `originlang-authorization-server` | 安全授权模块 | OAuth2 / 授权服务及账号流程相关能力。 |
| `originlang-admin-webmvc` | RBAC 能力模块（MVC） | 面向 Servlet/WebMVC + JPA 的共享 RBAC 表、仓储、控制器与自动配置。 |
| `originlang-admin-webflux` | RBAC 能力模块（WebFlux） | 面向 Reactive/WebFlux + R2DBC 的共享 RBAC 表、仓储、控制器与自动配置。 |

### 模块放置建议

- 新增通用代码：优先放 `originlang-base-lib`。
- 新增 starter 或自动配置：优先放 `originlang-base-spring-boot` 或对应技术模块。
- 新增 Servlet Web 能力：优先放 `originlang-webmvc`。
- 新增 Reactive Web 能力：优先放 `originlang-webflux`。
- 新增共享 JPA 能力：优先放 `originlang-data-jpa`。
- 新增共享响应式数据能力：优先放 `originlang-data-r2dbc`。
- 新增资源服务侧安全能力：优先放 `originlang-auth-resource`。
- 新增 OAuth2、登录、注册相关流程：优先放 `originlang-authorization-server`。
- 新增 Servlet/JPA 场景的 RBAC 能力：优先放 `originlang-admin-webmvc`。
- 新增 Reactive/R2DBC 场景的 RBAC 能力：优先放 `originlang-admin-webflux`。
- 新增可运行的授权服务逻辑：优先放 `originlang-authorization-server`。

## 构建

```bash
mvn clean install "-Dmaven.test.skip=true"
```

## 开发者本地构建与发布

```bash
# 本地快速构建（跳过发布、测试、GPG 与 Javadoc）
mvn clean install -DskipPublish -DskipTests -Dgpg.skip=true -DskipJavadoc

# 常规本地构建
mvn clean install

# 发布到 Maven Central
mvn -Pcentral deploy

# 发布到 GitHub Packages
mvn -Pgithub deploy

# 备用发布命令
# mvn clean deploy --settings .github/settings.xml
```
