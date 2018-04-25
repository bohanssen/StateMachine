package me.d2o.transfermodel.gameplay;

/**
 * Class: DeployRequest
 *
 * @author bo.hanssen
 * @since Jan 27, 2017 3:58:43 PM
 *
 */
public class DeployRequest {

	private String unitKey;
	private long gameId;
	private int region;
	private int mode;

	public String getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(String unitKey) {
		this.unitKey = unitKey;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
