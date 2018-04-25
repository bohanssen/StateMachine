package me.d2o.persistence.model.scenario;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

/**
 * The Class Region.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:36 PM
 */
@Entity
@XmlRootElement
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "SCENARIO_TITLEKEY", "KEY" }) })
public class Region {

	@Id
	@GeneratedValue()
	private int id;
	private String key;
	private int bonus;
	private int bonusUnit; // eg 4 units start at lowest and add one, add one
							// middle etc etc
	private int advantage;

	// @Basic(fetch=FetchType.EAGER)
	@Lob
	@Type(type = "text")
	private String vector;

	@ManyToOne
	private Scenario scenario;

	@JoinTable(name = "borders", joinColumns = {
			@JoinColumn(name = "region1", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "region2", referencedColumnName = "id", nullable = false) })
	@ManyToMany
	private Set<Region> borders = new HashSet<>();

	
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

	
	public Scenario getScenario() {
		return scenario;
	}

	
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	
	public Set<Region> getBorders() {
		return borders;
	}

	
	public void setBorders(Set<Region> borders) {
		this.borders = borders;
	}

	
	public void addBorder(Region region) {
		borders.add(region);
	}

	
	public String getVector() {
		return vector;
	}

	
	public void setVector(String vector) {
		this.vector = vector;
	}

	
	public int getBonus() {
		return bonus;
	}

	
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	
	public int getBonusUnit() {
		return bonusUnit;
	}

	
	public void setBonusUnit(int bonusUnit) {
		this.bonusUnit = bonusUnit;
	}

	
	public int getAdvantage() {
		return advantage;
	}

	
	public void setAdvantage(int advantage) {
		this.advantage = advantage;
	}

}
