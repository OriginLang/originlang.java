package com.originlang.jwt;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.StringUtils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Configuration
public class JwtConfig {

	private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

	private final JwtKeyProperties jwtKeyProperties;

	public JwtConfig(JwtKeyProperties jwtKeyProperties) {
		this.jwtKeyProperties = jwtKeyProperties;
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = resolveRsaKey();
		return new ImmutableJWKSet<>(new JWKSet(rsaKey));
	}

	RSAKey resolveRsaKey() {
		String publicKey = this.jwtKeyProperties.getRsaPublicKey();
		String privateKey = this.jwtKeyProperties.getRsaPrivateKey();
		if (StringUtils.hasText(publicKey) || StringUtils.hasText(privateKey)) {
			if (!StringUtils.hasText(publicKey) || !StringUtils.hasText(privateKey)) {
				throw new IllegalStateException(
						"Both originlang.jwt.rsa-public-key and originlang.jwt.rsa-private-key must be configured together");
			}
			RSAPrivateKey rsaPrivateKey = restoreRSAPrivateKey(privateKey);
			RSAPublicKey rsaPublicKey = restoreRSAPublicKey(publicKey);
			return new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).keyID("configured-jwt-key").build();
		}

		KeyPair generatedKeyPair = generateRsaKey();
		log.warn("No originlang.jwt RSA key pair configured; generating an ephemeral key pair for this process");
		return new RSAKey.Builder((RSAPublicKey) generatedKeyPair.getPublic())
			.privateKey((RSAPrivateKey) generatedKeyPair.getPrivate())
			.keyID(UUID.randomUUID().toString())
			.build();
	}

	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();

		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	// 从 Base64 字符串恢复公钥
	public static RSAPublicKey restoreRSAPublicKey(String base64PublicKey) {
		try {
			byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 从 Base64 字符串恢复私钥
	public static RSAPrivateKey restoreRSAPrivateKey(String base64PrivateKey) {
		try {
			byte[] privateKeyBytes = Base64.getDecoder().decode(base64PrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		// return new NimbusJwtDecoder(jwkSource);
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

}
