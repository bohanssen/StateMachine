/**
 *
 */
package me.d2o.transfermodel.gameplay;

/**
 * Class: UnassignedUnits
 *
 * @author bo.hanssen
 * @since Jan 27, 2017 2:06:25 PM
 *
 */
public class DivisionClientModel {

	private String key;
	private String name;
	private int quantity;
	private int available;
	private int resting;
	private int attack;
	private int defence;

	/**
	 * @param key
	 * @param name
	 * @param quantity
	 */
	public DivisionClientModel(String key, String name, int quantity, int available, int resting, int attack,
			int defence) {
		super();
		this.key = key;
		this.name = name;
		this.quantity = quantity;
		this.available = available;
		this.resting = resting;
		this.attack = attack;
		this.defence = defence;
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

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getResting() {
		return resting;
	}

	public void setResting(int resting) {
		this.resting = resting;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	@Override
	public String toString() {
		return "UnassignedUnitsClientModel [key=" + key + ", name=" + name + ", quantity=" + quantity + "]";
	}

}
