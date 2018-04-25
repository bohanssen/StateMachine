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
public abstract class AbstractMachineEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1518456235456803455L;
	private String machineId;
	private String event;
	private String propagate;

	private Object body;

	public AbstractMachineEvent(Object source) {
		super(source);
	}

	public void copy(AbstractMachineEvent e) {
		this.machineId = e.getMachineId();
		this.event = e.getEvent();
		this.body = e.getBody();
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
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
		return "MachineEvent [machineId=" + machineId + ", event=" + event + ", propagate=" + propagate + ", body="
				+ body + "]";
	}

}
