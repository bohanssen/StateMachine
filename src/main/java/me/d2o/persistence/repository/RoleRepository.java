package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.scenario.Role;

/**
 * The Interface RoleRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:36 PM
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

	public List<Role> findRoleByKey(String key);

	public Role findRoleByKeyAndScenarioTitleKey(String key, String titleKey);
}
