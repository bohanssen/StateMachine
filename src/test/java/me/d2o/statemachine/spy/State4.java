package me.d2o.statemachine.spy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.core.StateMachineService;
import me.d2o.statemachine.eventhandler.MachineState;

@Service
public class State4 extends MachineState {
	
	@Autowired
	private StateMachineService fsm;
	
	@Override
	public void enterState(MachineEvent event) {
		fsm.triggerAsynchronousTransition(event, Events.EVENT_1);
		event.setPropagate(Events.EVENT_3);
	}

	@Override
	public void exitState(MachineEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String state() {
		return States.STATE_4;
	}



}
