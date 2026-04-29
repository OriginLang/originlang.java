package com.originlang.admin.rbac;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rbac")
public class ReactiveRbacAdminController {

	private final ReactiveRbacAdminService reactiveRbacAdminService;

	public ReactiveRbacAdminController(ReactiveRbacAdminService reactiveRbacAdminService) {
		this.reactiveRbacAdminService = reactiveRbacAdminService;
	}

	@GetMapping("/user")
	public Mono<Map<String, List<ReactiveRbacAdminService.RbacUserView>>> listUsers() {
		return this.reactiveRbacAdminService.listUsers().map(users -> Map.of("data", users));
	}

	@PostMapping("/user")
	public Mono<ReactiveRbacAdminService.RbacUserView> createUser(
			@RequestBody ReactiveRbacAdminService.CreateUserRequest request) {
		return this.reactiveRbacAdminService.createUser(request);
	}

	@GetMapping("/role")
	public Mono<Map<String, List<ReactiveRbacAdminService.RbacRoleView>>> listRoles() {
		return this.reactiveRbacAdminService.listRoles().map(roles -> Map.of("data", roles));
	}

	@PostMapping("/role")
	public Mono<ReactiveRbacAdminService.RbacRoleView> createRole(
			@RequestBody ReactiveRbacAdminService.CreateRoleRequest request) {
		return this.reactiveRbacAdminService.createRole(request);
	}

	@GetMapping("/permission")
	public Mono<Map<String, List<ReactiveRbacAdminService.RbacPermissionView>>> listPermissions() {
		return this.reactiveRbacAdminService.listPermissions().map(permissions -> Map.of("data", permissions));
	}

	@PostMapping("/permission")
	public Mono<ReactiveRbacAdminService.RbacPermissionView> createPermission(
			@RequestBody ReactiveRbacAdminService.CreatePermissionRequest request) {
		return this.reactiveRbacAdminService.createPermission(request);
	}

	@PutMapping("/user/{userId}/role/{roleId}")
	public Mono<Map<String, Boolean>> grantRole(@PathVariable Long userId, @PathVariable Long roleId) {
		return this.reactiveRbacAdminService.grantRole(userId, roleId).thenReturn(Map.of("success", true));
	}

	@PutMapping("/role/{roleId}/permission/{permissionId}")
	public Mono<Map<String, Boolean>> grantPermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
		return this.reactiveRbacAdminService.grantPermission(roleId, permissionId).thenReturn(Map.of("success", true));
	}

	@GetMapping("/user/{userId}/grant")
	public Mono<ReactiveRbacAdminService.RbacUserGrantView> getUserGrants(@PathVariable Long userId) {
		return this.reactiveRbacAdminService.getUserGrants(userId);
	}

}
