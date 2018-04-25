/**
 *
 */
package me.d2o.service.scenario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.persistence.repository.UnitRepository;
import me.d2o.transfermodel.gameplay.store.UnitClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: UnitService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 10:21:07 AM
 *
 */
@Service
public class UnitService {

	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private Translate translate;

	public Unit getUnit(String key) {
		return unitRepository.findUnitByKey(key);
	}

	public Unit getUnit(String key, Role role) {
		return unitRepository.findUbnitByKeyAndRole(key, role);
	}

	public List<Unit> getAvailableUnits(Participant participant) {
		return unitRepository.findByRole(participant.getRole());
	}

	public List<UnitClientModel> getUnits(Participant participant) {
		return unitRepository.findByRole(participant.getRole()).stream()
				.map(u -> new UnitClientModel(u)
						.setName(translate.getMessage(participant.getGame().getScenario().getTitleKey() + "."
								+ participant.getRole().getKey() + "." + u.getKey(), participant.getUser())))
				.collect(Collectors.toList());
	}
}
