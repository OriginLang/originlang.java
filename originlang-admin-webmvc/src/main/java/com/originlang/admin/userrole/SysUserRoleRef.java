package com.originlang.admin.userrole;

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
@Table(name = "sys_user_role_ref", uniqueConstraints = @UniqueConstraint(name = "uk_sys_user_role_ref_user_role",
		columnNames = { "user_id", "role_id" }))
public class SysUserRoleRef extends BaseIdEntity {

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "role_id", nullable = false)
	private Long roleId;

}
