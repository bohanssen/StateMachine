package me.d2o.persistence.model.game;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import me.d2o.config.statemachine.States;
import me.d2o.statemachine.StateMachine;

/**
 * The Class Game.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:31 PM
 */
@Entity
public class Game {

	@Id
	private String id;

	private StateMachine state;

	@PrePersist
	public void init() {
		this.state = new StateMachine(States.INITIAL);
		this.id = state.getMachineId();
	}
}
