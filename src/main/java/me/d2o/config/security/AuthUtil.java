package me.d2o.config.security;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import me.d2o.persistence.model.general.UserEntity;

/**
 * The Class AuthUtil.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:58 PM
 */
public class AuthUtil {

	private static final String ROLEPREFIX = "ROLE_";

	private AuthUtil() {
		// Hide default constructor
	}

	public static void authenticate(String userId, UserEntity user) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null,
				Arrays.asList(new SimpleGrantedAuthority(ROLEPREFIX + user.getRole())));
		authentication.setDetails(user.getId());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
