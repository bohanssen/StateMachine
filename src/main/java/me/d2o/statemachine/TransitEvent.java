/**
 *
 */
package me.d2o.statemachine;

/**
 * Class: TransitEvent
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 3:55:04 PM
 *
 */
public class TransitEvent extends MachineEvent {

	private static final long serialVersionUID = -8167350404522760596L;

	public TransitEvent(Object source) {
		super(source);
	}
}
