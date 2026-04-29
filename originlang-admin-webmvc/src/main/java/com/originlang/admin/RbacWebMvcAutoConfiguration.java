package com.originlang.admin;

import com.originlang.admin.permission.SysPermission;
import com.originlang.admin.rbac.RbacAdminController;
import com.originlang.admin.rbac.RbacAdminService;
import com.originlang.admin.role.SysRole;
import com.originlang.admin.rolepermission.SysRolePermissionRef;
import com.originlang.admin.user.SysUser;
import com.originlang.admin.userrole.SysUserRoleRef;
import com.originlang.data.UserContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EntityScan(basePackageClasses = { SysUser.class, SysRole.class, SysPermission.class, SysUserRoleRef.class,
		SysRolePermissionRef.class })
@Import({ RbacAdminService.class, RbacAdminController.class })
public class RbacWebMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	UserContext defaultUserContext() {
		return new UserContext() {
			@Override
			public Optional<Long> userId() {
				return Optional.of(0L);
			}

			@Override
			public Optional<Long> tenantId() {
				return Optional.of(0L);
			}
		};
	}

}
