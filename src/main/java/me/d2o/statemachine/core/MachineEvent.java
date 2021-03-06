/**
 *
 */
package me.d2o.statemachine.core;

import me.d2o.statemachine.abstractevents.AbstractMachineEvent;

/**
 * Class: TransitEvent
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:49:04 AM
 *
 */
public class MachineEvent extends AbstractMachineEvent {

	public MachineEvent(Object source) {
		super(source);
	}

	private static final long serialVersionUID = -8167350404522760596L;

	@Override
	public void copy(AbstractMachineEvent e) {
		super.copy(e);
		this.setEnterState(e.getEnterState());
		this.setExitState(e.getExitState());
	}
	
}
