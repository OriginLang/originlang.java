package com.originlang.resourceserver;

import com.originlang.base.exception.Auth401Exception;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public final class AuthUtil {

	public static final String USERID_KEY = "user_id";

	/**
	 * 租户id
	 */
	public static final String TENANT_ID = "tenant_id";

	/**
	 * 登录凭证
	 */
	public static final String USERNAME = "username";

	/**
	 * client 名称
	 */
	public static final String CLIENT_NAME = "client_name";

	/**
	 * token 前缀
	 */
	public static final String BEARER = "Bearer ";

	/**
	 * Authorization,token
	 */
	public static final String AUTH = "Authorization";

	/**
	 * authorities,权限标识
	 */
	public static final String AUTHORITIES_KEY = "authorities";

	/**
	 * 获取当前用户id
	 * @return sysUserId
	 */
	public static Long currentUserId() {
		return currentUser().getUserId();
	}

	/**
	 * 用户名，UserDetailsService中的username
	 */
	public static String currentUsername() {
		return currentUser().getUsername();
	}

	/**
	 * 当前用户authorities
	 */
	public static Set<GrantedAuthority> currentAuthorities() {
		return currentUser().getAuthorities();
	}

	/**
	 * 获取当前用户，可能为null
	 */
	public static Optional<AuthedUser> currentUserOptional() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return Optional.empty();
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof AuthedUser authedUser) {
			return Optional.of(authedUser);
		}
		return Optional.empty();
	}

	/**
	 * 获取当前用户id，可能为null
	 */
	public static Optional<Long> currentUserIdOptional() {
		return currentUserOptional().map(AuthedUser::getUserId);
	}

	/**
	 * 获取当前用户
	 */
	public static AuthedUser currentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return currentUser(authentication);
	}

	/**
	 * 获取当前用户
	 * @param authentication 认证信息
	 */
	public static AuthedUser currentUser(Authentication authentication) {
		if (authentication == null) {
			throw new Auth401Exception("not authed");
		}
		Object principal = authentication.getPrincipal();
		if (principal == null) {
			throw new Auth401Exception("not authed");
		}
		if (principal instanceof AuthedUser authedUser) {
			return authedUser;
		}
		throw new Auth401Exception("not authed");
	}

}
