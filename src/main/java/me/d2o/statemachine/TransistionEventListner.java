/**
 *
 */
package me.d2o.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Class: DeterministicTransistion
 *
 * @author bo.hanssen
 * @since Jan 21, 2017 11:12:33 PM
 *
 */
@Service
@Transactional
public class TransistionEventListner {

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
		MachineEvent ge = null;
		String propagate = "";
		if (!mt.getPropagationEvent().isEmpty()) {
			propagate = mt.getPropagationEvent();
			logger.info("Propagte event [{}]", propagate);
		}
		if (!propagate.isEmpty()) {
			ge = fsm.triggerMachineEvent(transit, propagate);
		}
		stepThreeAdvanceState(machine, mt, ge);
	}

	private void stepThreeAdvanceState(StateMachine machine, MachineTransition mt, MachineEvent ge) {
		if (mt != null && !mt.getTargetState().isEmpty()) {
			logger.info("Update state [{}] -> [{}]", machine.getState(), mt.getTargetState());
			machine.setState(mt.getTargetState());//
		}
		stepFourPropagateTransistion(ge);
	}

	private void stepFourPropagateTransistion(MachineEvent event) {
		if (event != null && event.getPropagate() != null && !event.getPropagate().isEmpty()) {
			event.setEvent(event.getPropagate());
			event.setPropagate("");
			logger.info("Propagating transit [{}]", event);
			fsm.triggerTransition(event, event.getEvent());
		}
	}

	@EventListener
	private void execute(TransitEvent transit) {
		try {
			lock.aquire(transit);
			StateMachine machine = machineRepository.findOne(transit.getMachineId());
			stepOneMachineTransition(transit,machine);
		} catch (TransitionException ex) {
			logger.error("Could not process transition {}", ex);
		}
	}

}
