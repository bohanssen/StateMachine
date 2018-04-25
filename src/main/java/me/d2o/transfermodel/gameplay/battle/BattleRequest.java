/**
 *
 */
package me.d2o.transfermodel.gameplay.battle;

import java.util.List;

import me.d2o.statemachine.GameEvent;
import me.d2o.utils.ai.BattleProposalAI;

/**
 * Class: BattleRequest
 *
 * @author bo.hanssen
 * @since Feb 2, 2017 4:57:00 PM
 *
 */
public class BattleRequest {

	private String type;
	private long gameId;
	private int agressorId;
	private int defendorId;
	private int dice;
	private String unitKey;
	private List<Integer> result;

	public BattleRequest() {
		super();
	}

	public BattleRequest(BattleProposalAI proposal, GameEvent event, List<Integer> result) {
		this.gameId = event.getGameId();
		this.agressorId = proposal.getAgressor().getRegion().getId();
		this.defendorId = proposal.getDefendor().getRegion().getId();
		this.dice = proposal.getUnit().getOffence();
		this.unitKey = proposal.getUnit().getKey();
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public int getAgressorId() {
		return agressorId;
	}

	public void setAgressorId(int agressorId) {
		this.agressorId = agressorId;
	}

	public int getDefendorId() {
		return defendorId;
	}

	public void setDefendorId(int defendorId) {
		this.defendorId = defendorId;
	}

	public int getDice() {
		return dice;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}

	public String getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(String unitKey) {
		this.unitKey = unitKey;
	}

	public List<Integer> getResult() {
		return result;
	}

	public void setResult(List<Integer> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "BattleRequest [type=" + type + ", gameId=" + gameId + ", agressorId=" + agressorId + ", defendorId="
				+ defendorId + ", dice=" + dice + ", unitKey=" + unitKey + ", result=" + result + "]";
	}

}
