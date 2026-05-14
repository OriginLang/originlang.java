package com.originlang.data.page;

import com.originlang.data.base.BaseIdEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageRequestTest {

	@Test
	void convertsOneBasedRequestToZeroBasedPageableWithDefaultSort() {
		PageRequest request = new PageRequest(2, 25);

		Pageable pageable = request.toPageable();

		assertEquals(1, pageable.getPageNumber());
		assertEquals(25, pageable.getPageSize());
		assertSort(pageable.getSort(), BaseIdEntity.CREATE_TIME);
	}

	@Test
	void supportsCommonDescendingSorts() {
		PageRequest request = new PageRequest(1, 10);

		assertSort(request.toPageableSortByIdDesc().getSort(), BaseIdEntity.ID);
		assertSort(request.toPageableSortByCreateTimeDesc().getSort(), BaseIdEntity.CREATE_TIME);
		assertSort(request.toPageableSortByUpdateTimeDesc().getSort(), BaseIdEntity.UPDATETIME);
	}

	@Test
	void preservesCustomSort() {
		PageRequest request = new PageRequest(3, 5);
		Sort sort = Sort.by(Sort.Direction.ASC, "username");

		Pageable pageable = request.toPageable(sort);

		assertEquals(2, pageable.getPageNumber());
		assertEquals(5, pageable.getPageSize());
		assertEquals(sort, pageable.getSort());
	}

	private static void assertSort(Sort sort, String property) {
		Sort.Order order = sort.getOrderFor(property);
		assertEquals(Sort.Direction.DESC, order.getDirection());
	}

}
