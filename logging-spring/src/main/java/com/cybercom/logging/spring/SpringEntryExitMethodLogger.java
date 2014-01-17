package com.cybercom.logging.spring;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.cybercom.logging.core.DebugObjectMapper;
import com.cybercom.logging.core.EntryExitMethodLogger;
import com.cybercom.logging.core.MarkerProvider;

public class SpringEntryExitMethodLogger extends EntryExitMethodLogger {
	
	private ProceedingJoinPoint proceedingJoinPoint;

	public SpringEntryExitMethodLogger(
			MarkerProvider markerProvider,
			DebugObjectMapper mapper, 
			List<String> excludedCustomTypes,
			boolean excludeDefaultTypes, 
			ProceedingJoinPoint proceedingJoinPoint) {
		super(
				markerProvider, 
				mapper, 
				excludedCustomTypes, 
				excludeDefaultTypes,
				proceedingJoinPoint.getTarget(),
				((MethodSignature) proceedingJoinPoint.getSignature()).getMethod(), 
				proceedingJoinPoint.getArgs());
		this.proceedingJoinPoint = proceedingJoinPoint;
	}

	
	@Override
	protected Object callRealMethod() throws Throwable {
		return proceedingJoinPoint.proceed();
	}

}
