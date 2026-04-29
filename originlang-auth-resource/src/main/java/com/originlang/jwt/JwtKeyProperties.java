package com.originlang.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "originlang.jwt")
public class JwtKeyProperties {

	private String rsaPublicKey;

	private String rsaPrivateKey;

	public String getRsaPublicKey() {
		return this.rsaPublicKey;
	}

	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}

	public String getRsaPrivateKey() {
		return this.rsaPrivateKey;
	}

	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

}
