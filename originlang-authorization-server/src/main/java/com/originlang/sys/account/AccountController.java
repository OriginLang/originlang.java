package com.originlang.sys.account;

import com.originlang.data.page.PageRequest;
import com.originlang.data.page.PageResponse;
import com.originlang.jwt.TokenPair;
import com.originlang.webmvc.annotation.AnonymousAccess;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@Data
	public static class AuthDto implements Serializable {

		@Serial
		private static final long serialVersionUID = -6573743210561786343L;

		private String username;

		private String password;

	}

	public record AccountView(Long id, String username, LocalDateTime lastLoginTime) implements Serializable {
	}

	/**
	 * login
	 */
	@AnonymousAccess
	@PostMapping(value = { "/login", "sign-in" })
	public TokenPair login(@RequestBody AuthDto authDto, HttpServletRequest request) {
		return accountService.login(authDto, request);
	}

	/**
	 * register
	 */
	@AnonymousAccess
	@PostMapping(value = { "/register", "sign-up" })
	public void register(@RequestBody AuthDto authDto) {
		accountService.register(authDto);
	}

	@PostMapping
	public AccountView save(@RequestBody Account account) {
		return toView(accountService.save(account));
	}

	// @PreAuthorize("hasAuthority()")
	// @GetMapping
	// public PageResponse<Account> page(@RequestParam PageRequest pageRequest) {
	// return accountService.page(pageRequest);
	// }

	@GetMapping("/{id}")
	public AccountView findById(@PathVariable Long id) {
		return accountService.findById(id).map(this::toView).orElse(null);
	}

	/**
	 * deleteById
	 */
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Long id) {
		accountService.deleteById(id);
	}

	private AccountView toView(Account account) {
		return new AccountView(account.getId(), account.getUsername(), account.getLastLoginTime());
	}

}
