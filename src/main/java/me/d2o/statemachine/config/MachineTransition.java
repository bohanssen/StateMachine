/**
 *
 */
package me.d2o.statemachine.config;

/**
 * Class: MachineTransition
 *
 * @author bo.hanssen
 * @since Jan 21, 2017 11:39:17 PM
 *
 */
public class MachineTransition {

	private String event;
	private String currentState;
	private String targetState;

	/**
	 * @param event
	 * @param currentState
	 * @param targetState
	 * @param propagationEvent
	 */
	public MachineTransition(String event, String currentState, String targetState) {
		this.event = event;
		this.currentState = currentState;
		this.targetState = targetState;
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
		return "MachineTransition [event=" + event + ", currentState=" + currentState + ", targetState=" + targetState
				+ "]";
	}

}
