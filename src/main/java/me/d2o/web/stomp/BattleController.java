/**
 *
 */
package me.d2o.web.stomp;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.StateMachineService;
import me.d2o.transfermodel.gameplay.battle.BattleRequest;

/**
 * Class: BattleController
 *
 * @author bo.hanssen
 * @since Feb 2, 2017 4:49:15 PM
 *
 */
@Controller
@Transactional
public class BattleController {

	@Autowired
	private UserService userService;

	@Autowired
	private StateMachineService fsm;

	@MessageMapping("/battlerequest")
	public void battleRequest(Principal principal, BattleRequest request) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), request.getType(), request);
	}

	@MessageMapping("/stopbattlemode")
	public void stopBattleMode(Principal principal, long gameId) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.STOP_BATTLE_MODE);
	}
}
