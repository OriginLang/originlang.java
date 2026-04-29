package com.originlang.webmvc.servlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 映射favicon.ico - 优先从static目录查找
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/static/doc/");

		// 添加默认的静态资源映射（确保覆盖所有静态资源位置）
		// registry.addResourceHandler("/**")
		// .addResourceLocations(
		// "classpath:/META-INF/resources/",
		// "classpath:/resources/",
		// "classpath:/static/",
		// "classpath:/public/",
		// "file:" + FileConfig.getProfile() + "/" // 添加项目static根目录
		// );

		// 可选：添加缓存控制（生产环境建议开启）
		// registry.addResourceHandler("/**")
		// .addResourceLocations("classpath:/static/")
		// .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
	}

}
