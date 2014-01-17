package com.cybercom.logging.spring;

import java.util.List;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.cybercom.logging.core.DebugObjectMapper;
import com.cybercom.logging.core.MarkerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;

@Aspect
public class LoggedSpringAspect {

	private MarkerProvider markerProvider;

	private DebugObjectMapper mapper = new DebugObjectMapper();

	private List<JsonSerializer<?>> serializers;

	private List<String> excludedCustomTypes;
	
	private boolean excludeDefaultTypes = true;

	@PostConstruct
	void init() {
		if (serializers != null) {
			for (JsonSerializer<?> jsonSerializer : serializers) {
				mapper.addJsonSerializer(jsonSerializer);
			}
		}

	}

	@Around("(@within(com.cybercom.logging.Logged) || @annotation(com.cybercom.logging.Logged)) "
			+ "&& !@annotation(com.cybercom.logging.NotLogged)")
	public Object debug(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		return new SpringEntryExitMethodLogger(markerProvider, mapper, excludedCustomTypes, excludeDefaultTypes, proceedingJoinPoint).logEntryExit();
	}

	public MarkerProvider getMarkerProvider() {
		return markerProvider;
	}

	public void setMarkerProvider(MarkerProvider markerProvider) {
		this.markerProvider = markerProvider;
	}

	public List<String> getExcludedCustomTypes() {
		return excludedCustomTypes;
	}

	public void setExcludedCustomTypes(List<String> excludedCustomTypes) {
		this.excludedCustomTypes = excludedCustomTypes;
	}

	public List<JsonSerializer<?>> getSerializers() {
		return serializers;
	}

	public void setSerializers(List<JsonSerializer<?>> serializers) {
		this.serializers = serializers;
	}

}
