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
import me.d2o.transfermodel.gameplay.store.PurchaseUnitRequest;

/**
 * Class: UnitStoreController
 *
 * @author bo.hanssen
 * @since Jan 31, 2017 9:27:42 AM
 *
 */
@Controller
@Transactional
public class UnitStoreController {

	@Autowired
	private UserService userService;

	@Autowired
	private StateMachineService fsm;

	@MessageMapping("/buyunit")
	public void buy(Principal principal, PurchaseUnitRequest request) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), request.getGameId(), Events.PURCHASE_REQUEST, request);
	}

	@MessageMapping("/closestore")
	public void close(Principal principal, long gameId) {
		userService.authenticate(principal);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.CLOSE_STORE);
	}

}
