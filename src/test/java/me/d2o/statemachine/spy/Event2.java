package me.d2o.statemachine.spy;

import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.eventhandler.MachineEventHandler;

@Service
public class Event2 extends MachineEventHandler {

	public static boolean triggered = false;
	
	@Override
	public void handleEvent(MachineEvent event) {
		triggered = true;
	}

	@Override
	public String eventType() {
		return Events.EVENT_2;
	}



}
