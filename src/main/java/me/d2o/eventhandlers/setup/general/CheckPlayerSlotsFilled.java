/**
 *
 */
package me.d2o.eventhandlers.setup.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.gameplay.GameService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: CheckPlayerSlotsFilled
 * 
 * @author bo.hanssen
 * @since Feb 23, 2017 5:17:08 PM
 *
 */
@Service
@Transactional
public class CheckPlayerSlotsFilled extends GameEventHandler {

	@Autowired
	private GameService gameService;

	@Override
	public void handleEvent(GameEvent event) {
		if (gameService.getAvailableRoles(event.getGameId()).isEmpty()) {
			event.setBody(null);
			event.setPropagate(Events.SETUP);
		}
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_CHECK_SLOTS_FILLED;
	}

}
