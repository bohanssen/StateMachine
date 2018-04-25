/**
 *
 */
package me.d2o.web.stomp;

import java.security.Principal;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import me.d2o.persistence.model.chat.ChatMessage;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.ChatService;
import me.d2o.service.general.UserService;
import me.d2o.transfermodel.chat.ChangeNicknameRequest;
import me.d2o.transfermodel.chat.IncommingMessage;
import me.d2o.transfermodel.chat.MessageAcknowledge;
import me.d2o.transfermodel.chat.UserStatus;

/**
 * Class: ChatController
 *
 * @author bo.hanssen
 * @since Jan 16, 2017 1:26:45 PM
 *
 */
@Controller
public class ChatController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService uService;

	@Autowired
	private ChatService chatService;

	@Autowired
	private SimpMessagingTemplate mq;

	@MessageMapping("/subscribe")
	@SendTo("/topic/chatStatus")
	@Transactional
	public UserStatus subscribe(Principal principal) throws StompException {
		logger.debug("User is online [{}]", principal.getName());
		UserEntity user = uService.getUser(principal);
		chatService.initStatus(user);
		chatService.redeliver(user);
		user.setOnline(true);
		return new UserStatus(true, user.getNickname(), user.getId());
	}

	@MessageMapping("/unsubscribe")
	@SendTo("/topic/chatStatus")
	@Transactional
	public UserStatus unsubscribe(Principal principal) throws StompException {
		logger.debug("User is offline [{}]", principal.getName());
		UserEntity user = uService.getUser(principal);
		user.setOnline(false);
		return new UserStatus(false, user.getNickname(), user.getId());
	}

	@MessageMapping("/instantmessage")
	@SendToUser("/queue/pushmessage")
	@Transactional
	public ChatMessage send(Principal principal, IncommingMessage data) throws StompException {
		UserEntity sender = uService.getUser(principal);
		UserEntity receiver = uService.getUser(data.getTarget());
		return chatService.createMessage(data, sender, receiver);
	}

	@MessageMapping("/acknowledge")
	@Transactional
	public void ack(MessageAcknowledge payload) {
		logger.debug("Got delivered ack: {}", payload.getId());
		chatService.deliverStatus(payload.getId());
	}

	@MessageMapping("/changenickname")
	@SendToUser("/queue/changenickname")
	@Transactional
	public UserStatus changeNickname(ChangeNicknameRequest request, Principal principal) {
		UserEntity user = uService.getUser(principal);
		if (request.getNickname() != null && !request.getNickname().isEmpty()) {
			user.setNickname(request.getNickname());
		}
		UserStatus status = new UserStatus(user.isOnline(), user.getNickname(), user.getId());
		mq.convertAndSend("/topic/chatStatus", status);
		return status;
	}
}
