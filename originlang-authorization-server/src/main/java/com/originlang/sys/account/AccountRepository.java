package com.originlang.sys.account;

import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import com.originlang.data.base.BaseRepository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long> {

	@Find
	Optional<Account> findFirstByUsername(String username);

}
