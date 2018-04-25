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
 * The Class DivisionTransfer.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:38 PM
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "GAME_ID", "CONSUMER_ID", "SUPLIER_ID", "UNIT_ID" }) })
public class DivisionTransfer {

	@Id
	@GeneratedValue()
	@Column(name = "Id")
	private int id;
	private int quantity = 0;

	@ManyToOne
	private Territory suplier;

	@ManyToOne
	private Territory consumer;

	@ManyToOne
	private Game game;

	@ManyToOne
	private Unit unit;

	public void submit() {
		Division receiver;
		if (consumer.getDivisions().containsKey(unit.getKey())) {
			receiver = consumer.getDivisions().get(unit.getKey());
		} else {
			receiver = new Division();
			receiver.setUnit(unit);
		}
		receiver.setQuantity(receiver.getQuantity() + quantity);
		consumer.addDivision(receiver);
		quantity = 0;
	}

	public void rollback() {
		Division receiver = suplier.getDivisions().get(unit.getKey());
		receiver.setQuantity(receiver.getQuantity() + quantity);
		quantity = 0;
	}

	public void addOne() {
		quantity += 1;
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

	public Territory getSuplier() {
		return suplier;
	}

	public void setSuplier(Territory suplier) {
		this.suplier = suplier;
	}

	public Territory getConsumer() {
		return consumer;
	}

	public void setConsumer(Territory consumer) {
		this.consumer = consumer;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
