package com.originlang.data.page;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageResponseTest {

	@Test
	void createsEmptyResponseByDefault() {
		PageResponse<String> response = new PageResponse<>();

		assertTrue(response.getContent().isEmpty());
		assertEquals(0, response.getTotalPages());
		assertEquals(0L, response.getTotalElements());
	}

	@Test
	void copiesSpringPageMetadata() {
		PageImpl<String> page = new PageImpl<>(List.of("a", "b"), PageRequest.of(1, 2), 5);

		PageResponse<String> response = new PageResponse<>(page);

		assertEquals(List.of("a", "b"), response.getContent());
		assertEquals(3, response.getTotalPages());
		assertEquals(5L, response.getTotalElements());
	}

	@Test
	void acceptsExplicitContentAndTotals() {
		PageResponse<String> response = new PageResponse<>(List.of("originlang"), 1, 1L);

		assertEquals(List.of("originlang"), response.getContent());
		assertEquals(1, response.getTotalPages());
		assertEquals(1L, response.getTotalElements());
	}

}
