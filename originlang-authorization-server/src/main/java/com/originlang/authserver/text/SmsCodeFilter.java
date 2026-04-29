// package com.originlang.text;
//
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.web.authentication.AuthenticationFailureHandler;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.ServletRequestBindingException;
// import org.springframework.web.bind.ServletRequestUtils;
// import org.springframework.web.context.request.ServletWebRequest;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import java.io.IOException;
//
// @Component
// public class SmsCodeFilter extends OncePerRequestFilter {
//
// @Autowired
// private AuthenticationFailureHandler authenticationFailureHandler;
//
//// private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
//
// @Override
// protected void doFilterInternal(HttpServletRequest httpServletRequest,
// HttpServletResponse httpServletResponse, FilterChain filterChain) throws
// ServletException, IOException, IOException {
// if (StringUtils.equalsIgnoreCase("/login/mobile", httpServletRequest.getRequestURI())
// && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
// try {
// validateCode(new ServletWebRequest(httpServletRequest));
// } catch (Exception e) {
// authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,
// httpServletResponse, e);
// return;
// }
// }
// filterChain.doFilter(httpServletRequest, httpServletResponse);
// }
//
// private void validateCode(ServletWebRequest servletWebRequest) throws
// ServletRequestBindingException {
// String smsCode = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),
// "smsCode");
// String mobile = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),
// "mobile");
//
//// SmsCode codeInSession = (SmsCode) sessionStrategy.getAttribute(servletWebRequest,
// ValidateController.SESSION_KEY_SMS_CODE + mobile);
////
//// if (StringUtils.isBlank(smsCode)) {
//// throw new ValidateCodeException("验证码不能为空");
//// }
////
//// if (codeInSession == null) {
//// throw new ValidateCodeException("验证码不存在");
//// }
////
//// if (codeInSession.isExpire()) {
//// sessionStrategy.removeAttribute(servletWebRequest,
// ValidateController.SESSION_KEY_SMS_CODE + mobile);
//// throw new ValidateCodeException("验证码已经过期");
//// }
////
//// if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), smsCode)) {
//// throw new ValidateCodeException("验证码不正确");
//// }
////
//// sessionStrategy.removeAttribute(servletWebRequest,
// ValidateController.SESSION_KEY_SMS_CODE + mobile);
// }
// }
