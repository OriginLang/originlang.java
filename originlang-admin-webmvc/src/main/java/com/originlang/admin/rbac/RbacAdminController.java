package com.originlang.admin.rbac;

import com.originlang.webmvc.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/rbac")
public class RbacAdminController {

	private final RbacAdminService rbacAdminService;

	public RbacAdminController(RbacAdminService rbacAdminService) {
		this.rbacAdminService = rbacAdminService;
	}

	@GetMapping("/user")
	public Result<List<RbacAdminService.RbacUserView>> listUsers() {
		return Result.succeed(this.rbacAdminService.listUsers());
	}

	@PostMapping("/user")
	public Result<RbacAdminService.RbacUserView> createUser(@RequestBody RbacAdminService.CreateUserRequest request) {
		return Result.succeed(this.rbacAdminService.createUser(request));
	}

	@GetMapping("/role")
	public Result<List<RbacAdminService.RbacRoleView>> listRoles() {
		return Result.succeed(this.rbacAdminService.listRoles());
	}

	@PostMapping("/role")
	public Result<RbacAdminService.RbacRoleView> createRole(@RequestBody RbacAdminService.CreateRoleRequest request) {
		return Result.succeed(this.rbacAdminService.createRole(request));
	}

	@GetMapping("/permission")
	public Result<List<RbacAdminService.RbacPermissionView>> listPermissions() {
		return Result.succeed(this.rbacAdminService.listPermissions());
	}

	@PostMapping("/permission")
	public Result<RbacAdminService.RbacPermissionView> createPermission(
			@RequestBody RbacAdminService.CreatePermissionRequest request) {
		return Result.succeed(this.rbacAdminService.createPermission(request));
	}

	@PutMapping("/user/{userId}/role/{roleId}")
	public Result<Void> grantRole(@PathVariable Long userId, @PathVariable Long roleId) {
		this.rbacAdminService.grantRole(userId, roleId);
		return Result.succeed();
	}

	@PutMapping("/role/{roleId}/permission/{permissionId}")
	public Result<Void> grantPermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
		this.rbacAdminService.grantPermission(roleId, permissionId);
		return Result.succeed();
	}

	@GetMapping("/user/{userId}/grant")
	public Result<RbacAdminService.RbacUserGrantView> getUserGrants(@PathVariable Long userId) {
		return Result.succeed(this.rbacAdminService.getUserGrants(userId));
	}

}
