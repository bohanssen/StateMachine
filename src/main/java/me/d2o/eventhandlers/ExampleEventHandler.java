/**
 *
 */
package me.d2o.eventhandlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.statemachine.MachineEvent;
import me.d2o.statemachine.MachineEventHandler;

/**
 * Class: CapitolInitializeHandler
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:20:54 AM
 *
 */
@Service
@Transactional
public class ExampleEventHandler extends MachineEventHandler {

	@Override
	public void handleEvent(MachineEvent event) {
		logger.info("Received event [{}]", event);
		
	}

	@Override
	public String eventType() {
		return Events.NOTIFY_PLACE_TOWN;
	}

}
