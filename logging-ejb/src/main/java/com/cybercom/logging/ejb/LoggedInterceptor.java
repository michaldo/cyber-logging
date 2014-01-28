package com.cybercom.logging.ejb;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.cybercom.logging.core.DebugObjectMapper;

public class LoggedInterceptor {

   private DebugObjectMapper mapper = new DebugObjectMapper();

   @AroundInvoke
   public Object debug(InvocationContext ic) throws Exception, Throwable {
      return new EjbEntryExitMethodLogger(null, mapper, null, true, ic).logEntryExit();
   }

}
