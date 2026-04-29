package com.originlang.base.exception;

import org.springframework.util.StringUtils;

public class Assert {

	private Assert() {
	}

	public static void isTrue(boolean expression, String message) {
		isTrue(expression, ErrorCode.BAD_REQUEST, message);
	}

	public static void isTrue(boolean expression, ErrorCode errorCode, String message) {
		if (!expression) {
			throw new OriginLangAppException(errorCode, message);
		}
	}

	public static <T> T notNull(T value, String message) {
		return notNull(value, ErrorCode.BAD_REQUEST, message);
	}

	public static <T> T notNull(T value, ErrorCode errorCode, String message) {
		if (value == null) {
			throw new OriginLangAppException(errorCode, message);
		}
		return value;
	}

	public static String hasText(String value, String message) {
		return hasText(value, ErrorCode.BAD_REQUEST, message);
	}

	public static String hasText(String value, ErrorCode errorCode, String message) {
		if (!StringUtils.hasText(value)) {
			throw new OriginLangAppException(errorCode, message);
		}
		return value;
	}

}
