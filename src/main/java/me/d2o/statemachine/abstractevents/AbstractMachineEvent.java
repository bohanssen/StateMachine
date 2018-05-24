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

	private String enterState;
	private String exitState;
	private boolean terminated;
	
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

	public String getEnterState() {
		return enterState;
	}

	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}

	public String getExitState() {
		return exitState;
	}

	public void setExitState(String exitState) {
		this.exitState = exitState;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	@Override
	public String toString() {
		return "AbstractMachineEvent [machineId=" + machineId + ", event=" + event + ", propagate=" + propagate
				+ ", enterState=" + enterState + ", exitState=" + exitState + ", terminated=" + terminated + "]";
	}

//	@Override
//	public String toString() {
//		return this.getClass().getSimpleName()+" [machineId=" + machineId + ", event=" + event + ", propagate=" + propagate + ", body="
//				+ body + "]";
//	}

}
