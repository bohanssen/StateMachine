/**
 *
 */
package me.d2o.service.notification;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import me.d2o.config.constants.Constants;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.StompService;
import me.d2o.service.general.UserService;
import me.d2o.service.notification.events.PushNotificationEvent;
import me.d2o.service.notification.pojo.NotificationRequest;
import me.d2o.transfermodel.misc.Notification;
import me.d2o.utils.i18n.Translate;

/**
 * Class: NotificationFactory
 *
 * @author bo.hanssen
 * @since Jan 19, 2017 4:41:10 PM
 *
 */
@Service
public class NotificationFactory {

	private Map<String, NotificationRequest> requestMapping = new HashMap<>();

	@Autowired
	private Translate translate;

	@Autowired
	private UserService userService;

	@Autowired
	private SimpMessagingTemplate mq;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private StompService stomp;

	@Value("${redirectUrl}")
	private String baseUrl;

	public Notification createNotify(String message, UserEntity user, String level) {
		return new Notification(translate.getMessage(message, user), level);
	}

	public Notification createNotify(String message, Principal principal, String level) {
		return createNotify(message, userService.getUser(principal), level);
	}

	public Notification createNotify(String message, String username, String level) {
		return createNotify(message, userService.getUser(username), level);
	}

	public Notification createNotify(String message, UserEntity user) {
		return createNotify(message, user, Constants.DEFAULTLEVEL);
	}

	public Notification createNotify(String message, Principal principal) {
		return createNotify(message, principal, Constants.DEFAULTLEVEL);
	}

	public void sendNotify(String message, UserEntity user, String level) {
		Notification payload = createNotify(message, user, level);
		mq.convertAndSendToUser(user.getUsername(), Constants.NOTIFY_QUEUE, payload);
	}

	public void sendNotify(String message, Principal principal, String level) {
		Notification payload = createNotify(message, principal, level);
		mq.convertAndSendToUser(principal.getName(), Constants.NOTIFY_QUEUE, payload);
	}

	public void sendNotify(String message, String username, String level) {
		Notification payload = createNotify(message, username, level);
		mq.convertAndSendToUser(username, Constants.NOTIFY_QUEUE, payload);
	}

	public void sendNotify(String message, UserEntity user) {
		Notification payload = createNotify(message, user);
		mq.convertAndSendToUser(user.getUsername(), Constants.NOTIFY_QUEUE, payload);
	}

	public void sendNotify(String message, Principal principal) {
		Notification payload = createNotify(message, principal);
		mq.convertAndSendToUser(principal.getName(), Constants.NOTIFY_QUEUE, payload);
	}

	public void sendPushNotification(String title, String message, UserEntity user) {
		publisher.publishEvent(new PushNotificationEvent().addTitle(title).addContent(message).sendToUser(user));
	}

	public void sendPushNotification(String title, String message, UserEntity user, String url) {
		publisher.publishEvent(
				new PushNotificationEvent().addTitle(title).addContent(message).setUrl(url).sendToUser(user));
	}

	public void sendPushNotification(String title, String message, String username, String url) {
		publisher.publishEvent(new PushNotificationEvent().addTitle(title).addContent(message).setUrl(url)
				.sendToUser(userService.getUser(username)));
	}

	public void sendPushNotification(String title, String message, Principal principal) {
		sendPushNotification(title, message, userService.getUser(principal));
	}

	public void sendPushNotification(String message, UserEntity user) {
		sendPushNotification("", message, user);
	}

	public void sendPushNotification(String message, Principal principal) {
		sendPushNotification("", message, principal);
	}

	public void send(String title, String message, UserEntity user) {
		if (user.isOnline()) {
			sendNotify(message, user);
		} else {
			sendPushNotification(title, message, user);
		}
	}

	public void send(String title, String message, Principal principal) {
		send(title, message, userService.getUser(principal));
	}

	public void send(String message, UserEntity user) {
		send("", message, user);
	}

	public void send(String message, Principal principal) {
		send("", message, principal);
	}

	public void broadcastNotify(String message, UserEntity user, String level) {
		Notification payload = createNotify(message, user, level);
		mq.convertAndSendToUser(user.getUsername(), Constants.NOTIFY_TOPIC, payload);
	}

	public void broadcastNotify(String message, UserEntity user) {
		Notification payload = createNotify(message, user);
		mq.convertAndSend(Constants.NOTIFY_TOPIC, payload);
	}

	public void broadcastPush(String title, String message) {
		publisher.publishEvent(new PushNotificationEvent().addTitle(title).addContent(message).broadcast());
	}

	public void userNotification(UserEntity user, String label, long gameId) {
		NotificationRequest request = new NotificationRequest(user.getUsername(), translate.getMessage(label, user),
				baseUrl + "/secure/game/" + gameId + "/");
		requestMapping.put(user.getUsername() + gameId, request);
		stomp.pushTopic(gameId, "playeractive", true);
	}

	public void editUserActive(long gameId, String username, boolean active) {
		if (requestMapping.containsKey(username + gameId)) {
			requestMapping.get(username + gameId).setActive(active);
		}
	}

	@Scheduled(fixedRate = 500)
	public void proccessRequests() {
		for (String key : requestMapping.keySet()) {
			NotificationRequest request = requestMapping.get(key);
			if (request.isActive()) {
				this.sendNotify(request.getMessage(), request.getUserName(), "info");
				requestMapping.remove(key);
			} else if (request.getCount() == 2) {
				this.sendPushNotification("", request.getMessage(), request.getUserName(), request.getUrl());
				requestMapping.remove(key);
			} else {
				request.addCount();
			}
		}
	}
}
