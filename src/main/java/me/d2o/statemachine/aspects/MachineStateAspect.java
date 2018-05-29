package me.d2o.statemachine.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import me.d2o.statemachine.annotations.EnterMachineState;
import me.d2o.statemachine.annotations.ExitMachineState;
import me.d2o.statemachine.core.MachineEvent;


@Component
@Aspect
public class MachineStateAspect {
	
    @Around(value = "@annotation(me.d2o.statemachine.annotations.EnterMachineState)")
    public Object enterMachineState(ProceedingJoinPoint joinPoint) throws Throwable {
    	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EnterMachineState myAnnotation = method.getAnnotation(EnterMachineState.class);
        
        MachineEvent event = (MachineEvent) joinPoint.getArgs()[0];
        if (!event.isTerminated() && event.getEnterState().equals(myAnnotation.value())){
        	return joinPoint.proceed();
        } else {
        	return null;
        }
    }

    @Around(value = "@annotation(me.d2o.statemachine.annotations.ExitMachineState)")
    public Object exitMachineState(ProceedingJoinPoint joinPoint) throws Throwable {
    	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ExitMachineState myAnnotation = method.getAnnotation(ExitMachineState.class);
        
        MachineEvent event = (MachineEvent) joinPoint.getArgs()[0];
        if (event.getExitState().equals(myAnnotation.value())){
        	return joinPoint.proceed();
        } else {
        	return null;
        }
    }
}
