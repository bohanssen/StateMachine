/**
 *
 */
package me.d2o.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.service.general.UserService;

/**
 * Class: StateMachineService
 *
 * @author bo.hanssen
 * @since Jan 22, 2017 11:28:54 PM
 *
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StateMachineService {

	@Autowired
	private ApplicationEventPublisher publischer;

	@Autowired
	private UserService userService;

	@Async
	public void triggerTransition(long userId, long gameId, String event, Object body) {
		userService.authenticate(userService.getUser(userId));
		TransitEvent e = new TransitEvent("");
		e.setEvent(event);
		e.setGameId(gameId);
		e.setUserId(userId);
		e.setBody(body);
		publischer.publishEvent(e);
	}

	@Async
	public void triggerTransition(long userId, long gameId, String event) {
		triggerTransition(userId, gameId, event, null);
	}

	@Async
	public void triggerTransition(MachineEvent event, String transitionEvent) {
		userService.authenticate(userService.getUser(event.getUserId()));
		TransitEvent transit = new TransitEvent("");
		transit.copy(event);
		transit.setEvent(transitionEvent);
		publischer.publishEvent(transit);
	}

	protected GameEvent triggerGameEvent(MachineEvent event, String transitionEvent) {
		userService.authenticate(userService.getUser(event.getUserId()));
		GameEvent transit = new GameEvent("");
		transit.copy(event);
		transit.setEvent(transitionEvent);
		publischer.publishEvent(transit);
		return transit;
	}

}
