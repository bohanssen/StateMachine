package me.d2o.statemachine.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import me.d2o.statemachine.annotations.EnterMachineState;
import me.d2o.statemachine.annotations.ExitMachineState;
import me.d2o.statemachine.exceptions.MachineEventHandlerConfigurationException;
import me.d2o.statemachine.exceptions.StateMachineConfigurationException;

@Component
public class ConfigValidator implements BeanPostProcessor {
 
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private StateMachineConfigurable smc;
	
	@Autowired
	ConfigValidator(StateMachineConfigurable smc){
		this.smc = smc;
	}
	
	private void validate(String state){
		try {
			smc.checkIfStateIsValid(state);
		} catch (StateMachineConfigurationException ex){
			MachineEventHandlerConfigurationException e = new MachineEventHandlerConfigurationException("Could not construct the MachineEventHandler ["+this.getClass()+"] because the 'State' method returns an invalid String",ex);
			logger.error("Bad configuration",e);
			throw e;
		}
	}
	
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
      throws BeansException {
    	
    	//Check for method annotations Enter/Exit
    	for(Method method: bean.getClass().getMethods()){
    	    if(method.isAnnotationPresent(EnterMachineState.class)){
    	    	validate(method.getAnnotation(EnterMachineState.class).value());
    	    }
    	    if(method.isAnnotationPresent(ExitMachineState.class)){
    	    	validate(method.getAnnotation(ExitMachineState.class).value());
    	    }
    	}
    	
        return bean;
    }
 
}
