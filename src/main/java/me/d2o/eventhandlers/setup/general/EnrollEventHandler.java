/**
 *
 */
package me.d2o.eventhandlers.setup.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Game;
import me.d2o.service.gameplay.GameService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.menu.JoinRequest;

/**
 * Class: EnrollEventHandler
 *
 * @author bo.hanssen
 * @since Jan 22, 2017 11:37:09 PM
 *
 */
@Service
@Transactional
public class EnrollEventHandler extends GameEventHandler {

	@Autowired
	private GameService gameService;

	@Override
	public void handleEvent(GameEvent event) {
		Game game = gameService.getGame(event);
		JoinRequest request = (JoinRequest) event.getBody();
		logger.info("Process joinrequest [{}]", request);
		gameService.registerPlayer(game, request.getRole());
		gameService.removeInvite(game);
		gameService.triggerUpdateMenu(event.getGameId());
		event.setPropagate(Events.CHECK_SLOTS_FILLED);
	}

	@Override
	public String eventType() {
		return Events.ENROLL;
	}

}
