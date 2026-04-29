package com.originlang.minio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public class MinioConfigurationProperty {

	@Value("${minio.host}")
	private String host;

	@Value("${minio.port}")
	private Integer port;

	@Value("${minio.secure}")
	private Boolean minioSecure;

	@Value("${minio.access-key}")
	private String minioAccessKey;

	@Value("${minio.secret-key}")
	private String minioSecretKey;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getMinioSecure() {
		return minioSecure;
	}

	public void setMinioSecure(Boolean minioSecure) {
		this.minioSecure = minioSecure;
	}

	public String getMinioAccessKey() {
		return minioAccessKey;
	}

	public void setMinioAccessKey(String minioAccessKey) {
		this.minioAccessKey = minioAccessKey;
	}

	public String getMinioSecretKey() {
		return minioSecretKey;
	}

	public void setMinioSecretKey(String minioSecretKey) {
		this.minioSecretKey = minioSecretKey;
	}

}
