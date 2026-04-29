package com.originlang.minio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;
import java.io.Serializable;

/**
 * 对象存储OSS（Object Storage Service）
 */
@SuppressWarnings("unused")
public class FileOss implements Serializable {

	@Serial
	private static final long serialVersionUID = -177138292191863932L;

	@JsonIgnore
	private static final ObjectMapper objectMapper = new ObjectMapper();

	// 原始文件名
	private String filename;

	// 桶
	private String bucket;

	// 对象名
	private String object;

	// 文件外链 ,此字段不序列化
	// @JsonIgnore
	private String url;

	// @JsonIgnore
	public static String toJsonStr(FileOss fileInfo) {
		if (fileInfo == null) {
			return null;
		}
		try {
			objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			return objectMapper.writeValueAsString(fileInfo);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@JsonIgnore
	public static FileOss toObject(String jsonStr) {
		if (jsonStr == null) {
			return null;
		}
		try {
			objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			return objectMapper.readValue(jsonStr, FileOss.class);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFilename() {
		return filename;
	}

	public String getBucket() {
		return bucket;
	}

	public String getObject() {
		return object;
	}

	public String getUrl() {
		return url;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "FileInfo{" + "filename='" + filename + '\'' + ", bucket='" + bucket + '\'' + ", object='" + object
				+ '\'' + ", url='" + url + '\'' + '}';
	}

}
