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
import me.d2o.transfermodel.gameplay.DeployRequest;

/**
 * Class: UnitDeploymentController
 *
 * @author bo.hanssen
 * @since Jan 27, 2017 3:56:33 PM
 *
 */
@Controller
@Transactional
public class UnitDeploymentController {

	@Autowired
	private UserService userService;

	@Autowired
	private StateMachineService fsm;

	@MessageMapping("/deployunit")
	public void deploy(DeployRequest request, Principal principal) {
		request.setMode(1);
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.DEPLOY_UNIT_REQUEST, request);
	}

	@MessageMapping("/undeployunit")
	public void undeploy(DeployRequest request, Principal principal) {
		request.setMode(-1);
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.UNDEPLOY_UNIT_REQUEST,
				request);
	}
}
