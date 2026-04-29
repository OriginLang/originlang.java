package com.originlang.admin.permission;

import com.originlang.data.base.BaseIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sys_permission")
public class SysPermission extends BaseIdEntity {

	@Column(nullable = false, unique = true, length = 128)
	private String code;

	@Column(nullable = false, length = 128)
	private String name;

}
