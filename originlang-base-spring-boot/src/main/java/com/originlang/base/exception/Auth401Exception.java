package com.originlang.base.exception;

import java.io.Serial;

public class Auth401Exception extends OriginLangAppException {

	@Serial
	private static final long serialVersionUID = 2694815570723447208L;

	private final String message;

	private int code = 401;

	public Auth401Exception(String message) {
		super(message);
		this.message = message;
	}

	public Auth401Exception(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
