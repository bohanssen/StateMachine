/**
 *
 */
package me.d2o.statemachine.exceptions;

/**
 * Class:  MachineEventHandlerConfigurationException
 * 
 * @author bo.hanssen
 * @since Feb 3, 2017 10:15:59 AM
 *
 */
public class MachineEventHandlerConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -3908306534684554741L;

	/**
	 *
	 */
	public MachineEventHandlerConfigurationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MachineEventHandlerConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MachineEventHandlerConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MachineEventHandlerConfigurationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MachineEventHandlerConfigurationException(Throwable cause) {
		super(cause);
	}

}
