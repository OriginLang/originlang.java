package com.originlang.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdCreatorTemplateTest {

	@Test
	void tsidShouldGeneratePositiveAndDistinctValues() {
		long first = IdCreator.tsid();
		long second = IdCreator.tsid();

		assertTrue(first > 0, "template unit tests should assert the business expectation directly");
		assertTrue(second > 0, "template unit tests should stay free of Spring context");
		assertNotEquals(first, second, "subsequent ids should differ");
	}

}
