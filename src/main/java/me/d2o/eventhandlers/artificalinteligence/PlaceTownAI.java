/**
 *
 */
package me.d2o.eventhandlers.artificalinteligence;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Region;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: PlaceTownAI
 *
 * @author bo.hanssen
 * @since Feb 15, 2017 1:33:49 PM
 *
 */
@Service
@Transactional
public class PlaceTownAI extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		Participant player = participantService.getParticipant(event);
		logger.info("{} {}", event, player);
		List<Territory> territories = territoryService.getTerritories(player);
		while (player.getUndeployedtown() != 0) {
			placeTown(territories, player, territoryService.getTerritoriesThatContainCapitol(player).size());
		}
		event.setPropagate(Events.USER_CONNECTED);
	}

	public void placeTown(List<Territory> territories, Participant player, int current) {
		Territory territory = territories.get(0);
		int score = current == 0 ? 0 : 1000;
		for (Territory candidate : territories) {
			int total = validCandidate(candidate, player) ? territoryService.getTotalUnits(candidate)
					: current == 0 ? 0 : 1000;
			if ((current == 0 && total > score) || (current != 0 && total < score)) {
				score = total;
				territory = candidate;
			}
		}
		territory.setTown(true);
		player.setUndeployedtown(player.getUndeployedtown() - 1);
		stomp.pushTerritoryUpdate(territory);
	}

	public boolean validCandidate(Territory territory, Participant player) {
		boolean retval = true;
		Set<Region> borders = territory.getRegion().getBorders();
		for (Territory territory2 : territoryService.getTerritoriesThatContainCapitol(player)) {
			retval = retval && !borders.contains(territory2.getRegion());
		}
		return retval;
	}

	@Override
	public String eventType() {
		return Events.AI_PLACE_TOWN;
	}

}
