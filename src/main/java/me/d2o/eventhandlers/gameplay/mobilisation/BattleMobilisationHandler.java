package me.d2o.eventhandlers.gameplay.mobilisation;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.config.statemachine.Events;
import me.d2o.eventhandlers.gameplay.exceptions.RegionNotYoursException;
import me.d2o.eventhandlers.gameplay.exceptions.TerritoriesDontShareBorderException;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.DivisionTransfer;
import me.d2o.persistence.model.game.Territory;
import me.d2o.service.gameplay.DivisionTransferService;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.battle.BattleTransferRequest;

@Service
@Transactional
public class BattleMobilisationHandler extends GameEventHandler {

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private DivisionTransferService divisionTransferService;
	
	@Autowired
	private GameService gameService;

	@Autowired
	private StompService stomp;
	
	@Override
	public void handleEvent(GameEvent event) {
		BattleTransferRequest request =(BattleTransferRequest) event.getBody();
		Territory from = territoryService.getTerritory(request.getGameId(), request.getSuplier());
		Territory to = territoryService.getTerritory(request.getGameId(), request.getConsumer());
		logger.info("Transferint units from [{}] to [{}] transferrequest [{}]",from,to,request);
		if (from.getUser().getId() != event.getUserId() || to.getUser().getId() != event.getUserId()) {
			throw new RegionNotYoursException();
		}

		if (!from.getRegion().getBorders().contains(to.getRegion())) {
			throw new TerritoriesDontShareBorderException();
		}
		
		request.getTransfers().forEach((key,quantity) -> {
			Division fromDivision = from.getDivisions().get(key);
			DivisionTransfer transfer = divisionTransferService.getTransfer(from, to, key);
			transfer.setGame(gameService.getGame(event));
			for (int i = 0; i < quantity; i++){
				transfer.addOne();
				fromDivision.killOne();
			}
			transfer.submit();
		});
		stomp.pushTerritoryUpdate(from);
		stomp.pushTerritoryUpdate(to);
		gameService.continueFlowAsTurnUser(event);
		event.setPropagate(Events.USER_CONNECTED);
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_BATTLE_TRANSFER;
	}

}
