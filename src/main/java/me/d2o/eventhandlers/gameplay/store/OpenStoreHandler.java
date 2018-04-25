/**
 *
 */
package me.d2o.eventhandlers.gameplay.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Participant;
import me.d2o.service.gameplay.ParticipantService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.StompService;
import me.d2o.service.scenario.UnitService;
import me.d2o.statemachine.GameEvent;
import me.d2o.statemachine.GameEventHandler;
import me.d2o.transfermodel.gameplay.store.StoreModel;
import me.d2o.transfermodel.gameplay.store.UnitClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: OpenStoreHandler
 *
 * @author bo.hanssen
 * @since Jan 30, 2017 5:10:07 PM
 *
 */
@Service
@Transactional
public class OpenStoreHandler extends GameEventHandler {

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private StompService stomp;

	@Autowired
	private Translate i18n;

	@Override
	public void handleEvent(GameEvent event) {
		Participant player = participantService.getParticipant(event);
		StoreModel model = new StoreModel();
		model.setCapitols(territoryService.getCapitols(player));
		model.setUnits(unitService.getUnits(player));
		addItems(player, model);
		logger.info("Open store for user [{}]", player.getUser().getUsername());
		stomp.push("store", model);
		stomp.pushInfo(event);
	}

	public void addItems(Participant player, StoreModel model) {
		player.getRole().getItems().forEach((k, v) -> {
			UnitClientModel item = new UnitClientModel();
			item.setKey("item." + k);
			String label = player.getGame().getScenario().getTitleKey() + "." + player.getRole().getKey() + ".item."
					+ k;
			item.setName(i18n.getMessage(label, player.getUser()));
			item.setCost(v);
			model.getUnits().add(item);
		});
	}

	@Override
	public String eventType() {
		return Events.OPEN_STORE;
	}

}
