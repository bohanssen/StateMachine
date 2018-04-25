/**
 *
 */
package me.d2o.transfermodel.chat;

/**
 * Class: UserStatus
 *
 * @author bo.hanssen
 * @since Jan 16, 2017 1:29:54 PM
 *
 */
public class UserStatus {

	private boolean online;
	private String username;
	private long id;

	/**
	 * @param online
	 * @param username
	 * @param id
	 */
	public UserStatus(boolean online, String username, long id) {
		super();
		this.online = online;
		this.username = username;
		this.id = id;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
