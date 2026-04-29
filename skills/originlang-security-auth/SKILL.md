---
name: originlang-security-auth
description: Use when working on authentication, authorization, JWT, anonymous access, current-user context, or OAuth2 authorization-server behavior in this repository.
---

# OriginLang Security Auth

Use this skill when the task touches login, register, token parsing, anonymous access, or authorization-server behavior.

## Primary modules

- `originlang-auth-resource`
  - resource-server security chain
  - token parsing
  - current user / auth context

- `originlang-authorization-server`
  - account register/login
  - JWT issuing
  - OAuth2 authorization-server configuration

- `originlang-webmvc`
  - anonymous access annotations and servlet-side helper behavior

## Where to look first

1. Security config / filter chain
2. Token provider and JWT config
3. Anonymous access utilities or annotations
4. Controller endpoint annotations
5. Account service / login flow

Do not assume a controller annotation alone explains access behavior.

## Working rules

- If changing anonymous access, inspect both endpoint annotations and security filter logic.
- If changing token structure, inspect both token creation and token parsing.
- If changing login/register behavior, inspect service logic and returned token type together.
- If changing auth behavior for MVC apps, confirm the effect on `originlang-app-admin-webmvc`.
- If changing authorization-server behavior, verify whether there is an in-memory demo client that also needs updating.

## Local-debug cautions

- Some local debug flows rely on simplified behavior for development.
- Request authentication may depend on the `Authorization` header contract.
- Demo and sample defaults are acceptable for local development but should not be silently treated as production-safe behavior.

## Validation checklist

After a security-related change, verify at least one of:

1. anonymous endpoint still works as intended
2. protected endpoint rejects or accepts correctly
3. login still returns the expected token payload
4. token issued by the server can still be parsed by the resource side
