package com.originlang.admin.role;

import com.originlang.data.base.BaseRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface SysRoleRepository extends BaseRepository<SysRole, Long> {

	@Query("select count(this) from SysRole where code = ?1")
	long countByCode(String code);

	@Query("where id(this) in ?1")
	List<SysRole> findByIdIn(List<Long> idList);

}
