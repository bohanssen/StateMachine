/**
 *
 */
package me.d2o.eventhandlers.setup.units;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.UnassignedUnitService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: InformUserHandler
 *
 * @author bo.hanssen
 * @since Jan 27, 2017 1:13:02 PM
 *
 */
@Service
public class InitialUnitsHandler extends GameEventHandler {

	@Autowired
	private UnassignedUnitService unassignedUnitService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		logger.info("Checking if user [{}] has undeployed units", event.getUserId());
		Participant player = participantService.getParticipant(event);
		stomp.push("units", unassignedUnitService.getUnnasignedUnits(player));
		unassignedUnitService.allUnitsSet(event);
	}

	@Override
	public String eventType() {
		return Events.INITIAL_UNITS;
	}

}
