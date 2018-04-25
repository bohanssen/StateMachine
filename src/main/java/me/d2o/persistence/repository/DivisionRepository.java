package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.Division;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.scenario.Unit;

/**
 * The Interface DivisionRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:36 PM
 */
public interface DivisionRepository extends JpaRepository<Division, Integer> {

	
	public Division findDivisionByTerritoryAndUnit(Territory territory, Unit unit);

	
	public List<Division> findDivisionByTerritory(Territory territory);
}
