/**
 *
 */
package me.d2o.service.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.persistence.repository.DivisionRepository;
import me.d2o.service.general.UserService;
import me.d2o.transfermodel.gameplay.DivisionClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: DivisionService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 10:18:16 AM
 *
 */
@Service
@Transactional
public class DivisionService {

	@Autowired
	private Translate translate;

	@Autowired
	private UserService userService;

	@Autowired
	private DivisionRepository divisionRepository;

	public Division createDivision(int quantity, Unit unit) {
		Division division = new Division();
		division.setQuantity(quantity);
		division.setUnit(unit);
		return division;
	}

	public Division getOrCreateDivision(Territory territory, Unit unit) {
		Division division = territory.getDivisions().get(unit.getKey());
		if (division == null) {
			division = new Division();
			division.setUnit(unit);
			territory.addDivision(division);
		}
		return division;
	}

	public void clearAll(Territory territory) {
		if (territory.getDivisions() != null) {
			List<Division> divisions = new ArrayList<>();
			Object[] keys = territory.getDivisions().keySet().toArray();
			for (Object key : keys) {
				Division division = territory.getDivisions().get(key);
				division.markForDeletion();
				divisions.add(division);
			}
			divisionRepository.deleteInBatch(divisions);
		}
	}

	public List<DivisionClientModel> getDivisions(Territory territory) {
		return territory.getDivisions().values().stream().filter(d -> d.getQuantity() != 0)
				.map(u -> new DivisionClientModel(u.getKey(),
						translate.getMessage(territory.getGame().getScenario().getTitleKey() + "."
								+ territory.getRole().getKey() + "." + u.getUnit().getKey(), userService.getUser()),
						u.getQuantity(), u.getActive(), u.getResting(), u.getUnit().getOffence(),
						u.getUnit().getDefence()))
				.collect(Collectors.toList());
	}

	public int totalUnits(Territory territory) {
		return territory.getDivisions().values().stream().mapToInt(d -> d.getQuantity()).sum();
	}

	public int balanceAttackDefence(Territory territory, Territory enemy) {
		int attack = territory.getDivisions().values().stream().mapToInt(d -> {
			return d.getActive() * d.getUnit().getOffence();
		}).sum();
		int defence = enemy.getDivisions().values().stream().mapToInt(d -> {
			return d.getActive() * d.getUnit().getDefence();
		}).sum();
		return attack - defence;
	}

	public Unit selectUnit(Territory territory, Territory enemy) {
		Unit selected = null;
		int score = 0;
		int defence = 0;

		for (Division division : enemy.getDivisions().values()) {
			if (division.getActive() != 0 && division.getUnit().getDefence() > defence) {
				defence = division.getUnit().getDefence();
			}
		}

		for (Division division : territory.getDivisions().values()) {
			int tempScore = division.getActive() * division.getUnit().getOffence();
			if (tempScore > score) {
				score = tempScore;
				selected = division.getUnit();
			}
		}

		return selected;
	}

	public void save(Division division) {
		divisionRepository.save(division);
	}
}
