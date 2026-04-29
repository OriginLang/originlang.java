package com.originlang.admin.userrole;

import com.originlang.data.base.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SysUserRoleRepository extends BaseRepository<SysUserRole, Long> {

	Mono<Boolean> existsByUserIdAndRoleId(Long userId, Long roleId);

	Flux<SysUserRole> findAllByUserId(Long userId);

}
