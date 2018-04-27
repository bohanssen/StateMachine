/**
 *
 */
package me.d2o.statemachine.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.exceptions.TransitionException;

/**
 * Class: MachineEventHandler
 *
 * @author bo.hanssen
 * @since Jan 23, 2017 1:29:44 PM
 *
 */
@Component
@Transactional
public abstract class MachineEventHandler {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public abstract void handleEvent(MachineEvent event);

	public abstract String eventType();

	public boolean preCheck(MachineEvent event){
		return true;
	}
	
	@EventListener
	private void listner(MachineEvent event) {
		if (event.getEvent().equals(eventType())) {
			if (preCheck(event)){
				handleEvent(event);
			} else {
				throw new TransitionException("Precheck returned false");
			}
		}
	}
}
