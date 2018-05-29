package me.d2o.statemachine.annotations;

import java.lang.annotation.*;

import javax.transaction.Transactional;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@Order(50)
@EventListener
public @interface ExitMachineState {
	
	public String value();
	
}
