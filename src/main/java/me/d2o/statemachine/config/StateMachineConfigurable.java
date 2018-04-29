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
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import me.d2o.statemachine.exceptions.StateMachineConfigurationException;
import me.d2o.statemachine.utils.MachineTransition;

@Configuration
@EnableJpaRepositories("me.d2o.statemachine")
@EntityScan("me.d2o.statemachine")
@EnableAsync
public class StateMachineConfigurable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, MachineTransition> transitions;
	private List<String> events = new ArrayList<>();
	private List<String> states = new ArrayList<>();
	private Class<?> eventClass;
	
	public void checkIfEventIsValid(String event){
		if (!events.contains(event)){
			throw new StateMachineConfigurationException("["+event+"] is not a valid event because it is not declared in the events class ["+eventClass+"]");
		}
	}
	
	public StateMachineConfigurable(Class<?> events, Class<?> states) {
		eventClass = events;
		checkStaticFinalFields(events,'E');
		checkStaticFinalFields(states,'S');
		transitions = new HashMap<>();
	}

	private void checkStaticFinalFields(Class<?> configurable, char config){
		List<String> values = new ArrayList<>();
		for (Field field : configurable.getFields()){
			if ( field.getType().isAssignableFrom(String.class) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())){
					try {
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
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new StateMachineConfigurationException(e);
					}
			} else {
				throw new StateMachineConfigurationException("Please use only public static final String fields in your configuration: "+configurable.getName());
			}
		}
	}
	
	public void addTransition(String event, String currentState, String targetState) {
		if (!events.contains(event) || !states.contains(currentState) || !states.contains(targetState)){
			throw new StateMachineConfigurationException("Passed an invalid event or state to the Machine configuration");
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
