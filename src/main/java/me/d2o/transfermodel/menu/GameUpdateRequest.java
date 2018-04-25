/**
 *
 */
package me.d2o.transfermodel.menu;

import java.util.List;

/**
 * Class: GameUpdateRequest
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 10:53:03 AM
 *
 */
public class GameUpdateRequest {

	private List<Long> mygames;
	private List<Long> myinvites;
	private List<Long> opengames;

	public List<Long> getMygames() {
		return mygames;
	}

	public void setMygames(List<Long> mygames) {
		this.mygames = mygames;
	}

	public List<Long> getMyinvites() {
		return myinvites;
	}

	public void setMyinvites(List<Long> myinvites) {
		this.myinvites = myinvites;
	}

	public List<Long> getOpengames() {
		return opengames;
	}

	public void setOpengames(List<Long> opengames) {
		this.opengames = opengames;
	}

}
