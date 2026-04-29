package com.originlang.base.appname;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "spring")
public class SpringCoreProperty {

	@Value("${spring.application.name}")
	private String appName;

}
