/**
 *
 */
package me.d2o.statemachine.core;

import me.d2o.statemachine.utils.AbstractMachineEvent;

/**
 * Class: TransitEvent
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:49:04 AM
 *
 */
public class MachineEvent extends AbstractMachineEvent {

	private static final long serialVersionUID = -8167350404522760596L;

	protected MachineEvent(Object source) {
		super(source);
	}
}
