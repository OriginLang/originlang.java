package com.originlang.sys.permission;

import com.originlang.data.base.BaseTenantIdEntity;
import com.originlang.data.base.BaseTenantUserIdEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Entity
@Getter
@Setter
public class Permission extends BaseTenantIdEntity {

	@Serial
	private static final long serialVersionUID = 4349353090320713146L;

	private String permission;

}
