
package me.d2o.transfermodel.admin;

import java.io.Serializable;

/**
 * Class: UpdateResponse
 *
 * @author bo.hanssen
 * @since Jan 5, 2017 1:44:16 PM
 *
 */
public class UpdateResponse extends UpdateRequest implements Serializable {

	private static final long serialVersionUID = -1372985253209314757L;
	private String message;
	private String level;
	private Boolean status;
	private String alert;

	public UpdateResponse(UpdateRequest request) {
		super(request);
	}

	public UpdateResponse() {
		// Default constructor is needed for serialization
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public Boolean getStatus() {
		return status;
	}

}
