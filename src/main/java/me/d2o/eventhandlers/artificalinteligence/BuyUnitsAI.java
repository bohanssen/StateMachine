/**
 *
 */
package me.d2o.eventhandlers.artificalinteligence;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.service.scenario.UnitService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: BuyUnitsAI
 *
 * @author bo.hanssen
 * @since Feb 21, 2017 11:21:37 AM
 *
 */
@Service
@Transactional
public class BuyUnitsAI extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private StompService stomp;

	@Autowired
	private DivisionService divisionService;

	@Autowired
	private PlaceTownAI townHandler;

	private Random random;

	@PostConstruct
	public void init() {
		random = new Random();
	}

	@Override
	public void handleEvent(GameEvent event) {
		Participant player = participantService.getParticipant(event);
		List<Territory> territories = territoryService.getTerritories(player).stream().filter(t -> t.isTown())
				.collect(Collectors.toList());
		logger.info("AI buy units, cash to spend [{}]", player.getCash());
		int capitolCost = player.getRole().getItems().get("capitol");
		if ((territories.isEmpty() || (player.getCash() >= (territories.size()+1) * capitolCost))
				&& player.getCash() >= capitolCost) {
			player.editCash(-capitolCost);
			player.setUndeployedtown(player.getUndeployedtown() + 1);
			townHandler.placeTown(territoryService.getTerritories(player).stream().filter(t -> !t.isTown())
					.collect(Collectors.toList()),player,territories.size());
			territories = territoryService.getTerritories(player).stream().filter(t -> t.isTown())
					.collect(Collectors.toList());
		}
		while (player.getCash() != 0 && !territories.isEmpty()) {
			int skipped = 0;
			if (territories.isEmpty()) {
				logger.warn("No town present, buy one now");
				break;
			}
			for (Unit unit : unitService.getAvailableUnits(player)) {
				Territory territory = territories.get(random.nextInt(territories.size()));
				logger.info("Try to deploy unit [{}] to territory [{}] cost [{}]", unit.getKey(), territory.getKey(),
						unit.getCost());
				if (unit.getCost() <= player.getCash()) {
					Division division = divisionService.getOrCreateDivision(territory, unit);
					division.incrementQuantity(1);
					player.editCash(-unit.getCost());
					stomp.pushTerritoryUpdate(territory);
				} else {
					logger.info("To expensive skip!");
					skipped += 1;
				}
			}
			if (skipped == unitService.getAvailableUnits(player).size()) {
				break;
			}
		}
		event.setPropagate(Events.CLOSE_STORE);
	}

	@Override
	public String eventType() {
		return Events.AI_OPEN_STORE;
	}

}
