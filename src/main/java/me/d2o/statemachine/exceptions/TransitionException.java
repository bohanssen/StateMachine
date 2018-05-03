/**
 *
 */
package me.d2o.statemachine.exceptions;

/**
 * Class: TransitionException
 * 
 * @author bo.hanssen
 * @since Feb 3, 2017 10:15:59 AM
 *
 */
public class TransitionException extends RuntimeException {

	private static final long serialVersionUID = -3908306534684554741L;

	public TransitionException(String string) {
		super(string);
	}

}
