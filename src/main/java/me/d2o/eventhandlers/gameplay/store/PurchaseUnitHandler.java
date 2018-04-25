/**
 *
 */
package me.d2o.eventhandlers.gameplay.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.eventhandlers.gameplay.exceptions.NoGoldException;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.service.gameplay.DivisionService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.service.scenario.UnitService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.store.PurchaseUnitRequest;

/**
 * Class: PurchaseUnitHandler
 *
 * @author bo.hanssen
 * @since Jan 31, 2017 9:38:10 AM
 *
 */
@Service
@Transactional
public class PurchaseUnitHandler extends GameEventHandler {

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private StompService stomp;

	@Autowired
	private DivisionService divisionService;

	@Override
	public void handleEvent(GameEvent event) {
		PurchaseUnitRequest request = (PurchaseUnitRequest) event.getBody();
		if (request.getUnitKey().startsWith("item")) {
			purchaseItem(event, request);
		} else {
			Territory territory = territoryService.getTerritory(request.getGameId(), request.getRegion());

			if (!territoryService.checkOwnership(territory)) {
				return;
			}

			Participant player = participantService.getParticipant(event);
			Unit unit = unitService.getUnit(request.getUnitKey(), player.getRole());
			checkGold(player, unit.getCost());

			Division division = divisionService.getOrCreateDivision(territory, unit);
			division.incrementQuantity(1);
			player.editCash(-unit.getCost());
			logger.info("Deploy unit [{}] to region [{}] for role [{}]", unit.getKey(), territory.getKey(),
					territory.getRole().getKey());
			stomp.pushTerritoryUpdate(territory);
			stomp.push("info", participantService.getInfo(event.getGameId()));
		}
	}

	public void purchaseItem(GameEvent event, PurchaseUnitRequest request) {
		Participant player = participantService.getParticipant(event);
		int cost = player.getRole().getItems().get(request.getUnitKey().replaceAll("item.", ""));
		checkGold(player, cost);
		if ("item.capitol".equals(request.getUnitKey())) {
			player.setUndeployedtown(player.getUndeployedtown() + 1);
		}
		player.editCash(-cost);
		stomp.push("info", participantService.getInfo(event.getGameId()));
		event.setPropagate(Events.PURCHASE_TOWN);
	}

	public void checkGold(Participant player, int cost) {
		if (player.getCash() < cost) {
			throw new NoGoldException();
		}
	}

	@Override
	public String eventType() {
		return Events.PURCHASE_UNIT;
	}

}
