/**
 *
 */
package me.d2o.transfermodel.chat;

/**
 * Class: IncommingMessage
 * 
 * @author bo.hanssen
 * @since Jan 16, 2017 5:16:40 PM
 *
 */
public class IncommingMessage {

	private long target;
	private String message;

	public long getTarget() {
		return target;
	}

	public void setTarget(long target) {
		this.target = target;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
