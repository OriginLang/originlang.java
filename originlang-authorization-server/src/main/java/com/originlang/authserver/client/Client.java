package com.originlang.authserver.client;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.Serial;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class Client extends RegisteredClient {

	@Serial
	private static final long serialVersionUID = -8475058842666337979L;

	@Id
	private String id;

	private String clientId;

	private Instant clientIdIssuedAt;

	private String clientSecret;

	private Instant clientSecretExpiresAt;

	private String clientName;

	private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

	private Set<AuthorizationGrantType> authorizationGrantTypes;

	private Set<String> redirectUris;

	private Set<String> postLogoutRedirectUris;

	private Set<String> scopes;

	private ClientSettings clientSettings;

	private TokenSettings tokenSettings;

}
