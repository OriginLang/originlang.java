// package com.originlang.text;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.DefaultSecurityFilterChain;
// import org.springframework.security.web.authentication.AuthenticationFailureHandler;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.stereotype.Component;
//
// @Component
// public class SmsAuthenticationConfig extends
// SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
// @Autowired
// private AuthenticationFailureHandler authenticationFailureHandler;
// @Autowired
// private AuthenticationSuccessHandler authenticationSuccessHandler;
// @Autowired
// private UserDetailsService userDetailService;
//
// @Override
// public void configure(HttpSecurity httpSecurity) throws Exception {
// SmsAuthenticationFilter authenticationFilter = new SmsAuthenticationFilter();
// authenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
// authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
// authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
//
// SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
//// todo smsAuthenticationProvider.setUserDetailsService(userDetailService);
//
// httpSecurity.authenticationProvider(smsAuthenticationProvider)
// .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
// }
// }
