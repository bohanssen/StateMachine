
package me.d2o.transfermodel.admin;

import java.io.Serializable;

/**
 * Class: UpdateRequest
 *
 * @author bo.hanssen
 * @since Jan 5, 2017 1:44:57 PM
 *
 */
public class UpdateRequest implements Serializable {

	
	private static final long serialVersionUID = 9036687199436983891L;
	private String property;
	private String key;
	private long id;

	
	public UpdateRequest() {
		// Not implemented
	}

	
	public UpdateRequest(UpdateRequest template) {
		property = template.property;
		key = template.key;
		id = template.id;
	}

	
	public String getProperty() {
		return property;
	}

	
	public void setProperty(String property) {
		this.property = property;
	}

	
	public long getId() {
		return id;
	}

	
	public void setId(long id) {
		this.id = id;
	}

	
	public String getKey() {
		return key;
	}

	
	public void setKey(String key) {
		this.key = key;
	}

}
