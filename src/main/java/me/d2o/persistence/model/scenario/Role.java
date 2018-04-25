package me.d2o.persistence.model.scenario;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Role.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:37 PM
 */
@Entity
@XmlRootElement
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "SCENARIO_TITLEKEY", "KEY" }) })
public class Role {

	@Id
	@GeneratedValue()
	private int id;

	@ManyToOne
	private Scenario scenario;
	private String key;
	private String color;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	@MapKey(name = "key")
	private Map<String, Unit> units = new HashMap<>();

	@ElementCollection
	private Map<String, Integer> items;

	public Role() {
		items = new HashMap<>();
	}

	public void addUnit(Unit unit) {
		units.put(unit.getKey(), unit);
		unit.setRole(this);
	}

	public Unit findUnit(String key) {
		return units.get(key);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Map<String, Unit> getUnits() {
		return units;
	}

	public void setUnits(Map<String, Unit> units) {
		this.units = units;
	}

	public Map<String, Integer> getItems() {
		return items;
	}

	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}

}
