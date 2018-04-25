/**
 *
 */
package me.d2o.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.statemachine.GameEvent;

/**
 * Class: StompService
 *
 * @author bo.hanssen
 * @since Jan 27, 2017 2:41:46 PM
 *
 */
@Service
public class StompService {

	@Autowired
	private SimpMessagingTemplate mq;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private GameService gameService;

	@Autowired
	private UserService userService;

	@Autowired
	private TerritoryService territoryService;

	public void push(String queue, Object payload, UserEntity user) {
		if (user != null) {
			mq.convertAndSendToUser(user.getUsername(), "/queue/" + queue, payload);
		}
	}

	public void push(String queue, Object payload) {
		this.push(queue, payload, userService.getUser());
	}

	public void pushTopic(long gameId, String queue, Object payload) {
		mq.convertAndSend("/topic/" + queue + "/" + gameId, payload);
	}

	public void pushTopic(GameEvent event, String queue, Object payload) {
		this.pushTopic(event.getGameId(), queue, payload);
	}

	public void refreshClient(long gameId) {
		mq.convertAndSend("/topic/refresh/" + gameId, true);
	}

	public void refreshClient(GameEvent event) {
		refreshClient(event.getGameId());
	}

	public void pushInfo(GameEvent event) {
		gameService.getGame(event).getParticipants().values()
				.forEach(p -> this.push("info", participantService.getInfo(event.getGameId(), p), p.getUser()));

	}

	public void pushTerritoryUpdate(Territory territory) {
		this.pushTopic(territory.getGame().getId(), "territory", territoryService.getClientModel(territory));
	}
}
