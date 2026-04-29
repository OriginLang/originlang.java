package com.originlang.webmvc.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
public class ServletWebConfig {

	private static final Logger log = LoggerFactory.getLogger(ServletWebConfig.class);

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		// log header
		loggingFilter.setIncludeHeaders(true);
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		loggingFilter.setMaxPayloadLength(2000);
		return loggingFilter;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	public CorsFilter corsWebFilter() {

		CorsConfiguration config = new CorsConfiguration();
		// 这里仅为了说明问题，配置为放行所有域名，生产环境请对此进行修改
		// config.addAllowedOrigin("*");
		List<String> pattern = new ArrayList<>();
		pattern.add("*");
		config.setAllowedOriginPatterns(pattern);
		// 放行的请求头
		config.addAllowedHeader(CorsConfiguration.ALL);
		// 放行的请求方式，主要有：GET, POST, PUT, DELETE, OPTIONS
		config.addAllowedMethod(CorsConfiguration.ALL);
		// 暴露头部信息
		config.addExposedHeader("*");
		// 是否发送cookie
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
