/**
 *
 */
package me.d2o.eventhandlers.gameplay.exceptions;

import me.d2o.statemachine.TransitionException;

/**
 * Class: RegionNotYoursException
 *
 * @author bo.hanssen
 * @since Feb 4, 2017 11:46:52 PM
 *
 */
public class NoUnitsAvailableException extends TransitionException {

	private static final long serialVersionUID = 2727667466830311059L;

	public NoUnitsAvailableException() {
		super("no-units-available");
	}
}
