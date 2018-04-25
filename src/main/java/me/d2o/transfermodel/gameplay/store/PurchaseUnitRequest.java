/**
 *
 */
package me.d2o.transfermodel.gameplay.store;

/**
 * Class: PurchaseUnitRequest
 *
 * @author bo.hanssen
 * @since Jan 31, 2017 9:29:13 AM
 *
 */
public class PurchaseUnitRequest {

	private long gameId;
	private String unitKey;
	private int region;

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

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "PurchaseUnitRequest [gameId=" + gameId + ", unitKey=" + unitKey + ", region=" + region + "]";
	}

}
