/**
 *
 */
package me.d2o.eventhandlers.gameplay.exceptions;

import me.d2o.statemachine.TransitionException;

/**
 * Class: NoGoldException
 * 
 * @author bo.hanssen
 * @since Mar 1, 2017 2:41:15 PM
 *
 */
public class NoGoldException extends TransitionException {

	private static final long serialVersionUID = -8156926849481454989L;

	public NoGoldException() {
		super("nogold");
	}
}
