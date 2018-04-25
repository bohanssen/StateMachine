/**
 *
 */
package me.d2o.eventhandlers.gameplay.battle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Territory;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleTransfer;
import me.d2o.utils.i18n.Translate;

/**
 * Class: BattleOwnerHandler
 * 
 * @author bo.hanssen
 * @since Mar 1, 2017 4:59:19 PM
 *
 */
@Service
@Transactional
public class BattleOwnerHandler extends GameEventHandler {

	@Autowired
	private BattleService battleService;

	@Autowired
	private DivisionService divisionService;
	
	@Autowired
	private Translate translate;
	
	@Autowired
	private StompService stomp;
	
	public String getName(Territory territory){
		return translate.getMessage(territory.getGame().getScenario().getTitleKey()+"."+territory.getRegion().getKey(), territory.getRegion().getKey(),territory.getUser());
	}
	
	@Override
	public void handleEvent(GameEvent event) {
		Battle battle = battleService.getBattle(event);
		BattleTransfer transfer = new BattleTransfer();
		logger.info("Owner changed notify transfer");
		if (battle.getOutcome() == 1) {
			fillTransfer(transfer, battle.getAggressor(), battle.getDefendor());
		} else if (battle.getOutcome() == 2) {
			fillTransfer(transfer, battle.getDefendor(), battle.getAggressor());
		}
		stomp.push("battleTransfer", transfer, battle.getAggressor().getUser());
	}

	public void fillTransfer(BattleTransfer transfer,Territory winner, Territory loser){
		transfer.setConsumer(getName(loser));
		transfer.setSuplier(getName(winner));
		transfer.setAvailable(divisionService.getDivisions(winner));
		transfer.setSuplierId(winner.getRegion().getId());
		transfer.setConsumerId(loser.getRegion().getId());
	}
	
	@Override
	public String eventType() {
		return Events.EXECUTE_BATTLE_CHECK_OWNER;
	}

}
