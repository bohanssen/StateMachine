package me.d2o.statemachine.spy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.core.StateMachineService;
import me.d2o.statemachine.eventhandler.MachineEventHandler;

@Service
public class Event4 extends MachineEventHandler {

	public static boolean triggered = false;
	
	@Autowired
	private StateMachineService fsm;
	
	@Override
	public void handleEvent(MachineEvent event) {
		fsm.triggerAsynchronousTransition(event, Events.EVENT_2);
		event.setPropagate(Events.EVENT_3);
		triggered = true;
	}

	@Override
	public String eventType() {
		return Events.EVENT_4;
	}



}
