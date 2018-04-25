/**
 *
 */
package me.d2o.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class: GameEventHandler
 *
 * @author bo.hanssen
 * @since Jan 23, 2017 1:29:44 PM
 *
 */
@Component
@Transactional
public abstract class GameEventHandler {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public abstract void handleEvent(GameEvent event);

	public abstract String eventType();

	@EventListener
	private void listner(GameEvent event) {
		if (event.getEvent().equals(eventType())) {
			handleEvent(event);
		}
	}
}
