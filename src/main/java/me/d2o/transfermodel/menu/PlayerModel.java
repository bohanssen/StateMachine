/**
 *
 */
package me.d2o.transfermodel.menu;

/**
 * Class: PlayerModel
 *
 * @author bo.hanssen
 * @since Jan 19, 2017 10:56:01 AM
 *
 */
public class PlayerModel {

	private int id;
	private String title;
	private String summary;

	/**
	 * @param id
	 * @param title
	 * @param summary
	 */
	public PlayerModel(int id, String title, String summary) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
