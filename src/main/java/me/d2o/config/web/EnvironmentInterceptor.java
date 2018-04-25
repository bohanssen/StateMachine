package me.d2o.config.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import me.d2o.config.constants.Constants;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.UserService;

/**
 * The Class EnvironmentInterceptor.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:33 PM
 *
 *         This class intercepts all requests to run pre/post checks or to load
 *         properties in the model that are needed globally.
 */
@Component
public class EnvironmentInterceptor implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService uservice;

	@Value("${baseUri}")
	private String baseUri;

	@Value("${onesignal.id}")
	private String onesignalId;

	@Value("${D2O.id}")
	private String appId;

	private void fillMV(ModelAndView mv) {
		try {
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			if ("anonymousUser".equals(userId) || userId == null) {
				mv.addObject(Constants.AUTHENTICATED, false);
				logger.debug("Request from unautheticated user");
			} else {
				logger.debug("Request from: " + userId);
				mv.addObject(Constants.AUTHENTICATED, true);
				mv.addObject("user", uservice.getUser());
				boolean reload = uservice.getUser().isReload();
				mv.addObject("reload", reload);
				if (reload) {
					uservice.getUser().setReload(false);
					uservice.saveUser(uservice.getUser());
				}
			}
		} catch (NullPointerException ex) {
			mv.addObject(Constants.AUTHENTICATED, false);
			logger.debug("No user signed in: {}", ex);
		}
		mv.addObject("baseUri", baseUri);
		mv.addObject("onesignalId", onesignalId);
		mv.addObject("appId", appId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {
		if (mv != null) {
			fillMV(mv);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			Locale locale = LocaleContextHolder.getLocale();
			UserEntity user = uservice.getUser();
			if (!locale.equals(user.getLocale())) {
				logger.info("Saving locale for user [{}]", locale.getLanguage());
				user.setLocale(locale);
				user.setReload(true);
				uservice.saveUser(user);
			}
		} catch (Exception ex) {
			logger.debug("No user signed in", ex);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// No after complition needed at the moment
	}
}
