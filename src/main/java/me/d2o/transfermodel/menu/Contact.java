/**
 *
 */
package me.d2o.transfermodel.menu;

import me.d2o.persistence.model.general.UserEntity;

/**
 * Class: Contact
 *
 * @author bo.hanssen
 * @since Jan 19, 2017 1:16:38 PM
 *
 */
public class Contact {

	private String name;
	private long id;

	public Contact(UserEntity user) {
		setId(user.getId());
		setName(user.getNickname());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
