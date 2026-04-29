package com.originlang.admin.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("sys_user")
public class SysUser {

	@Id
	private Long id;

	private String username;

	private String password;

	@Column("display_name")
	private String displayName;

	private Boolean enabled = true;

}
