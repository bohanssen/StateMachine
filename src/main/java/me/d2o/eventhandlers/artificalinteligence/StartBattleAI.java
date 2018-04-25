/**
 *
 */
package me.d2o.eventhandlers.artificalinteligence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Region;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleRequest;
import me.d2o.utils.ai.BattleProposalAI;
import me.d2o.utils.ai.TimeUtils;

/**
 * Class: StartBattleAI
 *
 * @author bo.hanssen
 * @since Feb 21, 2017 12:51:45 PM
 *
 */
@Service
@Transactional
public class StartBattleAI extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private DivisionService divisionService;

	private Random random;

	@PostConstruct
	public void init() {
		random = new Random();
	}

	@Override
	public void handleEvent(GameEvent event) {
		Participant player = participantService.getParticipant(event);
		List<Territory> territories = territoryService.getTerritories(player);
		
		double aggression = ((float) territories.size()) / ((float) (player.getGame().getTerritories().size()/2) );
		aggression = Math.pow(aggression, 10) *100;
		
		BattleProposalAI proposal = getProposal(territories, event, aggression);
		if (proposal.getAgressor() != null) {
			List<Integer> result = new ArrayList<>();
			for (int i = 0; i < proposal.getUnit().getOffence(); i++) {
				result.add(random.nextInt(6) + 1);
			}
			event.setBody(new BattleRequest(proposal, event, result));
			event.setPropagate(Events.START_ATTACK);
			if (proposal.getDefendor().getUser() != null && !"AI".equals(proposal.getDefendor().getUser().getRole())) {
				TimeUtils.sleep(5000);
			}
		} else {
			event.setPropagate(Events.STOP_BATTLE_MODE);
		}
	}

	public BattleProposalAI getProposal(List<Territory> territories, GameEvent event, double aggression) {
		BattleProposalAI proposal = new BattleProposalAI();
		int highScore = 0;
		for (Territory territory : territories) {
			int score = (int) (getBaseScore(territory, event) + aggression);
			highScore = selectEnemy(proposal, territory, event, score, highScore);
		}
		return proposal;
	}

	public int getBaseScore(Territory territory, GameEvent event) {
		int score = divisionService.totalUnits(territory) * 15;
		for (Region region : territory.getRegion().getBorders()) {
			Territory enemy = territoryService.getTerritory(event.getGameId(), region.getId());
			if (validEnemy(territory, enemy)) {
				score -= divisionService.totalUnits(enemy) * 10;
				score *= enemy.isTown() && score > 0 ? 2 : 1;
			}
		}
		return score;
	}

	public int selectEnemy(BattleProposalAI proposal, Territory territory, GameEvent event, int score, int highScore) {
		int threshHold = territory.isTown() ? 5 : 2;
		if (territoryService.getTotalUnits(territory) < threshHold) {
			return highScore;
		}
		for (Region region : territory.getRegion().getBorders()) {
			Territory enemy = territoryService.getTerritory(event.getGameId(), region.getId());
			if (validEnemy(territory, enemy)) {
				int tempscore = score + divisionService.balanceAttackDefence(territory, enemy);
				logger.info("score [{}] attacker [{}] defendor [{}]", tempscore, territory, enemy);
				if (tempscore > highScore) {
					highScore = tempscore;
					proposal.setAgressor(territory);
					proposal.setDefendor(enemy);
					proposal.setUnit(divisionService.selectUnit(territory, enemy));
					logger.info("New highscore [{}] [{}]", highScore, proposal);
				}
			}
		}
		return highScore;
	}

	public boolean validEnemy(Territory territory, Territory enemy) {
		return "Neutral".equals(enemy.getRole().getKey())
				|| !territory.getUser().getUniqueUserId().equals(enemy.getUser().getUniqueUserId());
	}

	@Override
	public String eventType() {
		return Events.AI_START_BATTLE;
	}

}
