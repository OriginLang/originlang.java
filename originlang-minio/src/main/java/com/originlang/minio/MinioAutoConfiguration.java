package com.originlang.minio;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "minio.enable", havingValue = "true")
// @Configuration的替代方案
@Configuration
@EnableConfigurationProperties({ MinioConfigurationProperty.class })
public class MinioAutoConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MinioAutoConfiguration.class);

	@Autowired
	MinioConfigurationProperty minioConfigurationProperty;

	/**
	 * minio 客户端
	 * @return MinioClient
	 */

	@Bean
	public MinioClient minioClient() {
		final MinioClient minioClient = MinioClient.builder()
			// minio 是否使用https ?
			.endpoint(minioConfigurationProperty.getHost(), minioConfigurationProperty.getPort(),
					minioConfigurationProperty.getMinioSecure() != null && minioConfigurationProperty.getMinioSecure())
			// 密码
			.credentials(minioConfigurationProperty.getMinioAccessKey(), minioConfigurationProperty.getMinioSecretKey())
			.build();
		log.info("------------------------");
		log.info("-------------------------");
		log.info("========初始化minio client========");
		log.info("Host:" + minioConfigurationProperty.getHost());
		log.info("-----------------------");
		log.info("--------------------------");
		return minioClient;
	}

}
