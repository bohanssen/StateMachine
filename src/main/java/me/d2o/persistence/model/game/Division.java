package me.d2o.persistence.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import me.d2o.persistence.model.scenario.Unit;

/**
 * The Class Division.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:34 PM
 */
@Entity
public class Division {

	@Id
	@GeneratedValue()
	@Column(name = "Id")
	private int id;
	private int active = 0;
	private int resting = 0;
	private String key;

	@ManyToOne
	private Unit unit;

	@ManyToOne
	private Territory territory;

	public void incrementQuantity(int x) {
		this.active += x;
	}

	public void killOne() {
		if (resting > 0) {
			resting += -1;
		} else {
			active += -1;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.active + this.resting;
	}

	public void setQuantity(int quantity) {
		this.active = quantity;
		this.resting = 0;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.key = unit.getKey();
		this.unit = unit;
	}

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getResting() {
		return resting;
	}

	public void setResting(int resting) {
		this.resting = resting;
	}

	public void restOne() {
		if (active > 0) {
			this.resting += 1;
			this.active += -1;
		}
	}

	public void markForDeletion() {
		this.territory.removeDivision(this.key);
		this.territory = null;
	}

	public int getActive() {
		return active;
	}

	public void reset() {
		this.active += this.resting;
		this.resting = 0;
	}

	@Override
	public String toString() {
		return "Division [id=" + id + ", active=" + active + ", resting=" + resting + ", key=" + key + ", unit=" + unit
				+ ", territory=" + territory + "]";
	}

}
