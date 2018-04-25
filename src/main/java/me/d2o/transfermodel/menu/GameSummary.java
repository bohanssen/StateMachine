/**
 *
 */
package me.d2o.transfermodel.menu;

import java.util.List;

/**
 * Class: GameSummary
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 9:25:56 AM
 *
 */
public class GameSummary {

	private long id;
	private String title;
	private boolean turn;
	private List<String> participants;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "GameSummary [id=" + id + ", title=" + title + ", turn=" + turn + "]";
	}

}
