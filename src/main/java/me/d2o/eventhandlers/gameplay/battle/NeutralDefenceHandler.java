/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Battle;
import me.d2o.service.gameplay.BattleService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: NeutralDefenceHandler
 *
 * @author bo.hanssen
 * @since Feb 4, 2017 8:53:17 PM
 *
 */
@Service
@Transactional
public class NeutralDefenceHandler extends GameEventHandler {

	private Random rn = new Random();

	@Autowired
	private BattleService battleService;

	@Override
	public void handleEvent(GameEvent event) {
		Battle battle = battleService.getBattle(event);
		battle.setDefenceUnit(battle.getDefendor().getDivisions().get("neutraldefence").getUnit());
		List<Integer> defence = new ArrayList<>();
		for (int i = 0; i < battle.getDefenceUnit().getDefence(); i++) {
			defence.add(rn.nextInt(6) + 1);
		}
		battle.setDefence(defence);
		event.setPropagate(Events.CHECK_BATTLE);
	}

	@Override
	public String eventType() {
		return Events.NEUTRAL_DEFENCE;
	}

}
