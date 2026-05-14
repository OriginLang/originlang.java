package com.originlang.data.converter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SoftDeleteConverterTest {

	private final SoftDeleteConverter converter = new SoftDeleteConverter();

	@Test
	void convertsBooleanToDatabaseFlag() {
		assertEquals(1, this.converter.convertToDatabaseColumn(true));
		assertEquals(0, this.converter.convertToDatabaseColumn(false));
		assertEquals(0, this.converter.convertToDatabaseColumn(null));
	}

	@Test
	void convertsDatabaseFlagToBoolean() {
		assertTrue(this.converter.convertToEntityAttribute(1));
		assertFalse(this.converter.convertToEntityAttribute(0));
		assertFalse(this.converter.convertToEntityAttribute(null));
		assertFalse(this.converter.convertToEntityAttribute(2));
	}

}
