package com.originlang.admin.rolepermission;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("sys_role_permission_ref")
public class SysRolePermissionRef {

	@Id
	private Long id;

	@Column("role_id")
	private Long roleId;

	@Column("permission_id")
	private Long permissionId;

}
