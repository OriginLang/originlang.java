package com.originlang.config;

import com.originlang.data.UserContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserContextImpl implements UserContext {

	@Override
	public Optional<Long> userId() {
		return Optional.of(1L);
	}

	@Override
	public Optional<Long> tenantId() {
		return Optional.of(100L);
	}

}
