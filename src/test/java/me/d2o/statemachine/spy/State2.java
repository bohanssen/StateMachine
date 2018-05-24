package me.d2o.statemachine.spy;

import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.eventhandler.MachineState;

@Service
public class State2 extends MachineState {

	public static boolean entered = false;
	public static boolean exited = false;
	
	@Override
	public String state() {
		return States.STATE_2;
	}


	@Override
	public void enterState(MachineEvent event) {
		entered = true;
	}


	@Override
	public void exitState(MachineEvent event) {
		exited = true;
	}
}
