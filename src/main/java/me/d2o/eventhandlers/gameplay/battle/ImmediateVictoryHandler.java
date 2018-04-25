/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Battle;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: ImmediateVictoryHandler
 *
 * @author bo.hanssen
 * @since Feb 4, 2017 9:22:34 PM
 *
 */
@Service
@Transactional
public class ImmediateVictoryHandler extends GameEventHandler {

	@Autowired
	private BattleService battleService;

	@Autowired
	private DivisionService divisionService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		Battle battle = battleService.getBattle(event);
		divisionService.clearAll(battle.getDefendor());
		battle.getDefendor().setRole(battle.getAggressor().getRole());
		battle.getDefendor().setUser(battle.getAggressor().getUser());
		battle.getDefendor().setTown(false);
		battle.setOutcome(1);
		stomp.pushTerritoryUpdate(battle.getDefendor());
		stomp.pushInfo(event);
		event.setPropagate(Events.BATTLE_CHECK_OWNER);
	}

	@Override
	public String eventType() {
		return Events.CHANGE_OWNER;
	}

}
