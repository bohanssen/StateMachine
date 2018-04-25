/**
 *
 */
package me.d2o.eventhandlers.setup.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.service.gameplay.GameService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: InitializeTurnHandler
 *
 * @author bo.hanssen
 * @since Jan 29, 2017 11:30:19 PM
 *
 */
@Service
@Transactional
public class InitializeTurnHandler extends GameEventHandler {

	@Autowired
	private GameService gameService;

	@Override
	public void handleEvent(GameEvent event) {
		Game game = gameService.getGame(event);
		Map<Integer, Long> turn = new HashMap<>();
		List<Participant> players = new ArrayList<>(game.getParticipants().values());
		Collections.shuffle(players);
		int index = 0;
		for (Participant player : players) {
			turn.put(index, player.getUser().getId());
			index++;
		}
		game.setTurn(turn);
		event.setUserId(turn.get(0));
		event.setPropagate(Events.SETUP_READY);
		gameService.continueFlowAsTurnUser(event);
		logger.info("Initialized turn [{}]", turn);
	}

	@Override
	public String eventType() {
		return Events.INITIALIZE_TURN;
	}

}
