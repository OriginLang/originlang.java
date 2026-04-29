package com.originlang.data.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageResponse<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 3697186339359765914L;

	private final List<T> content;

	private final Integer totalPages;

	private final Long totalElements;

	public PageResponse(Page<T> page) {
		this.content = page.getContent();
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
	}

	public PageResponse(List<T> content, Integer totalPages, Long totalElements) {
		this.content = content;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}

	public PageResponse() {
		this.content = new ArrayList<>();
		this.totalPages = 0;
		this.totalElements = 0L;
	}

}
