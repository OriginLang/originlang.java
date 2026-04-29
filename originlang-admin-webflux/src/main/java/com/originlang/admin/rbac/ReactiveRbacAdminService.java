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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ReactiveRbacAdminService {

	private final SysUserRepository sysUserRepository;

	private final SysRoleRepository sysRoleRepository;

	private final SysPermissionRepository sysPermissionRepository;

	private final SysUserRoleRepository sysUserRoleRepository;

	private final SysRolePermissionRefRepository sysRolePermissionRefRepository;

	private final Optional<PasswordEncoder> passwordEncoder;

	public ReactiveRbacAdminService(SysUserRepository sysUserRepository, SysRoleRepository sysRoleRepository,
			SysPermissionRepository sysPermissionRepository, SysUserRoleRepository sysUserRoleRepository,
			SysRolePermissionRefRepository sysRolePermissionRefRepository, Optional<PasswordEncoder> passwordEncoder) {
		this.sysUserRepository = sysUserRepository;
		this.sysRoleRepository = sysRoleRepository;
		this.sysPermissionRepository = sysPermissionRepository;
		this.sysUserRoleRepository = sysUserRoleRepository;
		this.sysRolePermissionRefRepository = sysRolePermissionRefRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Mono<List<RbacUserView>> listUsers() {
		return this.sysUserRepository.findAll().map(RbacUserView::from).collectList();
	}

	public Mono<RbacUserView> createUser(CreateUserRequest request) {
		return this.sysUserRepository.existsByUsername(request.username())
			.flatMap(exists -> exists
					? Mono.error(new IllegalArgumentException("username already exists: " + request.username()))
					: this.sysUserRepository.save(toUser(request)).map(RbacUserView::from));
	}

	public Mono<List<RbacRoleView>> listRoles() {
		return this.sysRoleRepository.findAll().map(RbacRoleView::from).collectList();
	}

	public Mono<RbacRoleView> createRole(CreateRoleRequest request) {
		return this.sysRoleRepository.existsByCode(request.code())
			.flatMap(exists -> exists
					? Mono.error(new IllegalArgumentException("role code already exists: " + request.code()))
					: this.sysRoleRepository.save(toRole(request)).map(RbacRoleView::from));
	}

	public Mono<List<RbacPermissionView>> listPermissions() {
		return this.sysPermissionRepository.findAll().map(RbacPermissionView::from).collectList();
	}

	public Mono<RbacPermissionView> createPermission(CreatePermissionRequest request) {
		return this.sysPermissionRepository.existsByCode(request.code())
			.flatMap(exists -> exists
					? Mono.error(new IllegalArgumentException("permission code already exists: " + request.code()))
					: this.sysPermissionRepository.save(toPermission(request)).map(RbacPermissionView::from));
	}

	public Mono<Void> grantRole(Long userId, Long roleId) {
		return Mono.when(requireUser(userId), requireRole(roleId))
			.then(this.sysUserRoleRepository.existsByUserIdAndRoleId(userId, roleId))
			.flatMap(exists -> exists ? Mono.empty()
					: this.sysUserRoleRepository.save(newUserRole(userId, roleId)).then())
			.then();
	}

	public Mono<Void> grantPermission(Long roleId, Long permissionId) {
		return Mono.when(requireRole(roleId), requirePermission(permissionId))
			.then(this.sysRolePermissionRefRepository.existsByRoleIdAndPermissionId(roleId, permissionId))
			.flatMap(exists -> exists ? Mono.empty()
					: this.sysRolePermissionRefRepository.save(newRolePermission(roleId, permissionId)).then())
			.then();
	}

	public Mono<RbacUserGrantView> getUserGrants(Long userId) {
		return requireUser(userId).flatMap(user -> this.sysUserRoleRepository.findAllByUserId(userId)
			.map(SysUserRole::getRoleId)
			.distinct()
			.collectList()
			.flatMap(roleIds -> Mono.zip(loadRoles(roleIds), loadPermissions(roleIds))
				.map(tuple -> new RbacUserGrantView(RbacUserView.from(user), tuple.getT1(), tuple.getT2()))));
	}

	private Mono<List<RbacRoleView>> loadRoles(List<Long> roleIds) {
		if (roleIds.isEmpty()) {
			return Mono.just(List.of());
		}
		return this.sysRoleRepository.findAllById(roleIds).map(RbacRoleView::from).collectList();
	}

	private Mono<List<RbacPermissionView>> loadPermissions(List<Long> roleIds) {
		if (roleIds.isEmpty()) {
			return Mono.just(List.of());
		}
		return this.sysRolePermissionRefRepository.findAllByRoleIdIn(roleIds)
			.map(SysRolePermissionRef::getPermissionId)
			.distinct()
			.collectList()
			.flatMap(permissionIds -> permissionIds.isEmpty() ? Mono.just(List.of())
					: this.sysPermissionRepository.findAllById(permissionIds)
						.map(RbacPermissionView::from)
						.collectList());
	}

	private Mono<SysUser> requireUser(Long userId) {
		return this.sysUserRepository.findById(userId)
			.switchIfEmpty(Mono.error(new IllegalArgumentException("user not found: " + userId)));
	}

	private Mono<SysRole> requireRole(Long roleId) {
		return this.sysRoleRepository.findById(roleId)
			.switchIfEmpty(Mono.error(new IllegalArgumentException("role not found: " + roleId)));
	}

	private Mono<SysPermission> requirePermission(Long permissionId) {
		return this.sysPermissionRepository.findById(permissionId)
			.switchIfEmpty(Mono.error(new IllegalArgumentException("permission not found: " + permissionId)));
	}

	private SysUser toUser(CreateUserRequest request) {
		SysUser sysUser = new SysUser();
		sysUser.setUsername(request.username());
		sysUser.setPassword(encodePassword(request.password()));
		sysUser.setDisplayName(request.displayName());
		sysUser.setEnabled(request.enabled());
		return sysUser;
	}

	private SysRole toRole(CreateRoleRequest request) {
		SysRole sysRole = new SysRole();
		sysRole.setCode(request.code());
		sysRole.setName(request.name());
		return sysRole;
	}

	private SysPermission toPermission(CreatePermissionRequest request) {
		SysPermission sysPermission = new SysPermission();
		sysPermission.setCode(request.code());
		sysPermission.setName(request.name());
		return sysPermission;
	}

	private SysUserRole newUserRole(Long userId, Long roleId) {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUserId(userId);
		sysUserRole.setRoleId(roleId);
		return sysUserRole;
	}

	private SysRolePermissionRef newRolePermission(Long roleId, Long permissionId) {
		SysRolePermissionRef sysRolePermissionRef = new SysRolePermissionRef();
		sysRolePermissionRef.setRoleId(roleId);
		sysRolePermissionRef.setPermissionId(permissionId);
		return sysRolePermissionRef;
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
					Boolean.TRUE.equals(sysUser.getEnabled()));
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
