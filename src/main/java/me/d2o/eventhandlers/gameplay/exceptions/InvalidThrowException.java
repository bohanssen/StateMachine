/**
 *
 */
package me.d2o.eventhandlers.gameplay.exceptions;

import me.d2o.statemachine.TransitionException;

/**
 * Class: InvalidThrowException
 * 
 * @author bo.hanssen
 * @since Feb 3, 2017 11:27:42 AM
 *
 */
public class InvalidThrowException extends TransitionException {

	private static final long serialVersionUID = -8000124354730332585L;

	public InvalidThrowException() {
		super("invalid-throw");
	}

}
