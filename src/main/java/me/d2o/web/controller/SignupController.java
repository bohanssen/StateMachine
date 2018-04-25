package me.d2o.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import me.d2o.config.security.AuthUtil;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.UserService;

/**
 * The Class SignupController.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:56 PM
 */
@Controller
public class SignupController {

	private final ProviderSignInUtils signInUtils;

	private UserService userService;

	
	@Autowired
	public SignupController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository) {
		signInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	@RequestMapping(value = "/signup")
	public String signup(WebRequest request) {
		Connection<?> connection = signInUtils.getConnectionFromSession(request);
		if (connection != null) {

			String userId = connection.getDisplayName();

			if ("google".equals(connection.getKey().getProviderId())) {
				UserProfile profile = connection.fetchUserProfile();
				userId = profile.getEmail().split("@")[0];
			}

			// Save to DB if not exist and get id
			UserEntity user = userService.createIfNew(userId, connection.getKey().getProviderUserId());
			if (user == null) {
				return "redirect:/";
			}

			AuthUtil.authenticate(userId, user);
			signInUtils.doPostSignUp(userId, request);
		}
		return "redirect:/";
	}
}
