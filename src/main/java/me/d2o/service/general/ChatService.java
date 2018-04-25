/**
 *
 */
package me.d2o.service.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.chat.ChatMessage;
import me.d2o.persistence.model.chat.Conversation;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.repository.ConversationRepository;
import me.d2o.persistence.repository.InstantMessageRepository;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.transfermodel.chat.IncommingMessage;
import me.d2o.transfermodel.chat.UserStatus;

/**
 * Class: ChatService
 *
 * @author bo.hanssen
 * @since Jan 16, 2017 3:57:35 PM
 *
 */
@Service
@Transactional
public class ChatService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Set<Long> redeliverRequest;

	@Autowired
	private ConversationRepository conversations;

	@Autowired
	private SimpMessagingTemplate mq;

	@Autowired
	private InstantMessageRepository messages;

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationFactory notify;

	@PostConstruct
	private void init() {
		redeliverRequest = new HashSet<>();
	}

	public void removeConversation(String key) {
		if (conversations.exists(key)) {
			Conversation conv = conversations.findOne(key);
			conversations.delete(conv);
		}
	}

	public String generateKey(UserEntity receiver, UserEntity sender) {
		List<String> ids = Arrays.asList(receiver.getUniqueUserId(), sender.getUniqueUserId());
		Collections.sort(ids);
		StringBuilder sb = new StringBuilder();
		sb.append(ids.get(0));
		sb.append(ids.get(1));
		String key = sb.toString();
		logger.info("Created unique key: {}", key);
		return key;
	}

	public Conversation getConversation(UserEntity receiver, UserEntity sender) {
		String key = generateKey(receiver, sender);
		Conversation conv;
		if (conversations.exists(key)) {
			conv = conversations.findOne(key);
		} else {
			conv = new Conversation();
			conv.setId(key);
			conv.setUser1(sender);
			conv.setUser2(receiver);
			conversations.save(conv);
		}
		return conv;
	}

	public ChatMessage createMessage(IncommingMessage data, UserEntity sender, UserEntity receiver) {
		ChatMessage message = new ChatMessage();
		message.setAuthor(sender);
		message.setReceiver(receiver);
		message.setMessage(data.getMessage());
		messages.save(message);
		getConversation(receiver, sender).addMessage(message);
		if (receiver.isOnline()) {
			mq.convertAndSendToUser(receiver.getUsername(), "/queue/pushmessage", message);
		} else {
			notify.sendPushNotification(sender.getNickname() + " send you a message.", message.getMessage(), receiver);
		}
		return message;
	}

	public void redeliver(UserEntity receiver) {
		redeliverRequest.add(receiver.getId());
	}

	@Scheduled(fixedDelay = 5000)
	protected void redeliver() {
		for (long id : redeliverRequest) {
			UserEntity receiver = userService.getUser(id);
			if (receiver.isOnline()) {
				for (ChatMessage message : messages.findByReceiverAndDelivered(receiver, false)) {
					logger.debug("resending [{}/{}]", message.getId(), message.getMessage());
					mq.convertAndSendToUser(receiver.getUsername(), "/queue/pushmessage", message);
				}
			}
		}
		redeliverRequest.clear();
	}

	public void initStatus(UserEntity receiver) {
		for (UserEntity contact : userService.getAllUsers()) {
			mq.convertAndSendToUser(receiver.getUsername(), "/queue/chatStatus",
					new UserStatus(contact.isOnline(), contact.getNickname(), contact.getId()));
		}
	}

	public void deliverStatus(int id) {
		messages.findOne(id).setDelivered(true);
	}
}
