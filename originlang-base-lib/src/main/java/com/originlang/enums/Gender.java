package com.originlang.enums;

public enum Gender {

	/**
	 * 未填写或未知
	 */
	UNKNOWN(0, "unknown"),

	MALE(1, "male"),

	FEMALE(2, "female");
	;

	private final int value;

	private final String description;

	Gender(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

}
