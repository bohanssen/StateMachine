/**
 *
 */
package me.d2o.eventhandlers.gameplay.mobilisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.eventhandlers.gameplay.exceptions.NoUnitsAvailableException;
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
import me.d2o.transfermodel.gameplay.mobilisation.MobilisationRequest;

/**
 * Class: TransferHandler
 *
 * @author bo.hanssen
 * @since Feb 4, 2017 11:35:23 PM
 *
 */
@Service
@Transactional
public class TransferHandler extends GameEventHandler {

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
		MobilisationRequest request = (MobilisationRequest) event.getBody();
		Territory from = territoryService.getTerritory(request.getGameId(), request.getSource());
		Territory to = territoryService.getTerritory(request.getGameId(), request.getTarget());

		if (from.getUser().getId() != event.getUserId() || to.getUser().getId() != event.getUserId()) {
			throw new RegionNotYoursException();
		}

		if (!from.getRegion().getBorders().contains(to.getRegion())) {
			throw new TerritoriesDontShareBorderException();
		}

		Division fromDivision = from.getDivisions().get(request.getUnitKey());
		if (fromDivision.getQuantity() < 1) {
			throw new NoUnitsAvailableException();
		}

		DivisionTransfer transfer = divisionTransferService.getTransfer(from, to, request.getUnitKey());
		transfer.addOne();
		transfer.setGame(gameService.getGame(event));
		fromDivision.killOne();
		to.addTransfer(transfer);

		stomp.pushTerritoryUpdate(from);
		stomp.pushTerritoryUpdate(to);
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_MOBILISATION;
	}

}
