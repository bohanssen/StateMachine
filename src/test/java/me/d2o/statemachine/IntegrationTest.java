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
import me.d2o.statemachine.config.StateMachineConfigurable;
import me.d2o.statemachine.config.States;
import me.d2o.statemachine.config.configexceptiontests.EventsExceptionTest;
import me.d2o.statemachine.config.configexceptiontests.EventsNotFinal;
import me.d2o.statemachine.config.configexceptiontests.EventsNotStatic;
import me.d2o.statemachine.config.configexceptiontests.EventsNotString;
import me.d2o.statemachine.config.configexceptiontests.StatesNotUniqueTest;
import me.d2o.statemachine.core.MachineCore;
import me.d2o.statemachine.core.StateMachineService;
import me.d2o.statemachine.core.TestUtils;
import me.d2o.statemachine.exceptions.StateMachineConfigurationException;
import me.d2o.statemachine.spy.State5;
import me.d2o.statemachine.spy.State5Control;
import me.d2o.statemachine.spy.State1;
import me.d2o.statemachine.spy.State2;
import me.d2o.statemachine.spy.State3;

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
	
	private void threeStatesCheck(	boolean i1, boolean o1,
									boolean i2, boolean o2,
									boolean i3, boolean o3){
		assertEquals(i1, State1.entered);
		assertEquals(o1, State1.exited);
		
		assertEquals(i2, State2.entered);
		assertEquals(o2, State2.exited);
		
		assertEquals(i3, State3.entered);
		assertEquals(o3, State3.exited);
	}
	
	@Test
	public void happyFlow(){
		utils.reset();
		assertEquals(States.STATE_1,utils.get());
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_1);
		threeStatesCheck(false, true, true, false, false, false);
		assertEquals(States.STATE_2,utils.get());
		
		utils.resetSpies();
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_2);
		threeStatesCheck(false, false, false, true, true, false);
		assertEquals(States.STATE_3,utils.get());
		
		utils.resetSpies();
		fsm.triggerTransition(TestUtils.ID, Events.EVENT_3);
		threeStatesCheck(true, false, false, false, false, true);
		assertEquals(States.STATE_1,utils.get());
	}
	
//	@Test
//	public void faultyEvent(){
//		utils.reset();
//		assertEquals(States.STATE_1,utils.get());
//		fsm.triggerTransition(TestUtils.ID, Events.EVENT_2);
//		threeStatesCheck(false, false, false, false, false, false);
//		assertEquals(States.STATE_1,utils.get());
//	}
//	
//	@Test
//	public void concurentLockTest() throws InterruptedException{
//		utils.reset();
//		assertEquals(States.STATE_1,utils.get());
//		fsm.triggerTransition(TestUtils.ID, Events.EVENT_4);
//		Thread.sleep(500);
//		assertEquals(States.STATE_1,utils.get());
//	}
//	
//	@Test
//	public void unknowMachineTest(){
//		MachineCore.machineLookUpTimeOut = 1000;
//		fsm.triggerTransition("UNKNOWN", Events.EVENT_1);
//	}
//	
//	@Test
//	public void falsePrecheckTest(){
//		utils.reset();
//		fsm.triggerTransition(TestUtils.ID, Events.EVENT_5);
//		assertFalse(State5.entered);
//		assertTrue(State5Control.entered);
//	}
//	
//	@Test
//	public void faultyEventTransistionConfigTest(){
//		boolean exception = false;
//		try {
//			config.addTransition("UNKNOWN EVENT", States.STATE_1, States.STATE_1);
//		} catch (StateMachineConfigurationException ex){
//			assertEquals("Passed an invalid event or state to the Machine configuration", ex.getMessage());
//			exception = true;
//		}
//		assertTrue(exception);
//	}
//	
//	@Test
//	public void faultyStateTransistionConfigTest(){
//		boolean exception = false;
//		try {
//			config.addTransition(Events.EVENT_1, "UNKNOWN STATE", States.STATE_1);
//		} catch (StateMachineConfigurationException ex){
//			assertEquals("Passed an invalid event or state to the Machine configuration", ex.getMessage());
//			exception = true;
//		}
//		assertTrue(exception);
//	}
//	
//	@Test
//	public void eventsNotUniqueTest(){
//		boolean exception = false;
//		try {
//			new StateMachineConfigurable(EventsExceptionTest.class, States.class);
//		} catch (StateMachineConfigurationException ex){
//			assertTrue(ex.getMessage().startsWith("Value is not unique"));
//			exception = true;
//		}
//		assertTrue(exception);
//	}
//	
//	@Test
//	public void statesNotUniqueTest(){
//		boolean exception = false;
//		try {
//			new StateMachineConfigurable(Events.class, StatesNotUniqueTest.class);
//		} catch (StateMachineConfigurationException ex){
//			assertTrue(ex.getMessage().startsWith("Value is not unique"));
//			exception = true;
//		}
//		assertTrue(exception);
//	}
//	
//	@Test
//	public void eventsNotStaticTest(){
//		boolean exception = false;
//		try {
//			new StateMachineConfigurable(EventsNotStatic.class, States.class);
//		} catch (StateMachineConfigurationException ex){
//			assertTrue(ex.getMessage().startsWith("Please use only public static final String fields in your configuration:"));
//			exception = true;
//		}
//		assertTrue(exception);
//	}
//	
//	@Test
//	public void eventsNotFinalTest(){
//		boolean exception = false;
//		try {
//			new StateMachineConfigurable(EventsNotFinal.class, States.class);
//		} catch (StateMachineConfigurationException ex){
//			assertTrue(ex.getMessage().startsWith("Please use only public static final String fields in your configuration:"));
//			exception = true;
//		}
//		assertTrue(exception);
//	}
//	
//	@Test
//	public void eventsNotStringTest(){
//		boolean exception = false;
//		try {
//			new StateMachineConfigurable(EventsNotString.class, States.class);
//		} catch (StateMachineConfigurationException ex){
//			assertTrue(ex.getMessage().startsWith("Please use only public static final String fields in your configuration:"));
//			exception = true;
//		}
//		assertTrue(exception);
//	}

}
