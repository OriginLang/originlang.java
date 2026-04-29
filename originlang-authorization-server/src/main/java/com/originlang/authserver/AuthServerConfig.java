package com.originlang.authserver;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

@Configuration
// @EnableWebSecurity
public class AuthServerConfig {

	// @Bean
	// @Order(1)
	// public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity
	// http)
	// throws Exception {
	// OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
	// OAuth2AuthorizationServerConfigurer.authorizationServer();
	//
	// http
	// .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
	// .with(authorizationServerConfigurer, (authorizationServer) ->
	// authorizationServer
	// .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
	// )
	// .authorizeHttpRequests((authorize) ->
	// authorize
	// .anyRequest().authenticated()
	// )
	// // Redirect to the login page when not authenticated from the
	// // authorization endpoint
	// .exceptionHandling((exceptions) -> exceptions
	// .defaultAuthenticationEntryPointFor(
	// new LoginUrlAuthenticationEntryPoint("/login"),
	// new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
	// )
	// )
	//
	// ;
	//
	// return http.build();
	// }

	// @Bean
	// @Order(2)
	// public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
	// throws Exception {
	// http
	// .authorizeHttpRequests((authorize) -> authorize
	// .anyRequest().authenticated()
	// )
	// // Form login handles the redirect to the login page from the
	// // authorization server filter chain
	// .formLogin(Customizer.withDefaults());
	//
	// return http.build();
	// }

	// @Bean
	// public UserDetailsService userDetailsService() {
	// UserDetails userDetails = Account.withDefaultPasswordEncoder()
	// .username("user")
	// .password("password")
	// .roles("USER")
	// .build();
	//
	// return new InMemoryUserDetailsManager(userDetails);
	// }

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
			.clientId("oidc-client")
			.clientSecret("{noop}secret")
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
			.postLogoutRedirectUri("http://127.0.0.1:8080/")
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
			.build();

		return new InMemoryRegisteredClientRepository(oidcClient);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

}
