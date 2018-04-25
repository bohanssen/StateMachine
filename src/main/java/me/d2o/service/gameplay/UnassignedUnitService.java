/**
 *
 */
package me.d2o.service.gameplay;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.UnassignedUnits;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.general.StompService;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.StateMachineService;
import me.d2o.transfermodel.gameplay.DivisionClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: UnassignedUnitService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 10:58:17 AM
 *
 */
@Service
public class UnassignedUnitService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Translate translate;

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private StompService stompService;

	public void createUnits(Participant player, Unit unit, int quantity) {
		UnassignedUnits unUnit = new UnassignedUnits();
		unUnit.setQuantity(quantity);
		unUnit.setUnit(unit);
		player.addUnits(unUnit);
		logger.info("Added [{} / {}] to player [{} / {}]", quantity, unit.getKey(), player.getRole().getKey(),
				player.getUser().getUsername());
	}

	public List<DivisionClientModel> getUnnasignedUnits(Participant player) {
		return player.getUnassignedUnits().values().stream().filter(u -> u.getQuantity() != 0)
				.map(u -> new DivisionClientModel(u.getKey(),
						translate.getMessage(player.getGame().getScenario().getTitleKey() + "."
								+ player.getRole().getKey() + "." + u.getUnit().getKey(), userService.getUser()),
						u.getQuantity(), 0, 0, 0, 0))
				.collect(Collectors.toList());
	}

	public int count(Participant player) {
		int sum = 0;
		for (UnassignedUnits units : player.getUnassignedUnits().values()) {
			sum += units.getQuantity();
		}
		return sum;
	}

	public void allUnitsSet(GameEvent event) {
		int sum = 0;
		for (Participant player : gameService.getGame(event).getParticipants().values()) {
			sum += count(player);
		}
		if (sum == 0) {
			fsm.triggerTransition(event, Events.UNITS_READY);
			stompService.refreshClient(event);
		}
	}
}
