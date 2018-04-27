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
public class StateMachineConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -3908306534684554741L;

	/**
	 *
	 */
	public StateMachineConfigurationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public StateMachineConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StateMachineConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public StateMachineConfigurationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StateMachineConfigurationException(Throwable cause) {
		super(cause);
	}

}
