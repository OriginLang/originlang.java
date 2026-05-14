package com.originlang.enums;

public enum BoolEnum {

	/**
	 * false
	 */
	F(0, "false"),
	/**
	 * true
	 */
	T(1, "true"),;

	private final int value;

	private final String description;

	BoolEnum(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public static BoolEnum getByValue(int value) {
		for (BoolEnum e : values()) {
			if (e.getValue() == value) {
				return e;
			}
		}
		return null;
	}

	public int getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}

}
