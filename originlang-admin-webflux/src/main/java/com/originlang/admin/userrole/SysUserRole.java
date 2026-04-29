package com.originlang.admin.userrole;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("sys_user_role_ref")
public class SysUserRole {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("role_id")
	private Long roleId;

}
