/**
 *
 */
package me.d2o.eventhandlers.gameplay.mobilisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.gameplay.DivisionTransferService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: MobilisationResetHandler
 *
 * @author bo.hanssen
 * @since Feb 5, 2017 1:09:57 AM
 *
 */
@Service
@Transactional
public class MobilisationResetHandler extends GameEventHandler {

	@Autowired
	private DivisionTransferService divisionTransferService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		divisionTransferService.getTransfers(event).forEach(dt -> {
			dt.rollback();
			stomp.pushTerritoryUpdate(dt.getConsumer());
			stomp.pushTerritoryUpdate(dt.getSuplier());
		});
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_MOBILISATION_RESET;
	}

}
