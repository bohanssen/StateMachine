package me.d2o.utils.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.general.UserEntity;

/**
 * The Class Translate.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:37 PM
 */
@Service
public class Translate {

	@Autowired
	private MessageSource messageSource;

	
	public String getMessage(String label, UserEntity user) {
		return messageSource.getMessage(label, new Object[] {}, label, user.getLocale());
	}

	
	public String getMessage(String label, String defaultString, UserEntity user) {
		return messageSource.getMessage(label, new Object[] {}, defaultString, user.getLocale());
	}

	
	public String getMessage(String label, UserEntity user, Object... variables) {
		return messageSource.getMessage(label, variables, label, user.getLocale());
	}

	
	public String getMessage(String label, String defaultString, UserEntity user, Object... variables) {
		return messageSource.getMessage(label, variables, defaultString, user.getLocale());
	}
}
