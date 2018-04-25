/**
 *
 */
package me.d2o.transfermodel.gameplay.store;

import me.d2o.persistence.model.scenario.Unit;

/**
 * Class: UnitClientModel
 *
 * @author bo.hanssen
 * @since Jan 30, 2017 10:44:19 PM
 *
 */
public class UnitClientModel {

	private String key;
	private String name;
	private int cost;
	private int defence;
	private int offence;

	public UnitClientModel() {
		super();
	}

	public UnitClientModel(Unit unit) {
		super();
		this.key = unit.getKey();
		this.cost = unit.getCost();
		this.defence = unit.getDefence();
		this.offence = unit.getOffence();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public UnitClientModel setName(String name) {
		this.name = name;
		return this;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getOffence() {
		return offence;
	}

	public void setOffence(int offence) {
		this.offence = offence;
	}

}
