/**
 *
 */
package me.d2o.statemachine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.statemachine.config.MachineTransition;
import me.d2o.statemachine.config.StateMachineConfigurable;
import me.d2o.statemachine.exceptions.TransitionException;
import me.d2o.statemachine.repository.MachineRepository;


/**
 * Class: DeterministicTransistion
 *
 * @author bo.hanssen
 * @since Jan 21, 2017 11:12:33 PM
 *
 */
@Service
@Transactional
public class MachineCore {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StateMachineConfigurable config;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private LockingService lock;

	@Autowired
	private MachineRepository machineRepository;
	
	private void stepOneMachineTransition(TransitEvent transit, StateMachine machine) {
		MachineTransition mt = config.getTransition(transit.getEvent(), machine.getState());
		if (mt != null && !machine.getState().equals(mt.getCurrentState())) {
			logger.warn("Received invalid transition event [{}] current state is [{}]", mt, machine.getState());
		} else if (mt != null) {
			logger.info("Received event [{}]", mt);
			stepTwoPropagate(transit, machine, mt);
		}
	}

	private void stepTwoPropagate(TransitEvent transit, StateMachine machine, MachineTransition mt) {
		MachineEvent ge =  fsm.triggerMachineEvent(transit, mt.getEvent());
		stepThreeAdvanceState(machine, mt, ge);
	}

	private void stepThreeAdvanceState(StateMachine machine, MachineTransition mt, MachineEvent ge) {
		logger.info("Update state [{}] -> [{}]", machine.getState(), mt.getTargetState());
		machine.setState(mt.getTargetState());//
		stepFourPropagateTransistion(ge);
	}

	private void stepFourPropagateTransistion(MachineEvent event) {
		if (event != null && event.getPropagate() != null && !event.getPropagate().isEmpty()) {
			event.setEvent(event.getPropagate());
			event.setPropagate("");
			logger.info("Propagating transit [{}]", event);
			fsm.triggerAsynchronousTransition(event, event.getEvent());
		}
	}

	@EventListener
	private void execute(TransitEvent transit) {
		try {
			lock.aquire(transit);
			StateMachine machine = null;
			int timer = 0;
			while (machine == null){
				machine = machineRepository.findOne(transit.getMachineId());
				if (machine == null && timer >= 60000)
					throw new TransitionException("Could not transit machine because the StateMachine was not found in the db");
				if (machine == null)
					lock.sleep();
				timer += 5;
			}
			stepOneMachineTransition(transit,machine);
		} catch (Exception ex) {
			logger.error("Could not process transition {}", ex);
		}
	}

}
