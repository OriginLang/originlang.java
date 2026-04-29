package com.originlang.admin.user;

import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysUserRepository {

	@Find
	Optional<SysUser> findById(Long id);

	@Find
	Optional<SysUser> findFirstByUsername(String username);

	@Query("select count(this) from SysUser where username = ?1")
	long countByUsername(String username);

	@Insert
	SysUser save(SysUser sysUser);

	@Find
	List<SysUser> findAll();

}
