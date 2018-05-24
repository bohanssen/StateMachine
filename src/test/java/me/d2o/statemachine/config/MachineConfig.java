package me.d2o.statemachine.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MachineConfig {

	 @Bean
     public StateMachineConfigurable stateMachineConfigurable() {	
 		StateMachineConfigurable fsm = new StateMachineConfigurable(Events.class,States.class);
 		
 		fsm.addTransition(Events.EVENT_1, States.STATE_1, States.STATE_2);
 		fsm.addTransition(Events.EVENT_2, States.STATE_2, States.STATE_3);
 		fsm.addTransition(Events.EVENT_3, States.STATE_3, States.STATE_1);
 		
 		fsm.addTransition(Events.EVENT_4, States.STATE_1, States.STATE_4);
 		
 		fsm.addTransition(Events.EVENT_1, States.STATE_4, States.STATE_3);
 		
 		fsm.addTransition(Events.EVENT_5, States.STATE_1, States.STATE_5);
 		
 		return fsm;
 	}
}
