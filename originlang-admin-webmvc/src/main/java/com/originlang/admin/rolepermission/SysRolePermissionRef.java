package com.originlang.admin.rolepermission;

import com.originlang.data.base.BaseIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sys_role_permission_ref",
		uniqueConstraints = @UniqueConstraint(name = "uk_sys_role_permission_ref_role_permission",
				columnNames = { "role_id", "permission_id" }))
public class SysRolePermissionRef extends BaseIdEntity {

	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Column(name = "permission_id", nullable = false)
	private Long permissionId;

}
