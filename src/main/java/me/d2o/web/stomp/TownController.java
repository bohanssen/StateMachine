/**
 *
 */
package me.d2o.web.stomp;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.StateMachineService;
import me.d2o.transfermodel.gameplay.DeployRequest;

/**
 * Class: TownController
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 11:28:05 AM
 *
 */
@Controller
@Transactional
public class TownController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private StateMachineService fsm;

	@MessageMapping("/placetown")
	public void deploy(DeployRequest request, Principal principal) {
		userService.authenticate(principal);
		logger.debug("Received place town request");
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.PLACE_TOWN_REQUEST, request);
	}
}
