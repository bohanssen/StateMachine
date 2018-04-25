/**
 *
 */
package me.d2o.eventhandlers.setup.units;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.game.UnassignedUnits;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.gameplay.UnassignedUnitService;
import me.d2o.service.general.StompService;
import me.d2o.service.general.UserService;
import me.d2o.service.notification.services.ClientNotificationService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.DeployRequest;

/**
 * Class: DeployUnitHandler
 *
 * @author bo.hanssen
 * @since Jan 27, 2017 4:09:32 PM
 *
 */
@Service
@Transactional
public class DeployUnitHandler extends GameEventHandler {

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private ClientNotificationService notify;

	@Autowired
	private UserService userService;

	@Autowired
	private StompService stomp;

	@Autowired
	private UnassignedUnitService unassignedUnitService;

	@Override
	public void handleEvent(GameEvent event) {
		DeployRequest request = (DeployRequest) event.getBody();
		Territory territory = territoryService.getTerritory(request.getGameId(), request.getRegion());
		UserEntity user = userService.getUser();

		if (!territoryService.checkOwnership(territory)) {
			return;
		}

		Participant player = participantService.getParticipant(event);
		UnassignedUnits unassigned = player.getUnassignedUnits().get(request.getUnitKey());
		if (request.getMode() == 1 && unassigned.getQuantity() == 0) {
			notify.error("no-units-available", user);
			return;
		}

		Division division = territory.getDivisions().get(request.getUnitKey());
		if (request.getMode() == 1 && division == null) {
			division = new Division();
			division.setUnit(unassigned.getUnit());
			territory.addDivision(division);
		}

		division.incrementQuantity(1 * request.getMode());
		unassigned.incrementQuantity(-1 * request.getMode());
		logger.info("Deploy unit [{}] to region [{}] for role [{}]", unassigned.getKey(), territory.getKey(),
				territory.getRole().getKey());
		stomp.pushTerritoryUpdate(territory);

		Map<String, Integer> unasignedUpdate = new HashMap<>();
		player.getUnassignedUnits().forEach((k, u) -> unasignedUpdate.put(k, u.getQuantity()));
		stomp.push("unitsUpdate", unasignedUpdate);
		unassignedUnitService.allUnitsSet(event);
	}

	@Override
	public String eventType() {
		return Events.DEPLOY_UNIT;
	}

}
