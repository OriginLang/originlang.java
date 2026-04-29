package com.originlang.data;

import java.util.Optional;

public interface UserContext {

	Optional<Long> userId();

	Optional<Long> tenantId();

}
