/**
 *
 */
package me.d2o.transfermodel.menu;

/**
 * Class: JoinRequest
 *
 * @author bo.hanssen
 * @since Jan 23, 2017 2:58:37 PM
 *
 */
public class JoinRequest {

	private long gameId;
	private int role;

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "JoinRequest [gameId=" + gameId + ", role=" + role + "]";
	}

}
