package me.d2o.config.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.savedrequest.NullRequestCache;

/**
 * The Class SecurityConfiguration.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:26:02 PM
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/secure").authenticated().antMatchers("/secure/**").authenticated().and()
				.formLogin().loginPage("/").and().requestCache().requestCache(new NullRequestCache()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().csrf().disable();
	}
}
