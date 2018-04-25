/**
 *
 */
package me.d2o.statemachine;

/**
 * Class: TransitEvent
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:49:04 AM
 *
 */
public class GameEvent extends MachineEvent {

	private static final long serialVersionUID = -8167350404522760596L;

	protected GameEvent(Object source) {
		super(source);
	}
}
