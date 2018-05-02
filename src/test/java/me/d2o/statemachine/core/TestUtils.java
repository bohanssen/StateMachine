package me.d2o.statemachine.core;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.statemachine.config.MachineRepository;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.spy.Event1;
import me.d2o.statemachine.spy.Event2;
import me.d2o.statemachine.spy.Event3;
import me.d2o.statemachine.spy.Event4;

@Service
@Transactional
public class TestUtils {

	public static final String ID ="7757274c-fb2f-48ae-8202-fd24a7e091eb";
	
	@Autowired
	private MachineRepository repo;
	
	public String get(){
		return repo.findById(ID).get().getState();
	}
	
	public void reset(){
		Event1.triggered = false;
		Event2.triggered = false;
		Event3.triggered = false;
		Event4.triggered = false;
		repo.findById(ID).get().setState(States.STATE_1);
	}
}
