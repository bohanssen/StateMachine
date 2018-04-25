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
import me.d2o.transfermodel.gameplay.battle.BattleTransferRequest;
import me.d2o.transfermodel.gameplay.mobilisation.MobilisationRequest;

/**
 * Class: MobilisationController
 *
 * @author bo.hanssen
 * @since Feb 4, 2017 11:23:00 PM
 *
 */
@Controller
@Transactional
public class MobilisationController {

	@Autowired
	private UserService userService;

	@Autowired
	private StateMachineService fsm;

	@MessageMapping("/mobilisationrequest")
	public void mobilisationRequest(Principal principal, MobilisationRequest request) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.MOBILISATION_REQUEST, request);
	}

	@MessageMapping("/mobilisationreset")
	public void reset(Principal principal, long gameId) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.MOBILISATION_RESET);
	}

	@MessageMapping("/mobilisationsubmit")
	public void submit(Principal principal, long gameId) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.MOBILISATION_SUBMT);
	}
	
	@MessageMapping("/battlemobilisationsubmit")
	public void submitBattleTransfer(Principal principal,BattleTransferRequest request){
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.BATTLE_TRANSFER_REQUEST,request);
	}
}
