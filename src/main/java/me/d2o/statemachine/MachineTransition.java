/**
 *
 */
package me.d2o.statemachine;

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
	private String propagationEvent;
	private String aiEvent;
	private boolean checkTurn;

	/**
	 * @param event
	 * @param currentState
	 * @param targetState
	 * @param propagationEvent
	 */
	public MachineTransition(String event, String currentState, String targetState, String propagationEvent) {
		this(event, currentState, targetState, propagationEvent, false);
	}

	public MachineTransition(String event, String currentState, String targetState, String propagationEvent,
			boolean checkTurn) {
		this(event, currentState, targetState, propagationEvent, "", checkTurn);
	}

	public MachineTransition(String event, String currentState, String targetState, String propagationEvent,
			String aiEvent, boolean checkTurn) {
		super();
		this.event = event;
		this.currentState = currentState;
		this.targetState = targetState;
		this.propagationEvent = propagationEvent;
		this.aiEvent = aiEvent;
		this.checkTurn = checkTurn;
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

	public String getPropagationEvent() {
		return propagationEvent;
	}

	public boolean isCheckTurn() {
		return checkTurn;
	}

	public String getAiEvent() {
		return aiEvent;
	}

	@Override
	public String toString() {
		return "MachineTransition [event=" + event + ", currentState=" + currentState + ", targetState=" + targetState
				+ ", propagationEvent=" + propagationEvent + "]";
	}

}
