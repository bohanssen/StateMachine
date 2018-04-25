/**
 *
 */
package me.d2o.web.stomp;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.StateMachineService;
import me.d2o.transfermodel.menu.GameCreateRequest;
import me.d2o.transfermodel.menu.GameUpdateRequest;
import me.d2o.transfermodel.menu.GameUpdateResponse;
import me.d2o.transfermodel.menu.JoinRequest;

/**
 * Class: GameCreateController
 *
 * @author bo.hanssen
 * @since Jan 19, 2017 4:33:52 PM
 *
 */
@Controller
@Transactional
public class GameMenuController {

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@Autowired
	private StateMachineService fsm;

	@MessageMapping("/creategame")
	public void createGame(GameCreateRequest request, Principal principal) {
		userService.authenticate(principal);
		gameService.createGame(request);
	}

	@MessageMapping("/joingame")
	public void joinGame(JoinRequest request, Principal principal) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.REGISTER, request);
	}

	@MessageMapping("/getmenucontent")
	@SendToUser("/queue/updatemenu")
	public GameUpdateResponse getUpdate(Principal principal, GameUpdateRequest request) {
		userService.authenticate(principal);
		GameUpdateResponse response = new GameUpdateResponse();
		response.setMygames(gameService.getMyGames(request.getMygames()));
		response.setMyinvites(gameService.getMyInvites(request.getMyinvites()));
		response.setOpengames(gameService.getOpenGames(request.getOpengames()));
		return response;
	}

}
