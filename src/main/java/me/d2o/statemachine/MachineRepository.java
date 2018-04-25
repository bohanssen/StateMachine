package me.d2o.statemachine;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface MachineRepository.
 *
 * @Author: bo.hanssen
 * @since: April 25, 2018
 */
public interface MachineRepository extends JpaRepository<StateMachine, String> {

}
