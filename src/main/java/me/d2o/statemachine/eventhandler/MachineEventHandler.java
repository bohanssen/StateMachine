/**
 *
 */
package me.d2o.statemachine.eventhandler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.statemachine.config.StateMachineConfigurable;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.exceptions.MachineEventHandlerConfigurationException;
import me.d2o.statemachine.exceptions.StateMachineConfigurationException;
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

	@Autowired
	private StateMachineConfigurable smc;
	
	public abstract void handleEvent(MachineEvent event);

	public abstract String eventType();

	public boolean preCheck(MachineEvent event){
		return true;
	}
	
	@PostConstruct
	private void validate(){
		try {
			smc.checkIfEventIsValid(eventType());
		} catch (StateMachineConfigurationException ex){
			MachineEventHandlerConfigurationException e = new MachineEventHandlerConfigurationException("Could not construct the MachineEventHandler ["+this.getClass()+"] because the eventType method returns an invalid String",ex);
			logger.error("Bad configuration",e);
			throw e;
		}
	}
	
	@EventListener
	void listner(MachineEvent event) {
		if (event.getEvent().equals(eventType())) {
			if (preCheck(event)){
				handleEvent(event);
			} else {
				throw new TransitionException("Precheck returned false");
			}
		}
	}
}
