package me.d2o.statemachine.core;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.MachineRepository;
import me.d2o.statemachine.config.States;

import me.d2o.statemachine.spy.State1;
import me.d2o.statemachine.spy.State2;
import me.d2o.statemachine.spy.State3;

@Service
@Transactional
public class TestUtils {

	public static final String ID ="7757274c-fb2f-48ae-8202-fd24a7e091eb";
	
	@Autowired
	private MachineRepository repo;
	
	public String get(){
		StateMachine machine = repo.findById(ID).get();
		machine.toString();
		return machine.getState();
	}
	
	public void resetSpies(){
		State1.entered = false;
		State1.exited = false;
		State2.entered = false;
		State2.exited = false;
		State3.entered = false;
		State3.exited = false;
	}
	public void reset(){
		resetSpies();
		repo.findById(ID).get().setState(States.STATE_1);
	}
}
