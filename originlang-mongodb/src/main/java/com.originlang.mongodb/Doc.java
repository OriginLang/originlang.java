package com.originlang.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Document
public class Doc implements Serializable {

	@Serial
	private static final long serialVersionUID = -2736278619183769282L;

	@Id
	@MongoId
	private Long id;

	/**
	 * 应用id
	 */
	private Long appInfoId;

	/**
	 * 应用数据集id
	 */
	private Long appDataSetId;

	/**
	 * 二维码
	 */
	private String qrCodeUrl;

	/**
	 * 数据
	 */

	private Map<String, Object> data;

}
