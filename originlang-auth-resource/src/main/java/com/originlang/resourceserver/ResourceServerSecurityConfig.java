package com.originlang.resourceserver;

import com.originlang.base.exception.Auth401Exception;
import com.originlang.jwt.TokenProvider;
import com.originlang.webmvc.servlet.AnonymousUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
public class ResourceServerSecurityConfig {

	private static final Logger log = LoggerFactory.getLogger(ResourceServerSecurityConfig.class);

	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private TokenProvider tokenProvider;

	@Value("${spring.application.name}")
	private String clientName;

	// request.requestMatchers must not same
	// @Order(0)
	// @Bean(name = "s1")
	// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	// {
	// http
	// .authorizeHttpRequests(request -> {
	// request.requestMatchers("/**/*.html").permitAll() // 允许访问所有 HTML 页面
	// // 其他静态资源（如 js, css）也放行
	// .requestMatchers("/**/*.js", "/**/*.css", "/images/**").permitAll();
	// })
	//
	// .httpBasic(AbstractHttpConfigurer::disable); // jwt not use HTTP Basic 验证
	// return http.build();
	// }

	@Order(1)
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) {
		return http
			// 关闭默认端点
			.formLogin(AbstractHttpConfigurer::disable)
			// CORS 必须在 Spring Security 之前处理，因为预检请求不包含任何 cookie（即，即请求中没有 cookie
			// JSESSIONID）。
			// 如果请求不包含任何 cookie 且 Spring Security 优先处理，则 Spring Security
			// 会判定用户未通过身份验证（因为请求中没有 cookie），并拒绝该请求。
			// if Spring MVC is on classpath and no CorsConfigurationSource is provided,
			// Spring Security will use CORS configuration provided to Spring MVC
			// .cors(withDefaults())
			// use token 无状态api不需要开启,启用可能会导致403
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll())
			.addFilterBefore((servletRequest, servletResponse, filterChain) -> {
				if (servletRequest instanceof HttpServletRequest request) {
					String uri = request.getRequestURI();
					String method = request.getMethod();
					log.debug("Request Method: {} URI: {}", method, uri);
					if (method.equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
						log.info("OPTIONS Request ");
						filterChain.doFilter(request, servletResponse);
						return;
					}
					if (AnonymousUtils.isAnonymous(uri, method) || uri.endsWith(".html") || uri.endsWith(".js")
							|| uri.endsWith(".css")) {
						log.info("anonymous url ");
						filterChain.doFilter(request, servletResponse);
						return;
					}

					try {
						String token = request.getHeader(AuthUtil.AUTH);
						AuthedUser authedUser = null;
						// 去掉token前缀
						if (token == null) {
							throw new Auth401Exception("token is null");
						}
						else {
							token = token.replace(AuthUtil.BEARER, "");
						}
						log.info("携带了token,解析token");
						authedUser = tokenProvider.parseToken(token);

						if (authedUser != null) {
							log.info("校验token是否是当前端");
							if (!Objects.equals(authedUser.getClientName(), clientName)) {
								log.error("当前用户id={}, 尝试跨后端访问", authedUser.getUserId());
								throw new Auth401Exception("无法访问,token不匹配");
							}
							// log.info("校验用户当前状态");
							// AuthedUser status =
							// userStatusService.getAuthedUser(authedUser.getUserId());
							// if (Objects.equals(0, status.getAccountStatus())) {
							// log.warn("当前用户被禁用,id={}", status.getUserId());
							// throw new NotAuth401Exception("当前用户已被禁用");
							// }

							UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
									authedUser, authedUser.getPassword(), authedUser.getAuthorities());
							authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							// 允许子线程获取用户信息
							SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
							SecurityContextHolder.getContext().setAuthentication(authToken);
						}
					}
					catch (Exception e) {
						log.error("token error:  ", e);
						servletResponse.setContentType("application/json;charset=utf-8");
						Map<String, Object> res = new HashMap<>(4);
						res.put("success", false);
						res.put("code", 401);
						res.put("msg", "token validation failed");
						res.put("data", null);
						servletResponse.getWriter().write(objectMapper.writeValueAsString(res));
						return;
					}
				}
				filterChain.doFilter(servletRequest, servletResponse);
			}, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}
