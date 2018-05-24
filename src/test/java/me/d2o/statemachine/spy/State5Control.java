package me.d2o.statemachine.spy;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.eventhandler.MachineState;

@Service
public class State5Control extends MachineState {

	public static boolean entered = false;
	public static boolean exited = false;


	@Override
	public String state() {
		return States.STATE_5;
	}


	@Override
	public void enterState(MachineEvent event) {
		entered = true;
	}

	@Override
	public void exitState(MachineEvent event) {
		exited = true;
	}


	@Override
	@Order(51)
	@EventListener
	protected void enterListener(MachineEvent event) {
		// TODO Auto-generated method stub
		super.enterListener(event);
	}

}
