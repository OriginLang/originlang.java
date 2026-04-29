package com.originlang.admin.permission;

import com.originlang.data.base.BaseRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface SysPermissionRepository extends BaseRepository<SysPermission, Long> {

	@Query("select count(this) from SysPermission where code = ?1")
	long countByCode(String code);

	@Query("where id(this) in ?1")
	List<SysPermission> findByIdIn(List<Long> idList);

}
