package com.originlang.jwt;

import com.originlang.base.exception.Auth401Exception;
import com.originlang.resourceserver.AuthUtil;
import com.originlang.resourceserver.AuthedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Jwt {

	private static final Base64.Encoder base64Encoder = Base64.getEncoder();

	private static final Base64.Decoder base64Decoder = Base64.getDecoder();

	private static final String sha512 = "njJAI4smsWEuvdIavCRixXGU4GIGnApmxamNKB1O403GOzvLznmbQwCk4Wc2i3hz97c+YMZ/HcjEfNoI8lPRww==";

	private static final String algorithm = "HmacSHA512";

	private String id;

	private String issuer;

	private String subject;

	private String audience;

	private static String hs512() {
		SecretKey secretKey = Jwts.SIG.HS512.key().build();
		return base64Encoder.encodeToString(secretKey.getEncoded());
	}

	public static String token(String id, String subject, Long expiration, AuthedUser authedUser) {
		byte[] decodedKey = base64Decoder.decode(sha512);
		SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);

		String authorities = authedUser.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		return Jwts.builder()
			.signWith(secretKey)
			.id(id)
			.subject(subject)
			.claim(AuthUtil.USERID_KEY, authedUser.getUserId())
			.claim(AuthUtil.USERNAME, authedUser.getUsername())
			.claim(AuthUtil.TENANT_ID, authedUser.getTenantId())
			.claim(AuthUtil.CLIENT_NAME, authedUser.getClientName())
			.claim(AuthUtil.AUTHORITIES_KEY, authorities)
			.expiration(new Date(System.currentTimeMillis() + expiration))
			.compact();
	}

	public static AuthedUser parse(String token) {
		byte[] decodedKey = base64Decoder.decode(sha512);
		SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);

		Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

		final Long userId = claims.get(AuthUtil.USERID_KEY, Long.class);
		final Long tenantId = claims.get(AuthUtil.TENANT_ID, Long.class);
		final String clientName = claims.get(AuthUtil.CLIENT_NAME, String.class);
		final String authorities = claims.get(AuthUtil.AUTHORITIES_KEY, String.class);
		if (userId == null) {
			throw new Auth401Exception("not authed");
		}
		Set<GrantedAuthority> auth = new HashSet<>(AuthorityUtils.createAuthorityList(authorities.split(",")));
		return new AuthedUser(userId, tenantId, auth, clientName);
	}

	static void main() {
		// System.out.println(hs512());
		Set<GrantedAuthority> auth = new HashSet<>(AuthorityUtils.createAuthorityList("a,b,c,d"));
		AuthedUser authedUser = new AuthedUser(1L, 1L, auth, "test");

		String token = token("123", "1", 60000L, authedUser);
		System.out.println(token);
		AuthedUser parsed = parse(token);
		System.out.println(parsed.getClientName());
	}

}
