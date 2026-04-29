package com.originlang.admin.permission;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("sys_permission")
public class SysPermission {

	@Id
	private Long id;

	private String code;

	private String name;

}
