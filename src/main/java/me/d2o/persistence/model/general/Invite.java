/**
 *
 */
package me.d2o.persistence.model.general;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.d2o.persistence.model.game.Game;

@Entity
public class Invite {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne
	@JsonIgnore
	private Game game;

	@OneToOne
	@JsonIgnore
	private UserEntity user;

	/**
	 * @param target
	 * @param user
	 */
	public Invite(Game target, UserEntity user) {
		super();
		this.game = target;
		this.user = user;
	}

	public Invite() {
		super();
	}

	public Game getTarget() {
		return game;
	}

	public void setTarget(Game target) {
		this.game = target;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
