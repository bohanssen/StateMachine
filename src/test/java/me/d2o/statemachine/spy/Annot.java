package me.d2o.statemachine.spy;

import org.springframework.stereotype.Component;

import me.d2o.statemachine.annotations.EnterMachineState;
import me.d2o.statemachine.annotations.ExitMachineState;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineEvent;

@Component
public class Annot {

	@EnterMachineState(States.STATE_1)
	public void execute(MachineEvent event){
		System.out.println("Enter 1");
	}
	
	@ExitMachineState(States.STATE_1)
	public void execute2(MachineEvent event){
		System.out.println("Exit 1");
	}
}
