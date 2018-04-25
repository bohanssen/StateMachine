package me.d2o.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.Battle;

/**
 * The Interface BattleRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:39 PM
 */
public interface BattleRepository extends JpaRepository<Battle, Long> {

}
