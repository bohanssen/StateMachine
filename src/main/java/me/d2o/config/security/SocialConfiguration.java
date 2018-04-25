package me.d2o.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.web.context.request.NativeWebRequest;

import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.UserService;

/**
 * The Class SocialConfiguration.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:49 PM
 */
@Configuration
@EnableSocial
public class SocialConfiguration extends SocialConfigurerAdapter {

	private UserService userService;

	@Value("${spring.social.google.appId}")
	private String appId;

	@Value("${spring.social.google.appSecret}")
	private String appSecret;

	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	@Bean
	public SignInAdapter authSignInAdapter() {
		return new SignInAdapter() {

			@Override
			public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
				UserEntity user = userService.createIfNew(connection.getDisplayName(),
						connection.getKey().getProviderUserId());
				if (user == null) {
					return null;
				}
				AuthUtil.authenticate(userId, user);
				return null;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.social.config.annotation.SocialConfigurerAdapter#
	 * addConnectionFactories(org.springframework.social.config.annotation.
	 * ConnectionFactoryConfigurer, org.springframework.core.env.Environment)
	 */
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
			Environment environment) {
		GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(appId, appSecret);
		googleConnectionFactory.setScope("email");
		connectionFactoryConfigurer.addConnectionFactory(googleConnectionFactory);
	}

	
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Google google(ConnectionRepository repository) {
		Connection<Google> connection = repository.findPrimaryConnection(Google.class);
		return connection != null ? connection.getApi() : null;
	}
}
