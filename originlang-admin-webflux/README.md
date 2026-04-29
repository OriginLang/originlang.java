# originlang-admin-webflux

Reusable RBAC capability module for Reactive / Spring WebFlux + R2DBC applications.

It contributes:

- `sys_user`, `sys_role`, `sys_permission`, `sys_user_role_ref`, `sys_role_permission_ref` reactive table mappings
- R2DBC repositories for the RBAC aggregate
- `/admin/rbac/**` reactive management endpoints
- Boot auto-configuration and schema initialization via `RbacWebFluxAutoConfiguration`

Host applications should import this module to reuse the RBAC schema and endpoints instead of rebuilding them in each WebFlux service.
