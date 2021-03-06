/**
 *
 */
package me.d2o.statemachine.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import me.d2o.statemachine.exceptions.StateMachineConfigurationException;

@Configuration
@EnableAsync
public class StateMachineConfigurable {

	private static final Logger logger = LoggerFactory.getLogger(StateMachineConfigurable.class);

	private Map<String, MachineTransition> transitions;
	private List<String> events = new ArrayList<>();
	private List<String> states = new ArrayList<>();
	private Class<?> stateClass;
	
	
	public void checkIfStateIsValid(String state){
		if (!states.contains(state)){
			throw new StateMachineConfigurationException("["+state+"] is not a valid state because it is not declared in the states class ["+stateClass+"]");
		}
	}
	
	public StateMachineConfigurable(Class<?> events, Class<?> states) {
		logger.info("New Event driven StateMachine configuration");
		logger.info("Event configuration class: {}",events);
		logger.info("State configuration class: {}",states);
		stateClass = states;
		try {
			checkStaticFinalFields(events,'E');
			checkStaticFinalFields(states,'S');
		} catch (StateMachineConfigurationException ex) {
			logger.error("Could not configure statemachine",ex);
			throw ex;
		} catch (Exception e){
			throw new StateMachineConfigurationException(e);
		}
		transitions = new HashMap<>();
	}

	private void checkStaticFinalFields(Class<?> configurable, char config) throws IllegalArgumentException, IllegalAccessException{
		List<String> values = new ArrayList<>();
		for (Field field : configurable.getFields()){
			if ( field.getType().isAssignableFrom(String.class) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())){
				String val = (String) field.get("");
				if (values.contains(val)){
					throw new StateMachineConfigurationException("Value is not unique: "+field.toString());
				}
				values.add(val);
				if (config == 'E') {
					events.add(val);
				} else if (config == 'S'){
					states.add(val);
				}
			} else {
				throw new StateMachineConfigurationException("Please use only public static final String fields in your configuration: "+configurable.getName());
			}
		}
	}
	
	public void addTransition(String event, String currentState, String targetState) {
		if (!events.contains(event) || !states.contains(currentState) || !states.contains(targetState)){
			StateMachineConfigurationException ex = new StateMachineConfigurationException("Passed an invalid event or state to the Machine configuration");
			logger.error("Could not register transition",ex);
			throw ex;
		}
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
	
	public static void main(String[] args) {
	}
}
