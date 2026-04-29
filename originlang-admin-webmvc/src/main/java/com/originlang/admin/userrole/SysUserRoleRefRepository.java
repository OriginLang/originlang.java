package com.originlang.admin.userrole;

import com.originlang.data.base.BaseRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRefRepository extends BaseRepository<SysUserRoleRef, Long> {

	@Query("select count(this) from SysUserRoleRef where userId = ?1 and roleId = ?2")
	long countByUserIdAndRoleId(Long userId, Long roleId);

	@Find
	List<SysUserRoleRef> findAllByUserId(Long userId);

}
