/**
 *
 */
package me.d2o.eventhandlers.setup.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.gameplay.UnassignedUnitService;
import me.d2o.service.general.StompService;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.service.scenario.RoleService;
import me.d2o.service.scenario.UnitService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: InitializeEventHandler
 *
 * @author bo.hanssen
 * @since Jan 23, 2017 5:15:08 PM
 *
 */
@Service
@Transactional
public class InitializeGameEventHandler extends GameEventHandler {

	@Value("${redirectUrl}/secure/game/")
	private String baseUri;

	@Autowired
	private GameService gameService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private DivisionService divisionService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private UnassignedUnitService unassignedUnitService;

	@Autowired
	private NotificationFactory notify;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		Game game = gameService.getGame(event);
		logger.info("divide territory: {}", game.getId());
		List<String> regionkeys = new ArrayList<>();
		game.getScenario().getRegions().keySet().forEach(regionkeys::add);
		Collections.shuffle(regionkeys);
		createNeutral(game, regionkeys);
		divideOverPlayers(game, regionkeys);
		addUnitsToPlayers(game);
		game.getParticipants().values().forEach(p -> notify.sendPushNotification("Game has started", "play now",
				p.getUser(), baseUri + game.getId() + "/"));
		stomp.pushTopic(event, "refresh", true);
		event.setPropagate(Events.INITIALIZE_AI_PLAYER);
	}

	@Override
	public String eventType() {
		return Events.INITIALIZE_GAME;
	}

	private void addUnitsToPlayers(Game game) {
		logger.info("Add initial units to players...");
		game.getParticipants().values().forEach(player -> player.getRole().getUnits().values()
				.forEach(unit -> unassignedUnitService.createUnits(player, unit, unit.getStartQuantity())));
	}

	private void createNeutral(Game game, List<String> regionkeys) {
		Role neutral = roleService.getRole("Neutral").get(0);
		Unit unit = unitService.getUnit("neutraldefence");
		double open = Math.floor((1 - game.getScenario().getNeutral()) * regionkeys.size());
		int quantity = game.getScenario().getNeutralQuantity();

		logger.info("Assign neutral territories...");
		while (regionkeys.size() > open) {
			do {
				Territory territory = territoryService.createTerritory(game, regionkeys, neutral);
				Division division = divisionService.createDivision(quantity, unit);
				territory.addDivision(division);
				game.addTerritory(territory);
				logger.info("Added neutral territory: [{}/{}/{}]", territory.getKey(), unit.getKey(), quantity);
			} while ((regionkeys.size() % game.getParticipants().size()) != 0);
		}
	}

	private void divideOverPlayers(Game game, List<String> regionkeys) {
		logger.info("Assign territories...");
		while (!regionkeys.isEmpty()) {
			for (Participant player : game.getParticipants().values()) {
				Territory territory = territoryService.createTerritory(game, regionkeys, player.getRole());
				territory.setUser(player.getUser());
				game.addTerritory(territory);
				logger.info("Added [{}] to [{}/{}]", territory.getKey(), player.getRole().getKey(),
						player.getUser().getUsername());
			}
		}
	}

}
