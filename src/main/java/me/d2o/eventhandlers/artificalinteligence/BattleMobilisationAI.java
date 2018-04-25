package me.d2o.eventhandlers.artificalinteligence;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Territory;
import me.d2o.service.gameplay.BattleService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleTransferRequest;

@Service
@Transactional
public class BattleMobilisationAI extends GameEventHandler{

	@Autowired
	private BattleService battleService;
	
	@Override
	public void handleEvent(GameEvent event) {
		Battle battle = battleService.getBattle(event);
		logger.info("AI Battle mobilisation: Battle[{}]",battle);
		BattleTransferRequest request = new BattleTransferRequest();
		request.setGameId(event.getGameId());
		Territory from = null;
		if (battle.getOutcome() == 1) {
			request.setSuplier(battle.getAggressor().getRegion().getId());
			request.setConsumer(battle.getDefendor().getRegion().getId());
			from = battle.getAggressor();
		} else if (battle.getOutcome() == 2) {
			from = battle.getDefendor();
			request.setSuplier(battle.getDefendor().getRegion().getId());
			request.setConsumer(battle.getAggressor().getRegion().getId());
		}
		request.setTransfers(new HashMap<String,Integer>());
		from.getDivisions().values().forEach(d ->{
			if (d.getQuantity() != 0){
				request.getTransfers().put(d.getKey(), d.getQuantity()/2);
			}
		});
		event.setBody(request);
		event.setPropagate(Events.BATTLE_TRANSFER_REQUEST);
	}

	@Override
	public String eventType() {
		return Events.AI_BATTLE_MOBILISATION;
	}

}
