/**
 *
 */
package me.d2o.statemachine;

import javax.persistence.Embeddable;

import me.d2o.config.statemachine.States;

/**
 * Class: MachineState
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 4:45:28 PM
 *
 */
@Embeddable
public class MachineStateEmbeddableEntity {

	private String state;

	public MachineStateEmbeddableEntity() {
		setState(States.INITIAL);
	}

	public String getState() {
		return state;
	}

	protected void setState(String state) {
		this.state = state;
	}

}
