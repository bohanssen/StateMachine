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
import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.scenario.UnitService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleRequest;

/**
 * Class: DefenceHandler
 *
 * @author bo.hanssen
 * @since Feb 3, 2017 4:05:29 PM
 *
 */
@Service
@Transactional
public class DefenceHandler extends GameEventHandler {

	@Autowired
	private BattleService battleService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private ParticipantService participantService;

	@Override
	public void handleEvent(GameEvent event) {
		BattleRequest request = (BattleRequest) event.getBody();
		Participant player = participantService.getParticipant(event);
		Battle battle = battleService.getBattle(event);
		Unit unit = unitService.getUnit(request.getUnitKey(), player.getRole());

		if (battle.getDefendor().getDivisions().get(request.getUnitKey()).getActive() < 1) {
			throw new NoUnitsAvailableException();
		}
		if ((battle.getDefendor().getUser().getId() != player.getUser().getId())
				|| request.getResult().size() != unit.getDefence()) {
			throw new InvalidThrowException();
		}

		battle.setDefence(request.getResult());
		battle.setDefenceUnit(unit);
		event.setPropagate(Events.CHECK_BATTLE);
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_DEFENCE;
	}

}
