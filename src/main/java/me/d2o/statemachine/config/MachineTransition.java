/**
 *
 */
package me.d2o.statemachine.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class: MachineTransition
 *
 * @author bo.hanssen
 * @since Jan 21, 2017 11:39:17 PM
 *
 */
public class MachineTransition {

	private static final Logger logger = LoggerFactory.getLogger(MachineTransition.class);
	
	private String event;
	private String currentState;
	private String targetState;

	/**
	 * @param event Static final string from the event class
	 * @param currentState Static final string from the state class
	 * @param targetState Static final string from the state class
	 */
	public MachineTransition(String event, String currentState, String targetState) {
		this.event = event;
		this.currentState = currentState;
		this.targetState = targetState;
		logger.info("New: {}",this);
	}

	public String getEvent() {
		return event;
	}

	public String getCurrentState() {
		return currentState;
	}

	public String getTargetState() {
		return targetState;
	}

	@Override
	public String toString() {
		return "MachineTransition [event=" + getEvent() + ", currentState=" + getCurrentState() + ", targetState=" + getTargetState()
				+ "]";
	}

}
