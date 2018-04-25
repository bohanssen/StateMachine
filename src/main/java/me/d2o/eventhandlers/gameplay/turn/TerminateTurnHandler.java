/**
 *
 */
package me.d2o.eventhandlers.gameplay.turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.gameplay.BattleService;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;

/**
 * Class: TerminateTurnHandler
 *
 * @author bo.hanssen
 * @since Feb 5, 2017 1:39:37 AM
 *
 */
@Service
@Transactional
public class TerminateTurnHandler extends GameEventHandler {

	@Autowired
	private GameService gameService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private NotificationFactory notify;

	@Autowired
	private BattleService battleService;

	@Autowired
	private Environment environment;
	
	@Override
	public void handleEvent(GameEvent event) {
		checkDeathPlayers(event);
		Map<Integer, Long> turns = revolveTurn(event);
		UserEntity user = gameService.continueFlowAsTurnUser(event);
		
		boolean humansAlive= false; 
		if (!Arrays.asList(environment.getActiveProfiles()).contains("development")){
			for (Participant p : gameService.getGame(event).getParticipants().values()){
				humansAlive = !"AI".equals(p.getUser().getRole());
				if (humansAlive){
					break;
				}
			}
		} else {
			humansAlive= true; 
		}

		
		if (humansAlive && turns.size() != 1) {
			event.setPropagate(Events.USER_CONNECTED);
			notify.userNotification(user, "yourturn", event.getGameId());
		} else {
			event.setPropagate(Events.FINISHED_GAME);
			notify.broadcastNotify(user.getNickname() + " has won the game!", user);
			cleanup(event);
		}
	}

	public void checkDeathPlayers(GameEvent event) {
		Game game = gameService.getGame(event);
		List<Integer> indexes = new ArrayList<>();
		List<Integer> keys = new ArrayList<>();
		for (Participant player : game.getParticipants().values()) {
			if (territoryService.getTerritories(player).size() == 0) {
				for (int key : game.getTurn().keySet()) {
					if (game.getTurn().get(key).equals(player.getUser().getId())) {
						indexes.add(key);
						keys.add(player.getRole().getId());
					}
				}
				notify.broadcastNotify(player.getUser().getNickname() + " is whiped from the board!", player.getUser());
			}
		}
		for (int index : indexes) {
			game.getTurn().remove(index);
		}
		for (int key : keys) {
			game.getParticipants().remove(key);
		}
	}

	public void cleanup(GameEvent event) {
		battleService.remove(event);
	}

	public Map<Integer, Long> revolveTurn(GameEvent event) {
		Map<Integer, Long> turns = gameService.getGame(event).getTurn();
		Map<Integer, Long> tempTurns = new HashMap<>();
		turns.keySet().forEach(k -> {
			int temp = k - 1 < 0 ? turns.size() - 1 : k - 1;
			long user = turns.get(k);
			while (tempTurns.containsKey(temp)) {
				temp = temp - 1 < 0 ? turns.size() - 1 : temp - 1;
			}
			tempTurns.put(temp, user);
		});
		gameService.getGame(event).setTurn(tempTurns);
		event.setUserId(gameService.getGame(event).getTurn().get(0));
		return turns;
	}

	@Override
	public String eventType() {
		return Events.EXECUTE_TERMINATE_TURN;
	}

}
