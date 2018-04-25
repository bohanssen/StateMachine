package me.d2o.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.scenario.Scenario;

/**
 * The Interface ScenarioRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:38 PM
 */
public interface ScenarioRepository extends JpaRepository<Scenario, String> {

}
