// package com.originlang.text;
//
// import org.springframework.security.authentication.AbstractAuthenticationToken;
// import org.springframework.security.core.GrantedAuthority;
//
// import javax.security.auth.Subject;
// import java.io.Serial;
// import java.util.Collection;
//
// public class SmsAuthenticationToken extends AbstractAuthenticationToken {
//
// @Serial
// private static final long serialVersionUID = -1337659850269032949L;
//
// private final Object principal;
//
// public SmsAuthenticationToken(String mobile) {
// super(null);
// this.principal = mobile;
// super.setAuthenticated(false);
// }
//
// public SmsAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
// Object principal) {
// super(authorities);
// this.principal = principal;
// super.setAuthenticated(true);
// }
//
//
// @Override
// public Object getCredentials() {
// return null;
// }
//
// @Override
// public Object getPrincipal() {
// return this.principal;
// }
//
// @Override
// public boolean implies(Subject subject) {
// return super.implies(subject);
// }
// }
