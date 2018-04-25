
package me.d2o.transfermodel.misc;

import me.d2o.utils.notification.Level;

/**
 * Class: Greeting
 *
 * @author bo.hanssen
 * @since Jan 4, 2017 11:54:45 AM
 *
 */
public class Notification {

	private String msg;
	private String level;

	public Notification(String message, String level) {
		this.msg = message;
		this.level = level;
	}

	public Notification(String message) {
		this(message, "success");
	}

	public String getMsg() {
		return msg;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level.toString();
	}

}
