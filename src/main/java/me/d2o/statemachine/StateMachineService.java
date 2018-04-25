/**
 *
 */
package me.d2o.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class: StateMachineService
 *
 * @author bo.hanssen
 * @since Jan 22, 2017 11:28:54 PM
 *
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StateMachineService {

	@Autowired
	private ApplicationEventPublisher publischer;

	public void triggerTransition(String machineId, String event, Object body) {
		TransitEvent e = new TransitEvent("");
		e.setEvent(event);
		e.setMachineId(machineId);
		e.setBody(body);
		publischer.publishEvent(e);
	}

	public void triggerTransition(AbstractMachineEvent event, String transitionEvent) {
		TransitEvent transit = new TransitEvent("");
		transit.copy(event);
		transit.setEvent(transitionEvent);
		publischer.publishEvent(transit);
	}
	
	public void triggerTransition(String machineId, String event) {
		triggerTransition(machineId,event,null);
	}
	
	@Async
	public void triggerAsynchronousTransition(String machineId, String event, Object body) {
		triggerTransition(machineId,event,body);
	}

	@Async
	public void triggerAsynchronousTransition(String machineId, String event) {
		triggerTransition(machineId,event,null);
	}

	@Async
	public void triggerAsynchronousTransition(AbstractMachineEvent event, String transitionEvent) {
		triggerTransition(event,transitionEvent);
	}
	
	protected MachineEvent triggerMachineEvent(AbstractMachineEvent event, String transitionEvent) {
		MachineEvent transit = new MachineEvent("");
		transit.copy(event);
		transit.setEvent(transitionEvent);
		publischer.publishEvent(transit);
		return transit;
	}

}
