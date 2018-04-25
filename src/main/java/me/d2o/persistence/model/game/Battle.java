package me.d2o.persistence.model.game;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import me.d2o.persistence.model.scenario.Unit;

/**
 * The Class Battle.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:41 PM
 */
@Entity
public class Battle {

	@Id
	private long id;

	@ElementCollection
	private List<Integer> offence;

	@ElementCollection
	private List<Integer> defence;

	@OneToOne
	private Unit defenceUnit;

	@OneToOne
	private Unit offenceUnit;

	@OneToOne
	private Territory aggressor;

	@OneToOne
	private Territory defendor;

	private int outcome;

	public void reset() {
		this.offence = null;
		this.defence = null;
		this.defenceUnit = null;
		this.offenceUnit = null;
		this.aggressor = null;
		this.defendor = null;
		this.outcome = 0;
	}

	public boolean checkBorder() {
		return this.aggressor.getRegion().getBorders().contains(this.defendor.getRegion());
	}

	public List<Integer> getOffence() {
		return offence;
	}

	public void setOffence(List<Integer> offence) {
		this.offence = offence;
	}

	public List<Integer> getDefence() {
		return defence;
	}

	public void setDefence(List<Integer> defence) {
		this.defence = defence;
	}

	public Territory getAggressor() {
		return aggressor;
	}

	public void setAggressor(Territory aggressor) {
		this.aggressor = aggressor;
	}

	public Territory getDefendor() {
		return defendor;
	}

	public void setDefendor(Territory defendor) {
		this.defendor = defendor;
	}

	public Unit getDefenceUnit() {
		return defenceUnit;
	}

	public void setDefenceUnit(Unit defenceUnit) {
		this.defenceUnit = defenceUnit;
	}

	public Unit getOffenceUnit() {
		return offenceUnit;
	}

	public void setOffenceUnit(Unit offenceUnit) {
		this.offenceUnit = offenceUnit;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOutcome() {
		return outcome;
	}

	public void setOutcome(int outcome) {
		this.outcome = outcome;
	}

	@Override
	public String toString() {
		return "Battle [id=" + id + ", offence=" + offence + ", defence=" + defence + ", defenceUnit=" + defenceUnit
				+ ", offenceUnit=" + offenceUnit + ", aggressor=" + aggressor + ", defendor=" + defendor + ", outcome="
				+ outcome + "]";
	}

}
