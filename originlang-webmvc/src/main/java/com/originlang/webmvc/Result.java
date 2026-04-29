package com.originlang.webmvc;

import java.io.Serializable;

public record Result<T>(boolean success, int code, String message, T data, long timestamp) implements Serializable {

	public static <T> Result<T> succeed() {
		return new Result<>(true, 200, "succeed", null, System.currentTimeMillis());
	}

	public static <T> Result<T> succeed(String message) {
		return new Result<>(true, 200, message, null, System.currentTimeMillis());
	}

	public static <T> Result<T> succeed(int code, String message) {
		return new Result<>(true, code, message, null, System.currentTimeMillis());
	}

	public static <T> Result<T> succeed(int code, T data) {
		return new Result<>(true, code, "succeed", data, System.currentTimeMillis());
	}

	public static <T> Result<T> succeed(String message, T data) {
		return new Result<>(true, 200, message, data, System.currentTimeMillis());
	}

	public static <T> Result<T> succeed(T data) {
		return new Result<>(true, 200, "succeed", data, System.currentTimeMillis());
	}

	public static <T> Result<T> failed() {
		return new Result<>(false, 200, "failed", null, System.currentTimeMillis());
	}

	public static <T> Result<T> failed(String message) {
		return new Result<>(false, 200, message, null, System.currentTimeMillis());
	}

	public static <T> Result<T> failed(int code, String message) {
		return new Result<>(false, code, message, null, System.currentTimeMillis());
	}

	public static <T> Result<T> result(boolean success) {
		if (success) {
			return new Result<>(true, 200, "succeed", null, System.currentTimeMillis());
		}
		else {
			return new Result<>(false, 200, "failed", null, System.currentTimeMillis());
		}
	}

	public static <T> Result<T> result(boolean success, T data) {
		if (success) {
			return new Result<>(true, 200, "succeed", data, System.currentTimeMillis());
		}
		else {
			return new Result<>(false, 200, "failed", data, System.currentTimeMillis());
		}
	}

	public static <T> Result<T> result(boolean success, String message, T data) {
		return new Result<>(success, 200, message, data, System.currentTimeMillis());
	}

	public enum CodeMessage {

		SUCCESS(200, "success"), ERROR(500, "error"), NOT_AUTH(401, "not auth"),;

		final int code;

		final String message;

		CodeMessage(int code, String message) {
			this.code = code;
			this.message = message;
		}

	}

}