/**
 *
 */
package me.d2o.utils.ai;

import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Unit;

/**
 * Class: BattleProposalAI
 *
 * @author bo.hanssen
 * @since Feb 21, 2017 1:41:42 PM
 *
 */
public class BattleProposalAI {

	private Territory agressor;
	private Territory defendor;
	private Unit unit;

	public Territory getAgressor() {
		return agressor;
	}

	public void setAgressor(Territory agressor) {
		this.agressor = agressor;
	}

	public Territory getDefendor() {
		return defendor;
	}

	public void setDefendor(Territory defendor) {
		this.defendor = defendor;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "BattleProposalAI [agressor=" + agressor + ", defendor=" + defendor + ", unit=" + unit + "]";
	}

}
