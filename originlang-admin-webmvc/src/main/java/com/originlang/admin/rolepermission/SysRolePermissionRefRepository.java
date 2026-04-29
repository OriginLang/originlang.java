package com.originlang.admin.rolepermission;

import com.originlang.data.base.BaseRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface SysRolePermissionRefRepository extends BaseRepository<SysRolePermissionRef, Long> {

	@Query("select count(this) from SysRolePermissionRef where roleId = ?1 and permissionId = ?2")
	long countByRoleIdAndPermissionId(Long roleId, Long permissionId);

	@Query("where roleId in ?1")
	List<SysRolePermissionRef> findAllByRoleIdIn(List<Long> roleIdList);

}
