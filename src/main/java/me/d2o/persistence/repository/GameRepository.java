package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.Game;
import me.d2o.statemachine.MachineStateEmbeddableEntity;

/**
 * The Interface GameRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:31 PM
 */
public interface GameRepository extends JpaRepository<Game, Long> {

	public List<Game> findByStateAndOpen(MachineStateEmbeddableEntity state, boolean open);
}
