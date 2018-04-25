/**
 *
 */
package me.d2o.web.stomp;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.StateMachineService;
import me.d2o.transfermodel.graphics.PlayerInfoModel;
import me.d2o.transfermodel.graphics.TerritoryClientModel;

/**
 * Class: TerritoryController
 *
 * @author bo.hanssen
 * @since Jan 25, 2017 8:54:39 PM
 *
 */
@Controller
@Transactional
public class GameBoardInitializer {

	@Autowired
	private UserService userService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private GameService gameService;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private ParticipantService participantService;

	@MessageMapping("/getterritories")
	@SendToUser("/queue/territory")
	public List<TerritoryClientModel> initialize(Principal principal, long gameId) {
		userService.authenticate(principal);
		return territoryService.getTerritories(gameService.getGame(gameId)).stream()
				.map(territoryService::getClientModel).collect(Collectors.toList());
	}

	@MessageMapping("/boardConnect")
	@SendToUser("/queue/info")
	public PlayerInfoModel connect(Principal principal, long gameId) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.USER_CONNECTED, false);
		return participantService.getInfo(gameId);
	}
}
