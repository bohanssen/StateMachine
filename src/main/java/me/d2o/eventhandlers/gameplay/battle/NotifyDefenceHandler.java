/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Territory;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.general.StompService;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: NotifyDefenceHandler
 *
 * @author bo.hanssen
 * @since Feb 3, 2017 11:43:07 AM
 *
 */
@Service
@Transactional
public class NotifyDefenceHandler extends GameEventHandler {

	@Autowired
	private BattleService battleService;

	@Autowired
	private StompService stomp;

	@Autowired
	private NotificationFactory notify;

	@Override
	public void handleEvent(GameEvent event) {
		Battle battle = battleService.getBattle(event);
		Territory territory = battle.getDefendor();
		stomp.push("defencemode", territory.getRegion().getId(), territory.getUser());
		stomp.pushInfo(event);
		notify.userNotification(territory.getUser(), "under-attack", event.getGameId());
	}

	@Override
	public String eventType() {
		return Events.NOTIFY_DEFENCE;
	}

}
