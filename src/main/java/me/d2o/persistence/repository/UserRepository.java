package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.d2o.persistence.model.general.UserEntity;

/**
 * The Interface UserRepository.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:26:04 PM
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	public UserEntity findByUniqueUserId(String uniqueUserId);

	public List<UserEntity> findByRoleNotLike(String role);

	public UserEntity findByUsername(String userName);

	public List<UserEntity> getUserByOnline(boolean online);
}
