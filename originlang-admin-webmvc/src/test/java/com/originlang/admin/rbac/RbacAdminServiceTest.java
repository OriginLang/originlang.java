package com.originlang.admin.rbac;

import com.originlang.admin.permission.SysPermission;
import com.originlang.admin.permission.SysPermissionRepository;
import com.originlang.admin.role.SysRole;
import com.originlang.admin.role.SysRoleRepository;
import com.originlang.admin.rolepermission.SysRolePermissionRef;
import com.originlang.admin.rolepermission.SysRolePermissionRefRepository;
import com.originlang.admin.user.SysUser;
import com.originlang.admin.user.SysUserRepository;
import com.originlang.admin.userrole.SysUserRoleRef;
import com.originlang.admin.userrole.SysUserRoleRefRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RbacAdminServiceTest {

	private final SysUserRepository sysUserRepository = mock(SysUserRepository.class);

	private final SysRoleRepository sysRoleRepository = mock(SysRoleRepository.class);

	private final SysPermissionRepository sysPermissionRepository = mock(SysPermissionRepository.class);

	private final SysUserRoleRefRepository sysUserRoleRefRepository = mock(SysUserRoleRefRepository.class);

	private final SysRolePermissionRefRepository sysRolePermissionRefRepository = mock(
			SysRolePermissionRefRepository.class);

	private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

	private final RbacAdminService service = new RbacAdminService();

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(this.service, "sysUserRepository", this.sysUserRepository);
		ReflectionTestUtils.setField(this.service, "sysRoleRepository", this.sysRoleRepository);
		ReflectionTestUtils.setField(this.service, "sysPermissionRepository", this.sysPermissionRepository);
		ReflectionTestUtils.setField(this.service, "sysUserRoleRefRepository", this.sysUserRoleRefRepository);
		ReflectionTestUtils.setField(this.service, "sysRolePermissionRefRepository",
				this.sysRolePermissionRefRepository);
		ReflectionTestUtils.setField(this.service, "passwordEncoder", Optional.of(this.passwordEncoder));
	}

	@Test
	void createUserEncodesPasswordAndReturnsView() {
		when(this.sysUserRepository.countByUsername("alice")).thenReturn(0L);
		when(this.passwordEncoder.encode("secret")).thenReturn("encoded-secret");
		when(this.sysUserRepository.save(any(SysUser.class))).thenAnswer(invocation -> {
			SysUser user = invocation.getArgument(0);
			user.setId(11L);
			assertEquals("encoded-secret", user.getPassword());
			return user;
		});

		RbacAdminService.RbacUserView view = this.service
			.createUser(new RbacAdminService.CreateUserRequest("alice", "secret", "Alice", true));

		assertEquals(11L, view.id());
		assertEquals("alice", view.username());
		assertEquals("Alice", view.displayName());
	}

	@Test
	void createUserRejectsDuplicateUsername() {
		when(this.sysUserRepository.countByUsername("alice")).thenReturn(1L);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.service
			.createUser(new RbacAdminService.CreateUserRequest("alice", "secret", "Alice", true)));

		assertEquals("username already exists: alice", exception.getMessage());
		verify(this.sysUserRepository, never()).save(any());
	}

	@Test
	void grantRoleCreatesReferenceOnlyWhenMissing() {
		when(this.sysUserRepository.findById(1L)).thenReturn(Optional.of(user(1L, "alice")));
		when(this.sysRoleRepository.findById(2L)).thenReturn(Optional.of(role(2L, "admin")));
		when(this.sysUserRoleRefRepository.countByUserIdAndRoleId(1L, 2L)).thenReturn(0L);

		this.service.grantRole(1L, 2L);

		verify(this.sysUserRoleRefRepository).save(any(SysUserRoleRef.class));
	}

	@Test
	void getUserGrantsAggregatesDistinctPermissionsFromRoles() {
		when(this.sysUserRepository.findById(1L)).thenReturn(Optional.of(user(1L, "alice")));
		when(this.sysUserRoleRefRepository.findAllByUserId(1L)).thenReturn(List.of(userRole(1L, 2L), userRole(1L, 3L)));
		when(this.sysRoleRepository.findByIdIn(List.of(2L, 3L)))
			.thenReturn(List.of(role(2L, "admin"), role(3L, "auditor")));
		when(this.sysRolePermissionRefRepository.findAllByRoleIdIn(List.of(2L, 3L)))
			.thenReturn(List.of(rolePermission(2L, 4L), rolePermission(3L, 4L), rolePermission(3L, 5L)));
		when(this.sysPermissionRepository.findByIdIn(List.of(4L, 5L)))
			.thenReturn(List.of(permission(4L, "user:read"), permission(5L, "user:write")));

		RbacAdminService.RbacUserGrantView grants = this.service.getUserGrants(1L);

		assertEquals("alice", grants.user().username());
		assertEquals(List.of("admin", "auditor"),
				grants.roles().stream().map(RbacAdminService.RbacRoleView::code).toList());
		assertEquals(List.of("user:read", "user:write"),
				grants.permissions().stream().map(RbacAdminService.RbacPermissionView::code).toList());
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

	private static SysUserRoleRef userRole(Long userId, Long roleId) {
		SysUserRoleRef ref = new SysUserRoleRef();
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
