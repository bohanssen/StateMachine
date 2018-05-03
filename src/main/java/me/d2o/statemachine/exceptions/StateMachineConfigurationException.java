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

	private static final long serialVersionUID = 1973543642725051803L;

	public StateMachineConfigurationException(String string) {
		super(string);
	}

	public StateMachineConfigurationException(Exception e) {
		super(e);
	}
}
