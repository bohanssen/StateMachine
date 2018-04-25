package me.d2o.persistence.model.game;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.model.scenario.Role;

/**
 * The Class Participant.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:34 PM
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "GAME_ID", "ROLE_ID" }) })
public class Participant {

	@Id
	@GeneratedValue()
	@Column(name = "Id")
	private int id;
	private int cash = 0;
	private String level;
	private int undeployedtown = 1;

	@ManyToOne
	private UserEntity user;

	@ManyToOne
	private Game game;

	@ManyToOne
	private Role role;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "participant", cascade = CascadeType.ALL)
	@MapKeyColumn(name = "key")
	private Map<String, UnassignedUnits> unassignedUnits = new HashMap<>();

	public void editCash(int cash) {
		this.cash += cash;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Map<String, UnassignedUnits> getUnassignedUnits() {
		return unassignedUnits;
	}

	public void setUnassignedUnits(Map<String, UnassignedUnits> unassignedUnits) {
		this.unassignedUnits = unassignedUnits;
	}

	public void addUnits(UnassignedUnits uu) {
		this.unassignedUnits.put(uu.getKey(), uu);
		uu.setParticipant(this);
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getUndeployedtown() {
		return undeployedtown;
	}

	public void setUndeployedtown(int undeployedtown) {
		this.undeployedtown = undeployedtown;
	}

}
