package com.originlang.admin.rolepermission;

import com.originlang.data.base.BaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysRolePermissionRefRepository extends BaseRepository<SysRolePermissionRef, Long> {

	Mono<Boolean> existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

	Flux<SysRolePermissionRef> findAllByRoleIdIn(Iterable<Long> roleIds);

}
