package me.d2o.statemachine.utils;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.statemachine.core.StateMachine;

/**
 * The Interface MachineRepository.
 *
 * @Author: bo.hanssen
 * @since: April 25, 2018
 */
public interface MachineRepository extends JpaRepository<StateMachine, String> {

}
