/**
 *
 */
package me.d2o.statemachine.eventhandler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
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
public abstract class MachineState {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StateMachineConfigurable smc;
	
	public abstract void enterState(MachineEvent event);

	public abstract void exitState(MachineEvent event);
	
	public abstract String state();

	public boolean enterCheck(MachineEvent event){
		return true;
	}
	
	public boolean exitCheck(MachineEvent event){
		return true;
	}
	
	@PostConstruct
	void validate(){
		try {
			smc.checkIfStateIsValid(state());
		} catch (StateMachineConfigurationException ex){
			MachineEventHandlerConfigurationException e = new MachineEventHandlerConfigurationException("Could not construct the MachineEventHandler ["+this.getClass()+"] because the 'State' method returns an invalid String",ex);
			logger.error("Bad configuration",e);
			throw e;
		}
	}
	
	
	@Order(100)
	@EventListener
	protected void enterListener(MachineEvent event) {
		//System.out.println(this.getClass().getName() +": "+!event.isTerminated() +" "+ event.getEnterState().equals(state()));
		if (!event.isTerminated() && event.getEnterState().equals(state())) {
			System.out.println("enter: "+this.getClass().getName());
			if (enterCheck(event)){
				enterState(event);
			} else {
				throw new TransitionException("Entercheck returned false");
			}
		}
	}
	
	@Order(50)
	@EventListener
	protected void exitListener(MachineEvent event) {
		if (event.getExitState().equals(state())) {
			System.out.println("exit: "+this.getClass().getName());
			if (exitCheck(event)){
				exitState(event);
			} else {
				throw new TransitionException("Exitcheck returned false");
			}
		}
	}
}
