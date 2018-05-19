/**
 *
 */
package me.d2o.statemachine.abstractevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

/**
 * Class: AbstractMachineEvent
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 2:22:13 PM
 *
 */
public abstract class AbstractMachineEvent extends ApplicationEvent {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMachineEvent.class);
	
	private static final long serialVersionUID = 1518456235456803455L;
	private String machineId;
	private String event;
	private String propagate;

	private Object body;

	public AbstractMachineEvent(Object source) {
		super(source);
	}

	public void copy(AbstractMachineEvent e) {
		logger.debug("Copy event [{}]", e);
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
		return this.getClass().getSimpleName()+" [machineId=" + machineId + ", event=" + event + ", propagate=" + propagate + ", body="
				+ body + "]";
	}

}
