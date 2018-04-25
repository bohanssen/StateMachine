/**
 *
 */
package me.d2o.eventhandlers.artificalinteligence;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: SetupAI
 *
 * @author bo.hanssen
 * @since Feb 15, 2017 11:49:07 AM
 *
 */
@Service
@Transactional
public class SetupUnitsAI extends GameEventHandler {

	@Autowired
	private GameService gameService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private StompService stomp;

	private Random random;

	@PostConstruct
	public void init() {
		random = new Random();
	}

	@Override
	public void handleEvent(GameEvent event) {
		gameService.getGame(event).getParticipants().values().forEach(player -> setup(player));
	}

	public Division getDivision(Territory territory, Unit unit) {
		Division division = territory.getDivisions().get(unit.getKey());
		if (division == null) {
			division = new Division();
			division.setUnit(unit);
			territory.addDivision(division);
		}
		return division;
	}

	public void setup(Participant player) {
		if ("AI".equals(player.getUser().getRole())) {
			List<Territory> territories = territoryService.getTerritories(player);
			player.getUnassignedUnits().values().forEach(unasigned -> {
				for (int i = 0; i < unasigned.getQuantity(); i++) {
					Territory territory = territories.get(random.nextInt(territories.size()));
					Division division = getDivision(territory, unasigned.getUnit());
					division.incrementQuantity(1);
					stomp.pushTerritoryUpdate(territory);
				}
				unasigned.setQuantity(0);
			});
		}
	}

	@Override
	public String eventType() {
		return Events.HANDLE_AI_PLAYER_SETUP;
	}

}
