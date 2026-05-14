package com.originlang.resourceserver;

import com.originlang.base.exception.Auth401Exception;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthUtilTest {

	@AfterEach
	void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void returnsCurrentAuthedUserFromSecurityContext() {
		AuthedUser authedUser = authedUser();
		SecurityContextHolder.getContext()
			.setAuthentication(
					new UsernamePasswordAuthenticationToken(authedUser, "credentials", authedUser.getAuthorities()));

		assertSame(authedUser, AuthUtil.currentUser());
		assertEquals(7L, AuthUtil.currentUserId());
		assertEquals(Set.copyOf(authedUser.getAuthorities()), AuthUtil.currentAuthorities());
		assertTrue(AuthUtil.currentUserOptional().isPresent());
		assertEquals(7L, AuthUtil.currentUserIdOptional().orElseThrow());
	}

	@Test
	void returnsEmptyOptionalWhenNoAuthenticationExists() {
		assertFalse(AuthUtil.currentUserOptional().isPresent());
		assertFalse(AuthUtil.currentUserIdOptional().isPresent());
	}

	@Test
	void throwsWhenAuthenticationIsMissingOrPrincipalIsNotAuthedUser() {
		assertThrows(Auth401Exception.class, () -> AuthUtil.currentUser(null));

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("anonymous",
				"credentials");

		assertThrows(Auth401Exception.class, () -> AuthUtil.currentUser(authentication));
	}

	private static AuthedUser authedUser() {
		return new AuthedUser(7L, 3L, AuthorityUtils.createAuthorityList("user:read", "user:write"), "test-client");
	}

}
