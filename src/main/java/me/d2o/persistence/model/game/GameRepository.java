package me.d2o.persistence.model.game;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface MachineRepository.
 *
 * @Author: bo.hanssen
 * @since: April 25, 2018
 */
public interface GameRepository extends JpaRepository<Game, String> {

}
