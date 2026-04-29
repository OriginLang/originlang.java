package com.originlang.admin.permission;

import com.originlang.data.base.BaseRepository;
import reactor.core.publisher.Mono;

public interface SysPermissionRepository extends BaseRepository<SysPermission, Long> {

	Mono<Boolean> existsByCode(String code);

}
