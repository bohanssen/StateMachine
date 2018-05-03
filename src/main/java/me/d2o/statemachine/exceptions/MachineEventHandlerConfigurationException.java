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

	public MachineEventHandlerConfigurationException(String string, StateMachineConfigurationException ex) {
		super(string, ex);
	}
}
