package me.d2o.statemachine;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import me.d2o.statemachine.config.Events;
import me.d2o.statemachine.config.StateMachineConfigurable;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.config.configexceptiontests.EventsExceptionTest;
import me.d2o.statemachine.config.configexceptiontests.EventsNotFinal;
import me.d2o.statemachine.config.configexceptiontests.EventsNotStatic;
import me.d2o.statemachine.config.configexceptiontests.EventsNotString;
import me.d2o.statemachine.config.configexceptiontests.StatesNotUniqueTest;
import me.d2o.statemachine.core.MachineCore;
import me.d2o.statemachine.core.MachineEvent;
import me.d2o.statemachine.core.StateMachineService;
import me.d2o.statemachine.core.TestUtils;
import me.d2o.statemachine.eventhandler.MachineEventHandler;
import me.d2o.statemachine.exceptions.MachineEventHandlerConfigurationException;
import me.d2o.statemachine.exceptions.StateMachineConfigurationException;
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
	
	@Autowired
	private StateMachineConfigurable config;
	
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
	
	@Test
	public void falsePrecheckTest(){
		utils.reset();
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_5);
	}
	
	@Test
	public void faultyEventTransistionConfigTest(){
		boolean exception = false;
		try {
			config.addTransition("UNKNOWN EVENT", States.STATE_1, States.STATE_1);
		} catch (StateMachineConfigurationException ex){
			assertEquals("Passed an invalid event or state to the Machine configuration", ex.getMessage());
			exception = true;
		}
		assertTrue(exception);
	}
	
	@Test
	public void faultyStateTransistionConfigTest(){
		boolean exception = false;
		try {
			config.addTransition(Events.EVENT_1, "UNKNOWN STATE", States.STATE_1);
		} catch (StateMachineConfigurationException ex){
			assertEquals("Passed an invalid event or state to the Machine configuration", ex.getMessage());
			exception = true;
		}
		assertTrue(exception);
	}
	
	@Test
	public void eventsNotUniqueTest(){
		boolean exception = false;
		try {
			new StateMachineConfigurable(EventsExceptionTest.class, States.class);
		} catch (StateMachineConfigurationException ex){
			assertTrue(ex.getMessage().startsWith("Value is not unique"));
			exception = true;
		}
		assertTrue(exception);
	}
	
	@Test
	public void statesNotUniqueTest(){
		boolean exception = false;
		try {
			new StateMachineConfigurable(Events.class, StatesNotUniqueTest.class);
		} catch (StateMachineConfigurationException ex){
			assertTrue(ex.getMessage().startsWith("Value is not unique"));
			exception = true;
		}
		assertTrue(exception);
	}
	
	@Test
	public void eventsNotStaticTest(){
		boolean exception = false;
		try {
			new StateMachineConfigurable(EventsNotStatic.class, States.class);
		} catch (StateMachineConfigurationException ex){
			assertTrue(ex.getMessage().startsWith("Please use only public static final String fields in your configuration:"));
			exception = true;
		}
		assertTrue(exception);
	}
	
	@Test
	public void eventsNotFinalTest(){
		boolean exception = false;
		try {
			new StateMachineConfigurable(EventsNotFinal.class, States.class);
		} catch (StateMachineConfigurationException ex){
			assertTrue(ex.getMessage().startsWith("Please use only public static final String fields in your configuration:"));
			exception = true;
		}
		assertTrue(exception);
	}
	
	@Test
	public void eventsNotStringTest(){
		boolean exception = false;
		try {
			new StateMachineConfigurable(EventsNotString.class, States.class);
		} catch (StateMachineConfigurationException ex){
			assertTrue(ex.getMessage().startsWith("Please use only public static final String fields in your configuration:"));
			exception = true;
		}
		assertTrue(exception);
	}

}
