/**
 *
 */
package me.d2o.service.notification.pojo;

/**
 * Class: NotificationRequest
 *
 * @author bo.hanssen
 * @since Feb 6, 2017 10:12:23 AM
 *
 */
public class NotificationRequest {

	private String userName;
	private String message;
	private String url;
	private int count;
	private boolean active;

	/**
	 * @param userName
	 * @param message
	 * @param url
	 */
	public NotificationRequest(String userName, String message, String url) {
		super();
		this.userName = userName;
		this.message = message;
		this.url = url;
		this.count = 0;
		this.active = false;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void addCount() {
		this.count += 1;
	}

	@Override
	public String toString() {
		return "NotificationRequest [userName=" + userName + ", message=" + message + ", url=" + url + ", count="
				+ count + ", active=" + active + "]";
	}

}
