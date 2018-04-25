
package me.d2o.web.stomp;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.UserService;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.transfermodel.chat.UserStatus;

/**
 * Class: StompExampleController
 *
 * @author bo.hanssen
 * @since Jan 4, 2017 11:55:28 AM
 *
 */
@Controller
public class PingController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<Long, Long> pings = new HashMap<>();

	@Autowired
	private UserService uService;

	@Autowired
	private SimpMessagingTemplate mq;

	@Autowired
	private NotificationFactory notify;

	@MessageMapping("/active/{gameid}")
	public void activePlayer(@DestinationVariable long gameid, Principal principal, boolean active) {
		notify.editUserActive(gameid, principal.getName(), active);
	}

	@MessageMapping("/ping")
	@SendToUser("/queue/ping")
	public boolean respond(Principal principal) throws StompException {
		UserEntity user = uService.getUser(principal);
		pings.put(user.getId(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
		return true;
	}

	@Scheduled(fixedRate = 2000)
	private void check() {
		MDC.put("logFileName", "cleanup/" + this.getClass().getSimpleName());
		long threshHold = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 2000;
		for (UserEntity user : uService.getOnlineUsers()) {
			if (!pings.containsKey(user.getId()) || pings.get(user.getId()) < threshHold) {
				pings.remove(user.getId());
				user.setOnline(false);
				uService.saveUser(user);
				logger.info("No active ping for user [{}] changed state to offline.", user.getId());
				mq.convertAndSend("/topic/chatStatus",
						new UserStatus(user.isOnline(), user.getNickname(), user.getId()));
			}
		}
	}
}
