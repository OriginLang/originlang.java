// package com.originlang.text;
//
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.security.authentication.AuthenticationServiceException;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import
// org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
// import java.io.IOException;
//
// public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
// private static final String MOBILE_KEY = "mobile";
// private boolean postOnly = true;
//
// public SmsAuthenticationFilter() {
// super(new AntPathRequestMatcher("/login/mobile", "POST"));
// }
//
// @Override
// public Authentication attemptAuthentication(HttpServletRequest request,
// HttpServletResponse response) throws AuthenticationException, IOException,
// ServletException {
//
// if (postOnly && !request.getMethod().equals("POST")) {
// throw new AuthenticationServiceException("Authentication method not support: " +
// request.getMethod());
// }
//
// String mobile = request.getParameter(MOBILE_KEY);
//
// if (mobile == null) {
// mobile = "";
// }
//
// SmsAuthenticationToken authRequest = new SmsAuthenticationToken(mobile);
//
// setDetails(request, authRequest);
//
// return this.getAuthenticationManager().authenticate(authRequest);
// }
//
// protected void setDetails(HttpServletRequest request, SmsAuthenticationToken
// authRequest) {
// authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
// }
// }
