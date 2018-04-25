/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.general.StompService;
import me.d2o.service.general.UserService;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: InitializeBattleModeHandler
 *
 * @author bo.hanssen
 * @since Jan 31, 2017 5:33:32 PM
 *
 */
@Service
@Transactional
public class InitializeBattleModeHandler extends GameEventHandler {

	@Autowired
	private StompService stomp;

	@Autowired
	private BattleService battleService;

	@Autowired
	private NotificationFactory notify;

	@Autowired
	private UserService userService;

	@Override
	public void handleEvent(GameEvent event) {
		logger.info("Start battlemode for user [{}]", event.getUserId());
		battleService.remove(event);
		stomp.push("battlemode", true);
		notify.userNotification(userService.getUser(), "choose-attack", event.getGameId());
	}

	@Override
	public String eventType() {
		return Events.START_BATTLEMODE;
	}

}
