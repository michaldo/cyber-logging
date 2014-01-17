package com.cybercom.logging.ejb;

import java.util.List;

import javax.interceptor.InvocationContext;

import com.cybercom.logging.core.DebugObjectMapper;
import com.cybercom.logging.core.EntryExitMethodLogger;
import com.cybercom.logging.core.MarkerProvider;

public class EjbEntryExitMethodLogger extends EntryExitMethodLogger {
	
	private InvocationContext ic;

	protected EjbEntryExitMethodLogger(MarkerProvider markerProvider,
			DebugObjectMapper mapper, List<String> excludedCustomTypes,
			boolean excludeDefaultTypes, InvocationContext ic) {
		super(markerProvider, mapper, excludedCustomTypes, excludeDefaultTypes, ic.getTarget(), ic.getMethod(), ic.getParameters());
		this.ic = ic;
	}

	@Override
	protected Object callRealMethod() throws Exception {
		return ic.proceed();
	}
}
