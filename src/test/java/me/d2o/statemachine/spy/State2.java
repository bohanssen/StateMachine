package me.d2o.statemachine.spy;

import org.springframework.stereotype.Component;

import me.d2o.statemachine.annotations.EnterMachineState;
import me.d2o.statemachine.annotations.ExitMachineState;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineEvent;

@Component
public class State2 {

	public static boolean entered = false;
	public static boolean exited = false;

	@EnterMachineState(States.STATE_2)
	public void enterState(MachineEvent event) {
		entered = true;
	}

	@ExitMachineState(States.STATE_2)
	public void exitState(MachineEvent event) {
		exited = true;
	}
}
