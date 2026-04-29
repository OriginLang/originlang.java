package com.originlang.minio;

@SuppressWarnings("unused")
public enum Bucket {

	image("image", "图片");

	private String value;

	private String description;

	Bucket(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
