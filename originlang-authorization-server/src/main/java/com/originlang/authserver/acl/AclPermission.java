package com.originlang.authserver.acl;

public class AclPermission {

	private Long id;

	/**
	 * Object（对象）：需要保护的资源，如 Document、File、Order 等
	 */
	private String obj;

	/**
	 * Sid（Security Identity）：可以是 Principal 或 Role，标识用户、角色或群组
	 */
	private Long sid;

	/**
	 * Permission（权限）：指定对资源的具体操作，如 READ、WRITE、DELETE 等。
	 */

	/**
	 * Acl（访问控制列表）：将用户或角色与权限及资源关联起来，定义哪些用户可以对哪些对象执行什么操作。
	 */

}
