/**
 *
 */
package me.d2o.statemachine.core;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.statemachine.abstractevents.AbstractMachineEvent;

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

	private static final Logger logger = LoggerFactory.getLogger(StateMachineService.class);
	
	@Autowired
	private ApplicationEventPublisher publischer;

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(propagation = Propagation.REQUIRED)
	protected StateMachine getStateMachineById(String id){
		logger.debug("Retrieve Machine [{}] from DB",id);
		try {
			TypedQuery<StateMachine> query = em.createQuery("select m from StateMachine m where m.machineID = ?1", StateMachine.class);
			query.setParameter(1, id);
			return query.getSingleResult();
		} catch (NoResultException ex){
			logger.debug("Retrieving Machine [{}] from DB failed",id);
			return null;
		}
	}
	
	public void triggerTransition(String machineId, String event, Object body) {
		TransitEvent e = new TransitEvent("");
		e.setEvent(event);
		e.setMachineId(machineId);
		e.setBody(body);
		logger.debug("Trigger event [{}]",e);
		publischer.publishEvent(e);
	}

	public void triggerTransition(AbstractMachineEvent event, String transitionEvent) {
		TransitEvent transit = new TransitEvent("");
		transit.copy(event);
		transit.setEvent(transitionEvent);
		logger.debug("Trigger event [{}]",transit);
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
	
	MachineEvent triggerMachineEvent(AbstractMachineEvent event, String transitionEvent) {
		MachineEvent transit = new MachineEvent("");
		transit.copy(event);
		transit.setEvent(transitionEvent);
		publischer.publishEvent(transit);
		logger.debug("Trigger event [{}]",transit);
		return transit;
	}

}
