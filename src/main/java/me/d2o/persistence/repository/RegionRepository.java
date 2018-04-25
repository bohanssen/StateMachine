package me.d2o.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.scenario.Region;
import me.d2o.persistence.model.scenario.Scenario;

/**
 * The Interface RegionRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:33 PM
 */
public interface RegionRepository extends JpaRepository<Region, Integer> {

	
	public Region getRegionByKeyAndScenario(String key, Scenario scenario);
}
