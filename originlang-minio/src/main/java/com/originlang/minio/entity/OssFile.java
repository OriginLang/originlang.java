package com.originlang.minio.entity;

import com.originlang.data.base.BaseIdEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OssFile extends BaseIdEntity {

	/**
	 * 原始文件名
	 */
	private String originalFilename;

	/**
	 * bucket
	 */
	private String bucket;

	/**
	 * objectId
	 */
	private String objectId;

	/**
	 * 文件类型
	 */
	private String fileType;

}
