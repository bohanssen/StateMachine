package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.general.UserEntity;

/**
 * The Interface ParticipantRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:41 PM
 */
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

	public List<Participant> getPlayerByUser(UserEntity user);

	public Participant getPlayerByUserAndGame(UserEntity user, Game game);

	public Participant getPlayerByUserIdAndGameId(long userId, long gameId);
}
