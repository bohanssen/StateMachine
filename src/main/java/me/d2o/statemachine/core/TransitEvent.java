/**
 *
 */
package me.d2o.statemachine.core;

import me.d2o.statemachine.abstractevents.AbstractMachineEvent;

/**
 * Class: TransitEvent
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 3:55:04 PM
 *
 */
public class TransitEvent extends AbstractMachineEvent {

	private static final long serialVersionUID = -8167350404522760596L;

	protected TransitEvent(Object source) {
		super(source);
	}
}
