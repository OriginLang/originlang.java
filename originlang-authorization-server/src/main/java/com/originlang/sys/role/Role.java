package com.originlang.sys.role;

import com.originlang.data.base.BaseTenantIdEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Entity
@Getter
@Setter
public class Role extends BaseTenantIdEntity {

	@Serial
	private static final long serialVersionUID = -6932994494713334205L;

	private String role;

}
