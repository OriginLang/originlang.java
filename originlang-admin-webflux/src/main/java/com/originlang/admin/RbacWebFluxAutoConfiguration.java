package com.originlang.admin;

import com.originlang.admin.permission.SysPermissionRepository;
import com.originlang.admin.rbac.ReactiveRbacAdminController;
import com.originlang.admin.rbac.ReactiveRbacAdminService;
import com.originlang.admin.role.SysRoleRepository;
import com.originlang.admin.rolepermission.SysRolePermissionRefRepository;
import com.originlang.admin.user.SysUserRepository;
import com.originlang.admin.userrole.SysUserRoleRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableR2dbcRepositories(basePackageClasses = { SysUserRepository.class, SysRoleRepository.class,
		SysPermissionRepository.class, SysUserRoleRepository.class, SysRolePermissionRefRepository.class })
@Import({ ReactiveRbacAdminService.class, ReactiveRbacAdminController.class })
public class RbacWebFluxAutoConfiguration {

	@Bean
	@ConditionalOnProperty(value = "originlang.admin.rbac.schema.enabled", havingValue = "true", matchIfMissing = true)
	ConnectionFactoryInitializer originlangAdminSchemaInitializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(
				new ResourceDatabasePopulator(new ClassPathResource("db/originlang-admin-webflux-rbac.sql")));
		return initializer;
	}

}
