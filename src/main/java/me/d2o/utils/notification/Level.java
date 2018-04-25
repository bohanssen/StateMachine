package me.d2o.utils.notification;

/**
 * The Enum Level.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:43 PM
 */
public enum Level {

	SUCCESS("success"), INFO("info"), WARNING("warn"), ERROR("error");

	private String value;

	private Level(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	/*
	 *
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return value;
	}
}
