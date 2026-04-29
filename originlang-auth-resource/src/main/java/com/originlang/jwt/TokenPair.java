package com.originlang.jwt;

import java.io.Serial;
import java.io.Serializable;

public class TokenPair implements Serializable {

	@Serial
	private static final long serialVersionUID = 7236331768166212818L;

	/**
	 * access token
	 */
	private String accessToken;

	/**
	 * refresh token
	 */
	private String refreshToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public TokenPair(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

}
