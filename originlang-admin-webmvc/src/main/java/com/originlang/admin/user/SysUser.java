package com.originlang.admin.user;

import com.originlang.data.base.BaseIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseIdEntity {

	@Column(nullable = false, unique = true, length = 128)
	private String username;

	@Column(nullable = false, length = 512)
	private String password;

	@Column(nullable = false, length = 128)
	private String displayName;

	@Column(nullable = false)
	private boolean enabled = true;

}
