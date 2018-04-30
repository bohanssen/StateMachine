package me.d2o.statemachine.config;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.statemachine.core.StateMachine;

public interface MachineRepository extends JpaRepository<StateMachine, String> {

}
