package com.cybercom.logging.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.cybercom.logging.core.DebugObjectMapper;
import com.cybercom.logging.core.EntryExitMethodLogger;
import com.cybercom.logging.core.MarkerProvider;

public class ProxyEntryExitMethodLogger extends EntryExitMethodLogger {

   public ProxyEntryExitMethodLogger(MarkerProvider markerProvider, DebugObjectMapper mapper,
         List<String> excludedCustomTypes, boolean excludeDefaultTypes, Object target, Method method, Object[] args) {
      super(markerProvider, mapper, excludedCustomTypes, excludeDefaultTypes, target, method, args);
   }

   @Override
   protected Object callRealMethod() throws Throwable {
      try {
         return method.invoke(target, args);
      } catch (InvocationTargetException e) {
         throw e.getCause();
      }
   }

}
