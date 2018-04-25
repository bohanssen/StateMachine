/**
 *
 */
package me.d2o.service.gameplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.repository.TerritoryRepository;
import me.d2o.service.general.UserService;
import me.d2o.service.notification.services.ClientNotificationService;
import me.d2o.transfermodel.graphics.TerritoryClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: TerritoryService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 10:12:33 AM
 *
 */
@Service
@Transactional
public class TerritoryService {

	@Autowired
	private TerritoryRepository territoryRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ClientNotificationService notify;

	@Autowired
	private Translate translate;

	@Autowired
	private DivisionTransferService divisionTransferService;

	@Autowired
	private DivisionService divisionService;

	public boolean checkOwnership(Territory territory) {
		if (!userService.getUser().equals(territory.getUser())) {
			notify.error("region-not-yours", userService.getUser());
			return false;
		} else {
			return true;
		}
	}

	public Territory createTerritory(Game game, List<String> regionkeys, Role role) {
		Territory territory = new Territory();
		territory.setRegion(game.getScenario().getRegions().get(regionkeys.remove(0)));
		territory.setRole(role);
		return territory;
	}

	public Territory getTerritory(long gameId, int region) {
		return territoryRepository.findTerritoryByGameIdAndRegionId(gameId, region);
	}

	public List<Territory> getTerritories(Participant participant) {
		return territoryRepository.findTerritoryByGameIdAndUserId(participant.getGame().getId(),
				participant.getUser().getId());
	}

	public List<Territory> getTerritories(Game game) {
		return territoryRepository.findTerritoryByGame(game);
	}

	public List<Territory> getTerritoriesThatContainCapitol(Participant player){
		return territoryRepository.findTerritoryByGameIdAndUserIdAndTown(player.getGame().getId(), player.getUser().getId(), true);
	}
	
	public Map<String, Integer> getCapitols(Participant participant) {
		Map<String, Integer> capitols = new HashMap<>();
		for (Territory territory : territoryRepository.findTerritoryByGameIdAndUserIdAndTown(
				participant.getGame().getId(), participant.getUser().getId(), true)) {
			capitols.put(
					translate.getMessage(
							participant.getGame().getScenario().getTitleKey() + "." + territory.getRegion().getKey(),
							territory.getRegion().getKey(), participant.getUser()),
					territory.getRegion().getId());
		}
		return capitols;
	}

	public TerritoryClientModel getClientModel(Territory territory) {
		return new TerritoryClientModel(territory, divisionService.getDivisions(territory),
				divisionService.totalUnits(territory) + divisionTransferService.totalUnits(territory),
				divisionTransferService.getDivisionTransfers(territory));
	}

	public int getTotalUnits(Territory territory) {
		return territory.getDivisions().values().stream().mapToInt(d -> {
			return d.getQuantity();
		}).sum() + territory.getTransfer().stream().mapToInt(t -> {
			return t.getQuantity();
		}).sum();
	}

	public Division getUnit(Territory territory, boolean active) {
		int quantity = 0;
		Division retval = null;
		for (Division division : territory.getDivisions().values()) {
			if (division.getQuantity() > quantity && (!active || division.getActive() != 0)) {
				retval = division;
				quantity = division.getQuantity();
			}
		}
		return retval;
	}

	public Division getUnit(Territory territory) {
		return getUnit(territory, false);
	}
}
