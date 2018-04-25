/**
 *
 */
package me.d2o.transfermodel.gameplay.mobilisation;

/**
 * Class: MobilisationRequest
 * 
 * @author bo.hanssen
 * @since Feb 4, 2017 11:20:53 PM
 *
 */
public class MobilisationRequest {

	private long gameId;
	private String unitKey;
	private int source;
	private int target;

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(String unitKey) {
		this.unitKey = unitKey;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

}
