package com.originlang.admin.user;

import com.originlang.data.base.BaseRepository;
import reactor.core.publisher.Mono;

public interface SysUserRepository extends BaseRepository<SysUser, Long> {

	Mono<Boolean> existsByUsername(String username);

	Mono<SysUser> findFirstByUsername(String username);

}
