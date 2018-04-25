/**
 *
 */
package me.d2o.eventhandlers.setup.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.DeployRequest;

/**
 * Class: PlaceCapitolHandler
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 11:33:20 AM
 *
 */
@Service
@Transactional
public class PlaceCapitolHandler extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		DeployRequest request = (DeployRequest) event.getBody();
		Territory territory = territoryService.getTerritory(request.getGameId(), request.getRegion());
		if (!territoryService.checkOwnership(territory)) {
			return;
		}

		Participant player = participantService.getParticipant(event);
		if (player.getUndeployedtown() != 0 && !territory.isTown()) {
			territory.setTown(true);
			player.setUndeployedtown(player.getUndeployedtown() - 1);
			stomp.pushTerritoryUpdate(territory);
		}
		event.setPropagate(Events.USER_CONNECTED);
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_PLACE_TOWN;
	}

}
