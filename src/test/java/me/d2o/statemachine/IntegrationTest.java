package me.d2o.statemachine;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.config.MachineRepository;
import me.d2o.statemachine.config.StateMachineConfigurable;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.StateMachine;
import me.d2o.statemachine.core.StateMachineService;
import me.d2o.statemachine.spy.Event1;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={IntegrationTest.class})
@TestPropertySource(locations="classpath:test.properties")
@ComponentScan
@EnableAutoConfiguration
public class IntegrationTest {

	private static final String ID ="7757274c-fb2f-48ae-8202-fd24a7e091eb";
	
	@TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
  
        @Bean
        public StateMachineConfigurable stateMachineConfigurable() {	
    		StateMachineConfigurable fsm = new StateMachineConfigurable(Events.class,States.class);
    		
    		fsm.addTransition(Events.EVENT_1, States.STATE_1, States.STATE_2);
    		fsm.addTransition(Events.EVENT_2, States.STATE_2, States.STATE_3);
    		fsm.addTransition(Events.EVENT_3, States.STATE_3, States.STATE_1);
    		
    		return fsm;
    	}
    }
	
	@Autowired
	private StateMachineService fsm;
	
	@Autowired
	private MachineRepository repo;
	
	private StateMachine get(){
		return repo.findById(ID).get();
	}
	
	@Test
	public void happyFlow(){
		assertEquals(States.STATE_1,get().getState());
		fsm.triggerTransition(ID, Events.EVENT_1);
		System.out.println(Event1.triggered);
		assertEquals(States.STATE_2,get().getState());
		fsm.triggerTransition(ID, Events.EVENT_2);
		assertEquals(States.STATE_3,get().getState());
		fsm.triggerTransition(ID, Events.EVENT_3);
		assertEquals(States.STATE_1,get().getState());
	}
	
	@Test
	public void faultyEvent(){
		assertEquals(States.STATE_1,get().getState());
		Event1.triggered = false;
		fsm.triggerTransition(ID, Events.EVENT_2);
		System.out.println(Event1.triggered);
		assertEquals(States.STATE_1,get().getState());
	}
}
