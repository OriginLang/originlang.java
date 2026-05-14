package com.originlang.base.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertTest {

	@Test
	void isTrueDoesNothingWhenExpressionIsTrue() {
		Assert.isTrue(true, "should not fail");
	}

	@Test
	void isTrueThrowsBadRequestByDefault() {
		OriginLangAppException exception = assertThrows(OriginLangAppException.class,
				() -> Assert.isTrue(false, "invalid state"));
		assertEquals(ErrorCode.BAD_REQUEST.getValue(), exception.getCode());
		assertEquals("invalid state", exception.getMessage());
		assertEquals(ErrorCode.BAD_REQUEST, exception.getErrCode());
	}

	@Test
	void notNullReturnsValueWhenPresent() {
		String value = "originlang";
		assertSame(value, Assert.notNull(value, "value is required"));
	}

	@Test
	void notNullSupportsCustomErrorCode() {
		OriginLangAppException exception = assertThrows(OriginLangAppException.class,
				() -> Assert.notNull(null, ErrorCode.NOT_FOUND, "user not found"));
		assertEquals(ErrorCode.NOT_FOUND.getValue(), exception.getCode());
		assertEquals("user not found", exception.getMessage());
		assertEquals(ErrorCode.NOT_FOUND, exception.getErrCode());
	}

	@Test
	void hasTextReturnsOriginalValue() {
		assertEquals("admin", Assert.hasText("admin", "username is required"));
	}

	@Test
	void hasTextRejectsBlankStrings() {
		OriginLangAppException exception = assertThrows(OriginLangAppException.class,
				() -> Assert.hasText("   ", "username is required"));
		assertEquals(ErrorCode.BAD_REQUEST.getValue(), exception.getCode());
		assertEquals("username is required", exception.getMessage());
		assertEquals(ErrorCode.BAD_REQUEST, exception.getErrCode());
	}

}
