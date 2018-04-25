/**
 *
 */
package me.d2o.transfermodel.menu;

/**
 * Class: GameCreateRequest
 * 
 * @author bo.hanssen
 * @since Jan 19, 2017 4:31:14 PM
 *
 */
public class GameCreateRequest {

	private String key;
	private int role;
	private long[] invitees;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public long[] getInvitees() {
		return invitees;
	}

	public void setInvitees(long[] invitees) {
		this.invitees = invitees;
	}

}
