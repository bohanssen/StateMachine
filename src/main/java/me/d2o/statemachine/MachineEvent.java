/**
 *
 */
package me.d2o.statemachine;

import org.springframework.context.ApplicationEvent;

/**
 * Class: GameEvent
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 2:22:13 PM
 *
 */
public abstract class MachineEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1518456235456803455L;
	private long userId;
	private long gameId;
	private String event;
	private String propagate;

	private Object body;

	public MachineEvent(Object source) {
		super(source);
	}

	public void copy(MachineEvent e) {
		this.userId = e.getUserId();
		this.gameId = e.getGameId();
		this.event = e.getEvent();
		this.body = e.getBody();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getPropagate() {
		return propagate;
	}

	public void setPropagate(String propagate) {
		this.propagate = propagate;
	}

	@Override
	public String toString() {
		return "GameEvent [userId=" + userId + ", gameId=" + gameId + ", event=" + event + "]";
	}

}
