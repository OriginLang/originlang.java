package com.originlang.jwt;

import com.originlang.resourceserver.AuthUtil;
import com.originlang.resourceserver.AuthedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * token工具类
 *
 */
@Component
public class TokenProvider implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

	public static final String TOKEN_HEADER = "Authorization";

	public static final String TOKEN_PREFIX = "Bearer ";

	/**
	 * access token 过期时间 单位毫秒,1day
	 */
	private static final long one_day = 60 * 60 * 24 * 1000L;

	/**
	 * refresh token 过期时间 单位毫秒,30day
	 */
	private static final long one_month = 60 * 60 * 24 * 30 * 1000L;

	/**
	 * 应用名称
	 */
	@Value("${spring.application.name}")
	private String applicationName;

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private JwtDecoder jwtDecoder;

	@Override
	public void afterPropertiesSet() {
		// SecretKey secretKey = decodeSecretKey(sha512, "HmacSHA512");

	}

	private static String encodeToString(SecretKey secretKey) {
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	private static SecretKey decodeSecretKey(String base64Key, String algorithm) {
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
	}

	public TokenPair token(AuthedUser authedUser) {
		String authorities = authedUser.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		Map<String, Object> claims = new HashMap<>();
		claims.put(AuthUtil.AUTHORITIES_KEY, authorities);
		String accessToken = joseToken(authedUser.getUserId().toString(), authedUser.getUsername(), claims, one_day);
		String refreshToken = joseToken(authedUser.getUserId().toString(), authedUser.getUsername(), claims, one_month);
		return new TokenPair(accessToken, refreshToken);
	}

	public TokenPair token(Authentication authentication) {
		AuthedUser authedUser = AuthUtil.currentUser(authentication);
		return token(authedUser);
	}

	public AuthedUser parseToken(String token) {
		return joseTokenParse(token);
	}

	private String joseToken(String id, String subject, Map<String, Object> claimsContent, Long expiresAt) {
		JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
			.issuer("https://auth-server") // jwt signer
			.id(id)
			.subject(subject) // JWT所面向的用戶
			.audience(List.of(applicationName)) // 接收JWT的一方
			.issuedAt(Instant.now()) // jwt signer time
			.expiresAt(Instant.now().plusSeconds(expiresAt));

		for (Map.Entry<String, Object> entry : claimsContent.entrySet()) {
			builder.claim(entry.getKey(), entry.getValue());
		}
		JwtClaimsSet claimsSet = builder.build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
	}

	private AuthedUser joseTokenParse(String token) {
		Jwt jwt = jwtDecoder.decode(token);
		final Long userId = Long.valueOf(jwt.getId());
		final String subject = jwt.getSubject();
		Map<String, Object> claims = jwt.getClaims();

		final String authorities = (String) claims.get(AuthUtil.AUTHORITIES_KEY);
		final Set<GrantedAuthority> authoritySet = Stream.of(authorities.split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toSet());

		Long tenantId = null;
		Object tenantIdClaim = claims.get(AuthUtil.TENANT_ID);
		if (tenantIdClaim instanceof Long tenantIdTemp) {
			tenantId = tenantIdTemp;
		}

		return new AuthedUser(userId, tenantId, authoritySet, applicationName);
	}

}
