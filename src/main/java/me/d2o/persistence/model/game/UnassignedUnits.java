package me.d2o.persistence.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import me.d2o.persistence.model.scenario.Unit;

/**
 * The Class UnassignedUnits.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:36 PM
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "PARTICIPANT_ID", "UNIT_ID" }) })
public class UnassignedUnits {

	@Id
	@GeneratedValue()
	@Column(name = "Id")
	private int id;
	private int quantity;
	private String key;

	@ManyToOne
	private Unit unit;

	@ManyToOne
	private Participant participant;

	public void incrementQuantity(int x) {
		this.quantity += x;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.key = unit.getKey();
		this.unit = unit;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
