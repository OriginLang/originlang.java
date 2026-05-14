package com.originlang.admin.rbac;

import com.originlang.admin.permission.SysPermission;
import com.originlang.admin.permission.SysPermissionRepository;
import com.originlang.admin.role.SysRole;
import com.originlang.admin.role.SysRoleRepository;
import com.originlang.admin.rolepermission.SysRolePermissionRef;
import com.originlang.admin.rolepermission.SysRolePermissionRefRepository;
import com.originlang.admin.user.SysUser;
import com.originlang.admin.user.SysUserRepository;
import com.originlang.admin.userrole.SysUserRole;
import com.originlang.admin.userrole.SysUserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReactiveRbacAdminServiceTest {

	private final SysUserRepository sysUserRepository = mock(SysUserRepository.class);

	private final SysRoleRepository sysRoleRepository = mock(SysRoleRepository.class);

	private final SysPermissionRepository sysPermissionRepository = mock(SysPermissionRepository.class);

	private final SysUserRoleRepository sysUserRoleRepository = mock(SysUserRoleRepository.class);

	private final SysRolePermissionRefRepository sysRolePermissionRefRepository = mock(
			SysRolePermissionRefRepository.class);

	private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

	private final ReactiveRbacAdminService service = new ReactiveRbacAdminService(this.sysUserRepository,
			this.sysRoleRepository, this.sysPermissionRepository, this.sysUserRoleRepository,
			this.sysRolePermissionRefRepository, Optional.of(this.passwordEncoder));

	@Test
	void createUserEncodesPasswordAndReturnsView() {
		when(this.sysUserRepository.existsByUsername("alice")).thenReturn(Mono.just(false));
		when(this.passwordEncoder.encode("secret")).thenReturn("encoded-secret");
		when(this.sysUserRepository.save(any(SysUser.class))).thenAnswer(invocation -> {
			SysUser user = invocation.getArgument(0);
			user.setId(11L);
			assertEquals("encoded-secret", user.getPassword());
			return Mono.just(user);
		});

		StepVerifier
			.create(this.service
				.createUser(new ReactiveRbacAdminService.CreateUserRequest("alice", "secret", "Alice", true)))
			.assertNext(view -> {
				assertEquals(11L, view.id());
				assertEquals("alice", view.username());
				assertEquals("Alice", view.displayName());
			})
			.verifyComplete();
	}

	@Test
	void createUserRejectsDuplicateUsername() {
		when(this.sysUserRepository.existsByUsername("alice")).thenReturn(Mono.just(true));

		StepVerifier
			.create(this.service
				.createUser(new ReactiveRbacAdminService.CreateUserRequest("alice", "secret", "Alice", true)))
			.expectErrorMatches(error -> error instanceof IllegalArgumentException
					&& error.getMessage().equals("username already exists: alice"))
			.verify();
		verify(this.sysUserRepository, never()).save(any());
	}

	@Test
	void grantRoleCreatesReferenceOnlyWhenMissing() {
		when(this.sysUserRepository.findById(1L)).thenReturn(Mono.just(user(1L, "alice")));
		when(this.sysRoleRepository.findById(2L)).thenReturn(Mono.just(role(2L, "admin")));
		when(this.sysUserRoleRepository.existsByUserIdAndRoleId(1L, 2L)).thenReturn(Mono.just(false));
		when(this.sysUserRoleRepository.save(any(SysUserRole.class)))
			.thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

		StepVerifier.create(this.service.grantRole(1L, 2L)).verifyComplete();

		verify(this.sysUserRoleRepository).save(any(SysUserRole.class));
	}

	@Test
	void getUserGrantsAggregatesRolesAndDistinctPermissions() {
		when(this.sysUserRepository.findById(1L)).thenReturn(Mono.just(user(1L, "alice")));
		when(this.sysUserRoleRepository.findAllByUserId(1L)).thenReturn(Flux.just(userRole(1L, 2L), userRole(1L, 3L)));
		when(this.sysRoleRepository.findAllById(List.of(2L, 3L)))
			.thenReturn(Flux.just(role(2L, "admin"), role(3L, "auditor")));
		when(this.sysRolePermissionRefRepository.findAllByRoleIdIn(List.of(2L, 3L)))
			.thenReturn(Flux.just(rolePermission(2L, 4L), rolePermission(3L, 4L), rolePermission(3L, 5L)));
		when(this.sysPermissionRepository.findAllById(List.of(4L, 5L)))
			.thenReturn(Flux.just(permission(4L, "user:read"), permission(5L, "user:write")));

		StepVerifier.create(this.service.getUserGrants(1L)).assertNext(grants -> {
			assertEquals("alice", grants.user().username());
			assertEquals(List.of("admin", "auditor"),
					grants.roles().stream().map(ReactiveRbacAdminService.RbacRoleView::code).toList());
			assertEquals(List.of("user:read", "user:write"),
					grants.permissions().stream().map(ReactiveRbacAdminService.RbacPermissionView::code).toList());
		}).verifyComplete();
	}

	private static SysUser user(Long id, String username) {
		SysUser user = new SysUser();
		user.setId(id);
		user.setUsername(username);
		user.setDisplayName(username);
		user.setEnabled(true);
		return user;
	}

	private static SysRole role(Long id, String code) {
		SysRole role = new SysRole();
		role.setId(id);
		role.setCode(code);
		role.setName(code);
		return role;
	}

	private static SysPermission permission(Long id, String code) {
		SysPermission permission = new SysPermission();
		permission.setId(id);
		permission.setCode(code);
		permission.setName(code);
		return permission;
	}

	private static SysUserRole userRole(Long userId, Long roleId) {
		SysUserRole ref = new SysUserRole();
		ref.setUserId(userId);
		ref.setRoleId(roleId);
		return ref;
	}

	private static SysRolePermissionRef rolePermission(Long roleId, Long permissionId) {
		SysRolePermissionRef ref = new SysRolePermissionRef();
		ref.setRoleId(roleId);
		ref.setPermissionId(permissionId);
		return ref;
	}

}
