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
public class RegionNotYoursException extends TransitionException {

	private static final long serialVersionUID = 2727667466830311059L;

	public RegionNotYoursException() {
		super("region-not-yours");
	}
}
