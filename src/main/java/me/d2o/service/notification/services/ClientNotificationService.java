package me.d2o.service.notification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.general.UserEntity;
import me.d2o.transfermodel.misc.Notification;
import me.d2o.utils.i18n.Translate;
import me.d2o.utils.notification.Level;

/**
 * The Class Notification.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:38 PM
 */
@Service
public class ClientNotificationService {

	@Autowired
	private Translate translate;

	@Autowired
	private SimpMessagingTemplate mq;

	private void notify(String message, UserEntity user, Level level) {
		Notification m = new Notification(translate.getMessage(message, user));
		m.setLevel(level);
		mq.convertAndSendToUser(user.getUsername(), "/queue/notification", m);
	}

	private void notify(String message, String defaultString, UserEntity user, Level level, Object... variables) {
		Notification m = new Notification(translate.getMessage(message, defaultString, user, variables));
		m.setLevel(level);
		mq.convertAndSendToUser(user.getUsername(), "/queue/notification", m);
	}

	public void broadCast(String message, Level level) {
		Notification m = new Notification(message);
		m.setLevel(level);
		mq.convertAndSend("/topic/notification", m);
	}

	public void info(String message, UserEntity user) {
		notify(message, user, Level.INFO);
	}

	public void info(String message, String defaultString, UserEntity user, Object... variables) {
		notify(message, defaultString, user, Level.INFO, variables);
	}

	public void success(String message, UserEntity user) {
		notify(message, user, Level.SUCCESS);
	}

	public void success(String message, String defaultString, UserEntity user, Object... variables) {
		notify(message, defaultString, user, Level.SUCCESS, variables);
	}

	public void warn(String message, UserEntity user) {
		notify(message, user, Level.WARNING);
	}

	public void warn(String message, String defaultString, UserEntity user, Object... variables) {
		notify(message, defaultString, user, Level.WARNING, variables);
	}

	public void error(String message, UserEntity user) {
		notify(message, user, Level.ERROR);
	}

	public void error(String message, String defaultString, UserEntity user, Object... variables) {
		notify(message, defaultString, user, Level.ERROR, variables);
	}
}
