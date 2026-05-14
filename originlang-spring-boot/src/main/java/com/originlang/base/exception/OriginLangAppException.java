package com.originlang.base.exception;

import java.io.Serial;

/**
 * 应用层统一异常基类，优先使用 {@link ErrorCode} 表达错误码与描述。 子类可委托 {@code super(ErrorCode.xxx, message)}
 * 等构造，无需重复维护 code。
 */
public class OriginLangAppException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 2694815570723447208L;

	private final int code;

	private final ErrorCode errorCode;

	// ----- 基于 ErrorCode（推荐） -----

	public OriginLangAppException(ErrorCode errorCode) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
		this.code = errorCode.getValue();
	}

	public OriginLangAppException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.code = errorCode.getValue();
	}

	public OriginLangAppException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.code = errorCode.getValue();
	}

	// ----- 兼容：仅 code + message -----

	public OriginLangAppException(String message, int code) {
		super(message);
		this.errorCode = null;
		this.code = code;
	}

	public OriginLangAppException(String message, int code, Throwable cause) {
		super(message, cause);
		this.errorCode = null;
		this.code = code;
	}

	// ----- 仅 message 或 cause，默认 500 -----

	public OriginLangAppException(String message) {
		super(message);
		this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		this.code = ErrorCode.INTERNAL_SERVER_ERROR.getValue();
	}

	public OriginLangAppException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		this.code = ErrorCode.INTERNAL_SERVER_ERROR.getValue();
	}

	public OriginLangAppException(Throwable cause) {
		super(cause);
		this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		this.code = ErrorCode.INTERNAL_SERVER_ERROR.getValue();
	}

	// ----- getters -----

	public int getCode() {
		return code;
	}

	public ErrorCode getErrCode() {
		return errorCode;
	}

}
