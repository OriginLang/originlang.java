package com.originlang.base.exception;

public enum ErrorCode {

	SUCCESS(200, "success"), ERROR(500, "error"), NOT_AUTH(401, "not auth"), NOT_FOUND(404, "not found"),
	BAD_REQUEST(400, "bad request"), UNAUTHORIZED(401, "unauthorized"), FORBIDDEN(403, "forbidden"),
	INTERNAL_SERVER_ERROR(500, "internal server error"), SERVICE_UNAVAILABLE(503, "service unavailable"),
	GATEWAY_TIMEOUT(504, "gateway timeout"), PRECONDITION_FAILED(412, "precondition failed"),
	UNSUPPORTED_MEDIA_TYPE(415, "unsupported media type"),;

	private final int value;

	private final String description;

	ErrorCode(int value, String description) {
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
