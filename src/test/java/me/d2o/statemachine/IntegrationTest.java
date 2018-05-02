package me.d2o.statemachine;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.core.MachineCore;
import me.d2o.statemachine.core.StateMachineService;
import me.d2o.statemachine.core.TestUtils;
import me.d2o.statemachine.spy.Event1;
import me.d2o.statemachine.spy.Event2;
import me.d2o.statemachine.spy.Event3;
import me.d2o.statemachine.spy.Event4;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={IntegrationTest.class})
@TestPropertySource(locations="classpath:test.properties")
@ComponentScan
@EnableAutoConfiguration
public class IntegrationTest {
	
	@Autowired
	private StateMachineService fsm;
	
	@Autowired
	private TestUtils utils;
	
	@Test
	public void happyFlow(){
		utils.reset();
		assertEquals(States.STATE_1,utils.get());
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_1);
		assertTrue(Event1.triggered);
		assertEquals(States.STATE_2,utils.get());
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_2);
		assertTrue(Event2.triggered);
		assertEquals(States.STATE_3,utils.get());
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_3);
		assertTrue(Event3.triggered);
		assertEquals(States.STATE_1,utils.get());
	}
	
	@Test
	public void faultyEvent(){
		utils.reset();
		assertEquals(States.STATE_1,utils.get());
		Event1.triggered = false;
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_2);
		assertFalse(Event1.triggered);
		assertEquals(States.STATE_1,utils.get());
	}
	
	@Test
	public void concurentLockTest() throws InterruptedException{
		utils.reset();
		assertEquals(States.STATE_1,utils.get());
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_4);
		Thread.sleep(500);
		assertEquals(States.STATE_1,utils.get());
		assertFalse(Event1.triggered);
		assertTrue(Event2.triggered);
		assertTrue(Event3.triggered);
		assertTrue(Event4.triggered);
	}
	
	@Test
	public void unknowMachineTest(){
		MachineCore.machineLookUpTimeOut = 1000;
		fsm.triggerTransition("UNKNOWN", Events.EVENT_1);
	}
}
