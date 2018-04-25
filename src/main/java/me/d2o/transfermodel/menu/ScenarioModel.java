/**
 *
 */
package me.d2o.transfermodel.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: Scenario
 *
 * @author bo.hanssen
 * @since Jan 16, 2017 10:02:43 AM
 *
 */
public class ScenarioModel {

	private String title;
	private String summary;
	private String key;
	private List<PlayerModel> roles;

	/**
	 * @param title
	 * @param summary
	 * @param key
	 */
	public ScenarioModel(String title, String summary, String key) {
		super();
		this.title = title;
		this.summary = summary;
		this.key = key;
		roles = new ArrayList<>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<PlayerModel> getRoles() {
		return roles;
	}

	public void setRoles(List<PlayerModel> roles) {
		this.roles = roles;
	}

}
