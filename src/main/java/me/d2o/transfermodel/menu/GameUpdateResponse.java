/**
 *
 */
package me.d2o.transfermodel.menu;

import java.util.List;

/**
 * Class: GameUpdateResponse
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 10:59:09 AM
 *
 */
public class GameUpdateResponse {

	private List<GameSummary> mygames;
	private List<GameSummary> myinvites;
	private List<GameSummary> opengames;

	public List<GameSummary> getMygames() {
		return mygames;
	}

	public void setMygames(List<GameSummary> mygames) {
		this.mygames = mygames;
	}

	public List<GameSummary> getMyinvites() {
		return myinvites;
	}

	public void setMyinvites(List<GameSummary> myinvites) {
		this.myinvites = myinvites;
	}

	public List<GameSummary> getOpengames() {
		return opengames;
	}

	public void setOpengames(List<GameSummary> opengames) {
		this.opengames = opengames;
	}

}
