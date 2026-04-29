package com.originlang.data.hibernate;

import com.originlang.data.UserContext;
import jakarta.annotation.Resource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * if multi datasource use MultiTenantConnectionProvider
 */
@Component
public class CurrentTenantId implements CurrentTenantIdentifierResolver<Long>, HibernatePropertiesCustomizer {

	@Resource
	private UserContext userContext;

	@Override
	@org.checkerframework.checker.nullness.qual.UnknownKeyFor
	@org.checkerframework.checker.nullness.qual.Nullable
	@org.checkerframework.checker.initialization.qual.Initialized
	public Long resolveCurrentTenantIdentifier() {
		return userContext.tenantId().orElse(0L);
	}

	@Override
	@org.checkerframework.checker.nullness.qual.UnknownKeyFor
	@org.checkerframework.checker.initialization.qual.Initialized
	public boolean validateExistingCurrentSessions() {
		return false;
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
	}

}
