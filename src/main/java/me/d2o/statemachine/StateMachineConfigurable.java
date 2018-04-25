/**
 *
 */
package me.d2o.statemachine;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class: StateMachine This is my attempt to a finite state machine, replacing
 * the old Flow (switch/case) aproach.
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 2:14:28 PM
 *
 */
public class StateMachineConfigurable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, MachineTransition> transitions;

	public StateMachineConfigurable() {
		transitions = new HashMap<>();
	}

	public void addTransition(String event, String currentState, String targetState) {
		transitions.put(event+currentState, new MachineTransition(event, currentState, targetState));
	}

	public MachineTransition getTransition(String event, String currentState) {
		String key = event + currentState;
		if (!transitions.containsKey(key)) {
			logger.warn("Received an invalid trigger [{} : {}]", event, currentState);
			return null;
		}
		return transitions.get(key);
	}
}
