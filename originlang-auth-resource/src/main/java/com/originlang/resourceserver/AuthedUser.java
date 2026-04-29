package com.originlang.resourceserver;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 认证用户
 */

public class AuthedUser extends User {

	@Serial
	private static final long serialVersionUID = -6802606527708383428L;

	private final Long userId;

	private final Long tenantId;

	private final String clientName;

	/**
	 * authorities
	 */
	private final Set<GrantedAuthority> authorities = Collections.synchronizedSet(new HashSet<>());

	/**
	 * 构造方法
	 * @param userId 用户id
	 * @param password 密码
	 * @param enabled 是否可用
	 * @param accountNonExpired 是否过期
	 * @param credentialsNonExpired credential是否过期
	 * @param accountNonLocked 是否锁定
	 * @param authorities 权限
	 */

	public AuthedUser(Long userId, Long tenantId, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Set<GrantedAuthority> authorities,
			String clientName) {
		super("****", password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId = userId;
		this.tenantId = tenantId;
		this.clientName = clientName;
		if (!authorities.isEmpty()) {
			this.authorities.addAll(authorities);
		}

	}

	public AuthedUser(Long userId, Long tenantId, Collection<? extends GrantedAuthority> authorities,
			String clientName) {

		super("****", "*****", true, true, true, true, authorities);
		this.userId = userId;
		this.tenantId = tenantId;
		this.clientName = clientName;
		if (CollectionUtils.isNotEmpty(authorities)) {
			this.authorities.addAll(authorities);
		}
	}

	public AuthedUser(Long userId, Long tenantId, Collection<? extends GrantedAuthority> authorities, String clientName,
			Integer accountStatus) {

		super("****", "*****", 1 == accountStatus, true, true, true, authorities);
		this.userId = userId;
		this.tenantId = tenantId;
		this.clientName = clientName;
		if (CollectionUtils.isNotEmpty(authorities)) {
			this.authorities.addAll(authorities);
		}
	}

	public Long getUserId() {
		return userId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	// @Override
	// public String getUsername() {
	// return username;
	// }

	public String getClientName() {
		return clientName;
	}

	@Override
	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}

}
