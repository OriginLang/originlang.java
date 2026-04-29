package com.originlang.data.audit;

import com.originlang.data.UserContext;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 用户id审计
 *
 */

@Configuration
public class Auditor implements AuditorAware<Long> {

	@Resource
	private UserContext userContext;

	@Override
	public Optional<Long> getCurrentAuditor() {
		// 获取当前用户id,
		return userContext.userId();

	}

}
