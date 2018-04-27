/**
 *
 */
package me.d2o.statemachine.core;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Class: MachineState
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 4:45:28 PM
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class StateMachine {

	@Id
	private String machineID;
	private String state;
	
	protected StateMachine(){
		machineID = UUID.randomUUID().toString();
		setState(getInitialState());
	}

	public String getInitialState() {
		return "InitialState";
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

	@Override
	public String toString() {
		return "StateMachine [machineID=" + machineID + ", state=" + state + "]";
	}

}
