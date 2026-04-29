package com.originlang.admin.role;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("sys_role")
public class SysRole {

	@Id
	private Long id;

	private String code;

	private String name;

}
