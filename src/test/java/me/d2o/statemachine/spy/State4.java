package me.d2o.statemachine.spy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.d2o.statemachine.annotations.EnterMachineState;
import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.core.StateMachineService;

@Component
public class State4 {
	
	@Autowired
	private StateMachineService fsm;
	
	@EnterMachineState(States.STATE_4)
	public void enterState(MachineEvent event) {
		fsm.triggerAsynchronousTransition(event, Events.EVENT_1);
		event.setPropagate(Events.EVENT_3);
	}

}
