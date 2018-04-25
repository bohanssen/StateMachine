/**
 *
 */
package me.d2o.service.scenario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.repository.RoleRepository;

/**
 * Class: RoleService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 10:05:45 AM
 *
 */
@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public List<Role> getRole(String key) {
		return roleRepository.findRoleByKey(key);
	}

	public Role getRole(int id) {
		return roleRepository.findOne(id);
	}
}
