package com.originlang.admin.role;

import com.originlang.data.base.BaseRepository;
import reactor.core.publisher.Mono;

public interface SysRoleRepository extends BaseRepository<SysRole, Long> {

	Mono<Boolean> existsByCode(String code);

}
