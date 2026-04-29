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
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional

public class RbacAdminService {

	@Resource
	private SysUserRepository sysUserRepository;

	@Resource
	private SysRoleRepository sysRoleRepository;

	@Resource
	private SysPermissionRepository sysPermissionRepository;

	@Resource
	private SysUserRoleRefRepository sysUserRoleRefRepository;

	@Resource
	private SysRolePermissionRefRepository sysRolePermissionRefRepository;

	@Resource
	private Optional<PasswordEncoder> passwordEncoder;

	public List<RbacUserView> listUsers() {
		return this.sysUserRepository.findAll().stream().map(RbacUserView::from).toList();
	}

	public RbacUserView createUser(CreateUserRequest request) {
		if (this.sysUserRepository.countByUsername(request.username()) > 0) {
			throw new IllegalArgumentException("username already exists: " + request.username());
		}
		SysUser sysUser = new SysUser();
		sysUser.setUsername(request.username());
		sysUser.setDisplayName(request.displayName());
		sysUser.setEnabled(request.enabled());
		sysUser.setPassword(encodePassword(request.password()));
		return RbacUserView.from(this.sysUserRepository.save(sysUser));
	}

	public List<RbacRoleView> listRoles() {
		return this.sysRoleRepository.findAll().stream().map(RbacRoleView::from).toList();
	}

	public RbacRoleView createRole(CreateRoleRequest request) {
		if (this.sysRoleRepository.countByCode(request.code()) > 0) {
			throw new IllegalArgumentException("role code already exists: " + request.code());
		}
		SysRole sysRole = new SysRole();
		sysRole.setCode(request.code());
		sysRole.setName(request.name());
		return RbacRoleView.from(this.sysRoleRepository.save(sysRole));
	}

	public List<RbacPermissionView> listPermissions() {
		return this.sysPermissionRepository.findAll().stream().map(RbacPermissionView::from).toList();
	}

	public RbacPermissionView createPermission(CreatePermissionRequest request) {
		if (this.sysPermissionRepository.countByCode(request.code()) > 0) {
			throw new IllegalArgumentException("permission code already exists: " + request.code());
		}
		SysPermission sysPermission = new SysPermission();
		sysPermission.setCode(request.code());
		sysPermission.setName(request.name());
		return RbacPermissionView.from(this.sysPermissionRepository.save(sysPermission));
	}

	public void grantRole(Long userId, Long roleId) {
		requireUser(userId);
		requireRole(roleId);
		if (this.sysUserRoleRefRepository.countByUserIdAndRoleId(userId, roleId) == 0) {
			SysUserRoleRef sysUserRoleRef = new SysUserRoleRef();
			sysUserRoleRef.setUserId(userId);
			sysUserRoleRef.setRoleId(roleId);
			this.sysUserRoleRefRepository.save(sysUserRoleRef);
		}
	}

	public void grantPermission(Long roleId, Long permissionId) {
		requireRole(roleId);
		requirePermission(permissionId);
		if (this.sysRolePermissionRefRepository.countByRoleIdAndPermissionId(roleId, permissionId) == 0) {
			SysRolePermissionRef sysRolePermissionRef = new SysRolePermissionRef();
			sysRolePermissionRef.setRoleId(roleId);
			sysRolePermissionRef.setPermissionId(permissionId);
			this.sysRolePermissionRefRepository.save(sysRolePermissionRef);
		}
	}

	public RbacUserGrantView getUserGrants(Long userId) {
		SysUser sysUser = requireUser(userId);
		List<Long> roleIds = this.sysUserRoleRefRepository.findAllByUserId(userId)
			.stream()
			.map(SysUserRoleRef::getRoleId)
			.distinct()
			.toList();
		List<RbacRoleView> roles = roleIds.isEmpty() ? List.of()
				: this.sysRoleRepository.findByIdIn(roleIds).stream().map(RbacRoleView::from).toList();
		Set<Long> permissionIds = new LinkedHashSet<>(this.sysRolePermissionRefRepository.findAllByRoleIdIn(roleIds)
			.stream()
			.map(SysRolePermissionRef::getPermissionId)
			.toList());
		List<RbacPermissionView> permissions = permissionIds.isEmpty() ? List.of()
				: this.sysPermissionRepository.findByIdIn(permissionIds.stream().toList())
					.stream()
					.map(RbacPermissionView::from)
					.toList();
		return new RbacUserGrantView(RbacUserView.from(sysUser), roles, permissions);
	}

	private SysUser requireUser(Long userId) {
		return this.sysUserRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("user not found: " + userId));
	}

	private SysRole requireRole(Long roleId) {
		return this.sysRoleRepository.findById(roleId)
			.orElseThrow(() -> new EntityNotFoundException("role not found: " + roleId));
	}

	private SysPermission requirePermission(Long permissionId) {
		return this.sysPermissionRepository.findById(permissionId)
			.orElseThrow(() -> new EntityNotFoundException("permission not found: " + permissionId));
	}

	private String encodePassword(String password) {
		return this.passwordEncoder.map(encoder -> encoder.encode(password)).orElse(password);
	}

	public record CreateUserRequest(String username, String password, String displayName, boolean enabled) {
	}

	public record CreateRoleRequest(String code, String name) {
	}

	public record CreatePermissionRequest(String code, String name) {
	}

	public record RbacUserView(Long id, String username, String displayName, boolean enabled) {

		static RbacUserView from(SysUser sysUser) {
			return new RbacUserView(sysUser.getId(), sysUser.getUsername(), sysUser.getDisplayName(),
					sysUser.isEnabled());
		}

	}

	public record RbacRoleView(Long id, String code, String name) {

		static RbacRoleView from(SysRole sysRole) {
			return new RbacRoleView(sysRole.getId(), sysRole.getCode(), sysRole.getName());
		}

	}

	public record RbacPermissionView(Long id, String code, String name) {

		static RbacPermissionView from(SysPermission sysPermission) {
			return new RbacPermissionView(sysPermission.getId(), sysPermission.getCode(), sysPermission.getName());
		}

	}

	public record RbacUserGrantView(RbacUserView user, List<RbacRoleView> roles, List<RbacPermissionView> permissions) {
	}

}
