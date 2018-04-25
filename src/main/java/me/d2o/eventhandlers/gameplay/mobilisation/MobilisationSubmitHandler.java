/**
 *
 */
package me.d2o.eventhandlers.gameplay.mobilisation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.DivisionTransfer;
import me.d2o.service.gameplay.DivisionTransferService;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: MobilisationSubmitHandler
 *
 * @author bo.hanssen
 * @since Feb 5, 2017 1:20:49 AM
 *
 */
@Service
@Transactional
public class MobilisationSubmitHandler extends GameEventHandler {

	@Autowired
	private DivisionTransferService divisionTransferService;

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		List<DivisionTransfer> transfers = divisionTransferService.getTransfers(event);
		logger.info("Submit transfers [{}]", transfers.size());
		transfers.forEach(dt -> {
			dt.submit();
			stomp.pushTerritoryUpdate(dt.getConsumer());
			stomp.pushTerritoryUpdate(dt.getSuplier());
			divisionTransferService.remove(dt);
		});
		event.setPropagate(Events.USER_CONNECTED);
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_MOBILISATION_SUBMIT;
	}

}
