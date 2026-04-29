package com.originlang.data.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.originlang.data.base.BaseIdEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class PageRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = -4960454077289902184L;

	@JsonProperty("page")
	@NotNull(message = "page can not be null")
	@Min(value = 1, message = "must great than 0")
	private Integer page;

	@JsonProperty("size")
	@NotNull(message = "size can not be null")
	@Min(value = 1, message = "size must gt 1")
	@Max(value = Integer.MAX_VALUE, message = "size must lt 10000")
	private Integer size;

	public PageRequest() {
	}

	public PageRequest(Integer page, Integer size) {
		this.page = page;
		this.size = size;
	}

	public Pageable toPageableSortByIdDesc() {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseIdEntity.ID);
		return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
	}

	public Pageable toPageableSortByCreateTimeDesc() {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseIdEntity.CREATE_TIME);
		return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
	}

	public Pageable toPageableSortByUpdateTimeDesc() {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseIdEntity.UPDATETIME);
		return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
	}

	public Pageable toPageable(Sort sort) {
		return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
	}

	public Pageable toPageable() {
		Sort sort = Sort.by(Sort.Direction.DESC, BaseIdEntity.CREATE_TIME);
		return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
	}

}
