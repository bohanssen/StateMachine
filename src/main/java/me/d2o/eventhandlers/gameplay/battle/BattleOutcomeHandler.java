/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Territory;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleResult;

/**
 * Class: BattleOutcomeHandler
 *
 * @author bo.hanssen
 * @since Feb 3, 2017 4:19:42 PM
 *
 */
@Service
@Transactional
public class BattleOutcomeHandler extends GameEventHandler {

	@Autowired
	private GameService gameService;

	@Autowired
	private BattleService battleService;

	@Autowired
	private DivisionService divisionService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		Battle battle = battleService.getBattle(event);
		Collections.sort(battle.getDefence(), Collections.reverseOrder());
		Collections.sort(battle.getOffence(), Collections.reverseOrder());

		int defence = 0;
		int attacker = 0;
		String defenceResult = "[" + battle.getDefence().get(0) + "] [" + battle.getDefence().get(1) + "]";
		String attackerResult = "[" + battle.getOffence().get(0) + "] [" + battle.getOffence().get(1) + "]";

		boolean defendorVictorious = true;
		for (int i = 0; i < 2; i++) {
			defence += battle.getDefence().get(i);
			attacker += battle.getOffence().get(i);
			if (battle.getDefence().get(i) < battle.getOffence().get(i)) {
				defendorVictorious = false;
				break;
			} else if (battle.getDefence().get(i) > battle.getOffence().get(i)) {
				break;
			}
		}

		if (defendorVictorious) {
			sendResult(battle, getNickname(battle.getDefendor()), getNickname(battle.getAggressor()), defenceResult,
					attackerResult, percent(defence, attacker), percent(attacker, defence));
			defendorWon(battle);
		} else {
			sendResult(battle, getNickname(battle.getAggressor()), getNickname(battle.getDefendor()), attackerResult,
					defenceResult, percent(attacker, defence), percent(defence, attacker));
			attackerWon(battle);
		}

		this.reset(battle.getAggressor());
		this.reset(battle.getDefendor());
		stomp.pushTerritoryUpdate(battle.getAggressor());
		stomp.pushTerritoryUpdate(battle.getDefendor());
		stomp.pushInfo(event);
		if (battle.getOutcome() == 0) {
			gameService.continueFlowAsTurnUser(event);
			event.setPropagate(Events.USER_CONNECTED);
		} else {
			gameService.continueFlowAsUser(event, battle.getAggressor().getUser());
			event.setPropagate(Events.BATTLE_CHECK_OWNER);
		}

	}

	@Override
	public String eventType() {
		return Events.EXECUTE_BATTLE_CHECK;
	}

	private void defendorWon(Battle battle) {
		battle.getAggressor().getDivisions().get(battle.getOffenceUnit().getKey()).killOne();
		if (divisionService.totalUnits(battle.getAggressor()) == 0) {
			divisionService.clearAll(battle.getAggressor());
			battle.getAggressor().setRole(battle.getDefendor().getRole());
			battle.getAggressor().setUser(battle.getDefendor().getUser());
			battle.getAggressor().setTown(false);
			battle.setOutcome(2);
		} else {
			battle.getDefendor().getDivisions().get(battle.getDefenceUnit().getKey()).restOne();
		}
	}

	public void attackerWon(Battle battle) {
		battle.getDefendor().getDivisions().get(battle.getDefenceUnit().getKey()).killOne();
		if (divisionService.totalUnits(battle.getDefendor()) == 0) {
			divisionService.clearAll(battle.getDefendor());
			battle.getDefendor().setRole(battle.getAggressor().getRole());
			battle.getDefendor().setUser(battle.getAggressor().getUser());
			battle.getDefendor().setTown(false);
			battle.setOutcome(1);
		} else {
			battle.getAggressor().getDivisions().get(battle.getOffenceUnit().getKey()).restOne();
		}
	}

	private void reset(Territory territory) {
		int active = 0;
		for (Division division : territory.getDivisions().values()) {
			active += division.getActive();
		}
		if (active == 0) {
			territory.getDivisions().values().forEach(d -> d.reset());
		}
	}

	public void sendResult(Battle battle, String victor, String loser, String vresult, String lresult, int vpercent,
			int lpercent) {
		BattleResult result = new BattleResult(victor, loser, vresult, lresult, vpercent, lpercent);
		stomp.push("battleresult", result, battle.getAggressor().getUser());
		stomp.push("battleresult", result, battle.getDefendor().getUser());
	}

	public int percent(int a, int b) {
		return a * 100 / (a + b);
	}

	public String getNickname(Territory territory) {
		if ("Neutral".equals(territory.getRole().getKey())) {
			return "Neutral";
		} else {
			return territory.getUser().getNickname();
		}
	}
}
