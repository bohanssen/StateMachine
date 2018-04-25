package me.d2o.persistence.model.scenario;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Unit.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:36 PM
 */
@Entity
@XmlRootElement
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "ROLE_ID", "KEY" }) })
public class Unit {

	@Id
	@GeneratedValue()
	private int id;

	private String key;
	private int offence;
	private int defence;
	private int cost;
	private boolean veteran;
	private int startQuantity;

	@ManyToOne
	private Role role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getOffence() {
		return offence;
	}

	public void setOffence(int offence) {
		this.offence = offence;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * Checks if is veteran.
	 *
	 * @return true, if is veteran
	 */
	public boolean isVeteran() {
		return veteran;
	}

	public void setVeteran(boolean veteran) {
		this.veteran = veteran;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getStartQuantity() {
		return startQuantity;
	}

	public void setStartQuantity(int startQuantity) {
		this.startQuantity = startQuantity;
	}

	@Override
	public String toString() {
		return key;
	}

}
