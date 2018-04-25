package me.d2o.persistence.model.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import me.d2o.persistence.model.scenario.Region;
import me.d2o.persistence.model.scenario.Role;

/**
 * The Class Territory.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:31 PM
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "GAME_ID", "REGION_ID" }) })
public class Territory {

	@Id
	@GeneratedValue()
	@Column(name = "Id")
	private int id;
	private String key;
	private boolean town;

	@ManyToOne
	private UserEntity user;

	@ManyToOne
	private Game game;

	@ManyToOne
	private Role role;

	@ManyToOne
	private Region region;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "territory", cascade = CascadeType.ALL)
	@MapKeyColumn(name = "key")
	private Map<String, Division> divisions = new HashMap<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consumer", cascade = CascadeType.ALL)
	private Set<DivisionTransfer> transfer = new HashSet<>();

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.key = region.getKey();
		this.region = region;
	}

	public Map<String, Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(Map<String, Division> divisions) {
		this.divisions = divisions;
	}

	public void addDivision(Division division) {
		this.divisions.put(division.getKey(), division);
		division.setTerritory(this);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void removeDivision(String key) {
		this.divisions.remove(key);
	}

	public Set<DivisionTransfer> getTransfer() {
		return transfer;
	}

	public void setTransfer(Set<DivisionTransfer> transfer) {
		this.transfer = transfer;
	}

	public void addTransfer(DivisionTransfer transfer) {
		this.transfer.add(transfer);
		transfer.setConsumer(this);
	}

	/**
	 * Checks if is town.
	 *
	 * @return true, if is town
	 */
	public boolean isTown() {
		return town;
	}

	public void setTown(boolean town) {
		this.town = town;
	}

	@Override
	public String toString() {
		return key;
	}

}
