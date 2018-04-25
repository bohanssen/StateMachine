/**
 *
 */
package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.general.Invite;
import me.d2o.persistence.model.general.UserEntity;

/**
 * Class: InviteRepository
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 8:49:00 AM
 *
 */
public interface InviteRepository extends JpaRepository<Invite, Long> {

	public List<Invite> findByUser(UserEntity user);

	public Invite findByUserAndGame(UserEntity user, Game game);
}
