/**
 *
 */
package me.d2o.eventhandlers.artificalinteligence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.DivisionTransfer;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Region;
import me.d2o.service.gameplay.DivisionTransferService;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.utils.ai.LoopController;

/**
 * Class: MobilisationAI
 *
 * @author bo.hanssen
 * @since Feb 22, 2017 8:44:41 AM
 *
 */
@Service
@Transactional
public class MobilisationAI extends GameEventHandler {

	@Autowired
	private ParticipantService participantService;

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
		Participant player = participantService.getParticipant(event);
		List<Territory> territories = territoryService.getTerritories(player);
		LoopController controller = new LoopController();
		do {
			controller.deactivate();
			for (Territory territory : territories) {
				proposeTransfers(territory, event, controller);
			}
			logger.info("Finished one itteration.");
		} while (controller.isActive());
		event.setPropagate(Events.MOBILISATION_SUBMT);
	}

	public void proposeTransfers(Territory territory, GameEvent event, LoopController controller) {
		for (Region region : territory.getRegion().getBorders()) {
			Territory territory2 = territoryService.getTerritory(event.getGameId(), region.getId());
			move(territory, territory2, controller, event);
		}
	}

	public void move(Territory from, Territory to, LoopController controller, GameEvent event) {
		int x = from.isTown() ? 5 : 2;
		if (from.getRole().equals(to.getRole()) && (territoryService.getTotalUnits(from) > x)
				&& (territoryService.getTotalUnits(from) > territoryService.getTotalUnits(to))) {
			Division division = territoryService.getUnit(from);
			if (division != null) {
				DivisionTransfer transfer = divisionTransferService.getTransfer(from, to, division.getKey());
				transfer.setGame(gameService.getGame(event));
				transfer.addOne();
				division.killOne();
				to.addTransfer(transfer);

				stomp.pushTerritoryUpdate(from);
				stomp.pushTerritoryUpdate(to);
				logger.info("Transfer one: {}: {} -> {}", division.getKey(), from, to);
				controller.activate();
			}
		}
	}

	@Override
	public String eventType() {
		return Events.AI_MOBILISATION;
	}

}
