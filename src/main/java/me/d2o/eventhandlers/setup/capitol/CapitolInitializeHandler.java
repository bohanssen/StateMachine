/**
 *
 */
package me.d2o.eventhandlers.setup.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.statemachine.StateMachineService;

/**
 * Class: CapitolInitializeHandler
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:20:54 AM
 *
 */
@Service
@Transactional
public class CapitolInitializeHandler extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private StompService stomp;

	@Autowired
	private GameService gameService;

	@Autowired
	private StateMachineService fsm;

	@Override
	public void handleEvent(GameEvent event) {
		logger.info("Checking if user [{}] has a Capitol to be placed", event.getUserId());
		Participant player = participantService.getParticipant(event);
		if (player.getUndeployedtown() != 0) {
			stomp.push("town", true);
		} else {
			stomp.push("town", false);
			checkIfThisStageIsReady(event);
		}
	}

	@Override
	public String eventType() {
		return Events.NOTIFY_PLACE_TOWN;
	}

	private void checkIfThisStageIsReady(GameEvent event) {
		Game game = gameService.getGame(event);
		boolean ready = true;
		for (Participant player : game.getParticipants().values()) {
			if (player.getUndeployedtown() != 0) {
				ready = false;
				if ("AI".equals(player.getUser().getRole())) {
					event.setUserId(player.getUser().getId());
					fsm.triggerTransition(event, Events.AI_INIT_TOWN);
				}
			}
		}
		if (ready) {
			fsm.triggerTransition(event, Events.PLACE_TOWN_READY);
		}
	}
}
