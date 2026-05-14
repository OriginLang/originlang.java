package com.originlang.jwt;

import com.originlang.resourceserver.AuthedUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtTest {

	@Test
	void tokenRoundTripPreservesAuthedUserClaims() {
		AuthedUser authedUser = new AuthedUser(42L, 9L, AuthorityUtils.createAuthorityList("role:admin", "user:read"),
				"admin-console");

		String token = Jwt.token("token-id", "subject", 60_000L, authedUser);
		AuthedUser parsed = Jwt.parse(token);

		assertEquals(42L, parsed.getUserId());
		assertEquals(9L, parsed.getTenantId());
		assertEquals("admin-console", parsed.getClientName());
		assertEquals(Set.of("role:admin", "user:read"), authorityNames(parsed));
	}

	private static Set<String> authorityNames(AuthedUser user) {
		return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
	}

}
