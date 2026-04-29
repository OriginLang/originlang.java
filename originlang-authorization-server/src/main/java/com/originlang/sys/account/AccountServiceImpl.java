package com.originlang.sys.account;

import com.github.f4b6a3.tsid.TsidCreator;
import com.originlang.base.appname.SpringCoreProperty;
import com.originlang.data.page.PageRequest;
import com.originlang.data.page.PageResponse;
import com.originlang.base.exception.Auth401Exception;
import com.originlang.jwt.TokenPair;
import com.originlang.jwt.TokenProvider;
import com.originlang.resourceserver.AuthedUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final PasswordEncoder passwordEncoder;

	private final TokenProvider tokenProvider;

	private final SpringCoreProperty springCoreProperty;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public TokenPair login(AccountController.AuthDto authDto, HttpServletRequest request) {
		Account account = accountRepository.findFirstByUsername(authDto.getUsername())
			.orElseThrow(() -> new Auth401Exception("username or password is incorrect"));
		if (!passwordEncoder.matches(authDto.getPassword(), account.getPassword())) {
			throw new Auth401Exception("username or password is incorrect");
		}
		// Account account = userRepository.findById(account.getUserId()).orElseThrow(()
		// -> new
		// Auth401Exception("account is incorrect"));

		Set<GrantedAuthority> authorities = new HashSet<>();
		// 加一个默认角色,
		authorities.add(new SimpleGrantedAuthority("ROLE_" + springCoreProperty.getAppName()));

		AuthedUser authedUser = new AuthedUser(account.getId(), null, authorities, springCoreProperty.getAppName());
		// set spring security authed
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authedUser, "protected",
				authorities);
		token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// 允许子线程获取用户信息
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		SecurityContextHolder.getContext().setAuthentication(token);

		account.setLastLoginTime(LocalDateTime.now());
		accountRepository.save(account);

		return tokenProvider.token(SecurityContextHolder.getContext().getAuthentication());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	// @Override
	// public PageResponse<Account> page(PageRequest pageRequest) {
	// Account page = accountRepository.findAll(pageRequest.toPageable());
	// return new PageResponse<>(page);
	// }

	@Override
	public Optional<Account> findById(Long id) {
		return accountRepository.findById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteById(Long id) {
		accountRepository.deleteById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void register(AccountController.AuthDto authDto) {
		Account account = new Account();
		account.setOpenId(TsidCreator.getTsid().toString());
		account.setUsername(authDto.getUsername());
		account.setPassword(passwordEncoder.encode(authDto.getPassword()));
		accountRepository.save(account);

	}

}