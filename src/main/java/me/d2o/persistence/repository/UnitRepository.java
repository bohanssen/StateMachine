package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.model.scenario.Unit;

/**
 * The Interface UnitRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:37 PM
 */
public interface UnitRepository extends JpaRepository<Unit, Integer> {

	public Unit findUbnitByKeyAndRole(String key, Role role);

	public List<Unit> findByRole(Role role);

	public Unit findUnitByKey(String key);
}
