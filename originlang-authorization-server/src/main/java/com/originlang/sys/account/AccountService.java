package com.originlang.sys.account;

import com.originlang.data.page.PageRequest;
import com.originlang.data.page.PageResponse;
import com.originlang.jwt.TokenPair;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface AccountService {

	Account save(Account account);

	// PageResponse<Account> page(PageRequest pageRequest);

	Optional<Account> findById(Long id);

	void deleteById(Long id);

	void register(AccountController.AuthDto authDto);

	TokenPair login(AccountController.AuthDto authDto, HttpServletRequest request);

}
