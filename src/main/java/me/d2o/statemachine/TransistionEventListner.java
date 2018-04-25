/**
 *
 */
package me.d2o.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.persistence.model.game.Game;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.general.StompService;
import me.d2o.service.general.UserService;
import me.d2o.service.notification.NotificationFactory;

/**
 * Class: DeterministicTransistion
 *
 * @author bo.hanssen
 * @since Jan 21, 2017 11:12:33 PM
 *
 */
@Service
@Transactional
public class TransistionEventListner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected GameService gameService;

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationFactory notify;

	@Autowired
	private StateMachineConfigurable config;

	@Autowired
	private StompService stomp;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private LockingService lock;

	private void stepOneMachineTransition(TransitEvent transit, Game game) {
		while (game == null)
			game = gameService.getGame(transit.getGameId());
		MachineTransition mt = config.getTransition(transit.getEvent(), game.getState().getState());
		if (mt != null && !game.getState().getState().equals(mt.getCurrentState())) {
			logger.warn("Received invalid transition event [{}] current state is [{}]", mt, game.getState().getState());
		} else if (mt != null) {
			logger.info("Received event [{}]", mt);
			stepTwoCheckTurn(transit, game, mt);
		}
	}

	private void stepTwoCheckTurn(TransitEvent transit, Game game, MachineTransition mt) {
		if (mt.isCheckTurn() && !gameService.checkTurn(game, userService.getUser().getId())) {
			notify.sendNotify("notyourturn", userService.getUser(transit.getUserId()));
			logger.warn("Received [{}] event form user [{}] while it is not his turn.", transit.getEvent(),
					transit.getUserId());
		} else {
			stepThreePropagate(transit, game, mt);
		}
	}

	private void stepThreePropagate(TransitEvent transit, Game game, MachineTransition mt) {
		GameEvent ge = null;
		String propagate = "";
		if ("AI".equals(userService.getUser().getRole()) && !mt.getAiEvent().isEmpty()) {
			propagate = mt.getAiEvent();
			logger.info("Propagte alternative AI event [{}]", propagate);
		} else if (!mt.getPropagationEvent().isEmpty()) {
			propagate = mt.getPropagationEvent();
			logger.info("Propagte event [{}]", propagate);
		}
		if (!propagate.isEmpty()) {
			ge = fsm.triggerGameEvent(transit, propagate);
		}
		stepFourAdvanceState(game, mt, ge);
	}

	private void stepFourAdvanceState(Game game, MachineTransition mt, GameEvent ge) {
		if (mt != null && !mt.getTargetState().isEmpty()) {
			logger.info("Update state [{}] -> [{}]", game.getState().getState(), mt.getTargetState());
			game.getState().setState(mt.getTargetState());//
		}
		stepFivePropagateTransistion(ge);
	}

	private void stepFivePropagateTransistion(GameEvent event) {
		if (event != null && event.getPropagate() != null && !event.getPropagate().isEmpty()) {
			event.setEvent(event.getPropagate());
			event.setPropagate("");
			logger.info("Propagating transit [{}]", event);
			fsm.triggerTransition(event, event.getEvent());
		}
	}

	@Async
	@EventListener
	private void execute(TransitEvent transit) {
		try {
			lock.aquire(transit);
			MDC.put("logFileName", "games/game_" + transit.getGameId());
			Game game = gameService.getGame(transit);
			stepOneMachineTransition(transit, game);
		} catch (TransitionException ex) {
			logger.error("Could not process transition {}", ex);
			notify.sendNotify(ex.getMessage(), userService.getUser(transit.getUserId()));
			stomp.refreshClient(transit.getGameId());
		}
	}

}
