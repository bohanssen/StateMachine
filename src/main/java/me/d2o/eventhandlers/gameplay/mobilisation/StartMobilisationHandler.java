/**
 *
 */
package me.d2o.eventhandlers.gameplay.mobilisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.general.StompService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: StartMobilisationHandler
 * 
 * @author bo.hanssen
 * @since Feb 4, 2017 10:10:08 PM
 *
 */
@Service
@Transactional
public class StartMobilisationHandler extends GameEventHandler {

	@Autowired
	private StompService stomp;

	@Override
	public void handleEvent(GameEvent event) {
		stomp.push("openmobilisationmode", true);
	}

	@Override
	public String eventType() {
		return Events.START_MOBILISATIONMODE;
	}

}
