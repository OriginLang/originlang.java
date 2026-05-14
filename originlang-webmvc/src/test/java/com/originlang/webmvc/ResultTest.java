package com.originlang.webmvc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultTest {

	@Test
	void succeedCreatesDefaultSuccessfulResult() {
		Result<Object> result = Result.succeed();

		assertTrue(result.success());
		assertEquals(200, result.code());
		assertEquals("succeed", result.message());
		assertNull(result.data());
		assertTrue(result.timestamp() > 0);
	}

	@Test
	void succeedCanCarryMessageCodeAndData() {
		Result<String> result = Result.succeed(201, "created");
		Result<String> dataResult = Result.succeed("accepted", "originlang");

		assertTrue(result.success());
		assertEquals(201, result.code());
		assertEquals("created", result.message());
		assertTrue(dataResult.success());
		assertEquals("accepted", dataResult.message());
		assertEquals("originlang", dataResult.data());
	}

	@Test
	void failedCreatesFailureResult() {
		Result<Object> result = Result.failed(400, "bad request");

		assertFalse(result.success());
		assertEquals(400, result.code());
		assertEquals("bad request", result.message());
		assertNull(result.data());
	}

	@Test
	void resultUsesSuccessFlagAndPreservesData() {
		Result<String> success = Result.result(true, "payload");
		Result<String> failed = Result.result(false, "payload");

		assertTrue(success.success());
		assertEquals("succeed", success.message());
		assertEquals("payload", success.data());
		assertFalse(failed.success());
		assertEquals("failed", failed.message());
		assertEquals("payload", failed.data());
	}

}
