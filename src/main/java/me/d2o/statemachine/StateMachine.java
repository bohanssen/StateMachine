/**
 *
 */
package me.d2o.statemachine;

import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Id;


/**
 * Class: MachineState
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 4:45:28 PM
 *
 */
@Embeddable
public class StateMachine {

	@Id
	private String machineID;
	private String state;
	
	public StateMachine(String initialState) {
		machineID = UUID.randomUUID().toString();
		setState(initialState);
	}

	public String getState() {
		return state;
	}

	public String getMachineId() {
		return machineID;
	}
	
	protected void setState(String state) {
		this.state = state;
	}

}
