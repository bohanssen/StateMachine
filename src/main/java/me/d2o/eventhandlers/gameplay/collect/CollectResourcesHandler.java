/**
 *
 */
package me.d2o.eventhandlers.gameplay.collect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: CollectResourcesHandler
 *
 * @author bo.hanssen
 * @since Jan 30, 2017 12:00:52 AM
 *
 */
@Service
@Transactional
public class CollectResourcesHandler extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		Participant player = participantService.getParticipant(event);
		logger.info("Calculate gold for user [{}]", player.getUser().getUsername());
		int cash = participantService.getWorth(player);
		player.editCash(cash);
		logger.info("Grant [{}] gold to [{}]", cash, player.getUser().getUsername());
		event.setPropagate(Events.START_STORE);
		stomp.pushInfo(event);
	}

	@Override
	public String eventType() {
		return Events.COLLECT_RESOURCES;
	}

}
