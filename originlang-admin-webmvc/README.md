# originlang-admin-webmvc

Reusable RBAC capability module for Servlet / Spring WebMVC + JPA applications.

It contributes:

- `sys_user`, `sys_role`, `sys_permission`, `sys_user_role_ref`, `sys_role_permission_ref` entity mappings
- JPA repositories for the RBAC aggregate
- Boot auto-configuration via `RbacWebMvcAutoConfiguration`

Host applications should import this module instead of copying RBAC table design and controllers into each app.
