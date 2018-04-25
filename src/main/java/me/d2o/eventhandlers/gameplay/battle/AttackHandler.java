/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.eventhandlers.gameplay.exceptions.InvalidThrowException;
import me.d2o.eventhandlers.gameplay.exceptions.NoUnitsAvailableException;
import me.d2o.eventhandlers.gameplay.exceptions.TerritoriesDontShareBorderException;
import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.scenario.UnitService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleRequest;

/**
 * Class: AttackHandler
 *
 * @author bo.hanssen
 * @since Feb 2, 2017 5:15:49 PM
 *
 */
@Service
@Transactional
public class AttackHandler extends GameEventHandler {

	@Autowired
	private BattleService battleService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private DivisionService divisionService;

	@Override
	public void handleEvent(GameEvent event) {
		BattleRequest request = (BattleRequest) event.getBody();
		logger.info("{}", request);//
		Participant player = participantService.getParticipant(event);
		Battle battle = battleService.initializeBattle(event);
		battle.setAggressor(territoryService.getTerritory(event.getGameId(), request.getAgressorId()));
		battle.setDefendor(territoryService.getTerritory(event.getGameId(), request.getDefendorId()));
		this.checkOwnerShip(battle);
		this.checkBorder(battle);
		this.checkUnitsAvailable(battle, request);
		Unit unit = unitService.getUnit(request.getUnitKey(), player.getRole());
		this.checkDiceThrow(request, unit);
		battle.setOffence(request.getResult());
		battle.setOffenceUnit(unit);
		this.setPropagation(event, battle);
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_ATTACK;
	}

	public void setPropagation(GameEvent event, Battle battle) {
		if ("Neutral".equals(battle.getDefendor().getRole().getKey())) {
			event.setPropagate(Events.ATTACK_NEUTRAL);
		} else if (divisionService.totalUnits(battle.getDefendor()) == 0) {
			event.setPropagate(Events.NO_RESISTANCE);
		} else if ("AI".equals(battle.getDefendor().getUser().getRole())) {
			event.setPropagate(Events.AI_START_DEFENCE);
		} else {
			event.setPropagate(Events.START_DEFENCE);
		}
	}

	public void checkOwnerShip(Battle battle) {
		if (battle.getAggressor().getRole().equals(battle.getDefendor().getRole())) {
			throw new InvalidThrowException();
		}
	}

	public void checkBorder(Battle battle) {
		if (!battle.checkBorder()) {
			throw new TerritoriesDontShareBorderException();
		}
	}

	public void checkUnitsAvailable(Battle battle, BattleRequest request) {
		if (battle.getAggressor().getDivisions().get(request.getUnitKey()).getActive() < 1) {
			throw new NoUnitsAvailableException();
		}
	}

	public void checkDiceThrow(BattleRequest request, Unit unit) {
		if (request.getResult().size() != unit.getOffence()) {
			throw new InvalidThrowException();
		}
	}
}
