package com.cybercom.logging.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class EntryExitMethodLogger logs method entry and exit
 */
public abstract class EntryExitMethodLogger {

   private final static Logger LOGGER = LoggerFactory.getLogger(EntryExitMethodLogger.class);

   private final static List<String> EXCLUDED_DEFAULT_TYPES = Arrays
         .asList(new String[] {"javax.servlet.ServletRequest", "javax.servlet.ServletResponse",
               "javax.servlet.http.HttpSession", "org.springframework.validation.BindingResult",
               "org.springframework.validation.support.BindingAwareModelMap"});

   protected MarkerProvider markerProvider;

   private DebugObjectMapper mapper;

   private List<String> excludedCustomTypes;

   private boolean excludeDefaultTypes = true;

   protected Object target;

   protected Method method;

   protected Object[] args;

   protected EntryExitMethodLogger(MarkerProvider markerProvider, DebugObjectMapper mapper,
         List<String> excludedCustomTypes, boolean excludeDefaultTypes, Object target, Method method, Object[] args) {
      this.markerProvider = markerProvider;
      this.mapper = mapper;
      this.excludedCustomTypes = excludedCustomTypes;
      this.excludeDefaultTypes = excludeDefaultTypes;
      this.target = target;
      this.method = method;
      this.args = args;
   }

   /**
    * Log entry exit do: 1. Log method enty 2. Call method 3. Log method exit or exception if thrown
    * 
    * @return the object
    * @throws Throwable the throwable
    */
   public Object logEntryExit() throws Throwable {

      if (!LOGGER.isDebugEnabled()) {
         return callRealMethod();
      }
      if (isMethodExcludedByNotLoggedAnnotation()) {
         return callRealMethod();
      }

      String marker = "";

      if (markerProvider != null) {
         marker = "<" + markerProvider.getMarker() + "> ";
      }

      String targetName = getTargetName();
      Object[] debugableArgs = getDebugableArguments();

      String methodName = method.getName();

      if (debugableArgs.length == 0) {
         LOGGER.debug("Entering {}{}.{}", marker, targetName, methodName);
      } else {
         LOGGER.debug("Entering {}{}.{} with {}", marker, targetName, methodName,
               mapper.writeValueAsString(debugableArgs));
      }

      try {
         Object result = callRealMethod();
         if (isVoidReturned() || isExcludedByDefaultType(result) || isExcludedByCustomType(result)) {
            LOGGER.debug("Exiting {}{}.{}", marker, targetName, methodName);
         } else {
            LOGGER.debug("Exiting {}{}.{} with {}", marker, targetName, methodName, mapper.writeValueAsString(result));
         }
         return result;
      } catch (Exception e) {
         LOGGER.debug("Exiting {}{}.{} with exception {}", marker, targetName, methodName, e.getMessage());
         throw e;
      } catch (Error e) {
         LOGGER.debug("Exiting {}{}.{} with exception {}", marker, targetName, methodName, e.getMessage());
         throw e;
      }
   }

   private Object[] getDebugableArguments() {
      Annotation paramAnnotation[][] = method.getParameterAnnotations();

      List<Object> debugableArgs = new ArrayList<Object>();
      if (args != null) {
         debugableArgs.addAll(Arrays.asList(args));
      }

      excludeByAnnotationNotLogged(debugableArgs, paramAnnotation);
      excludeByDefaultType(debugableArgs);
      excludeByCustomType(debugableArgs);
      return debugableArgs.toArray();
   }

   private void excludeByDefaultType(List<Object> debugableArgs) {
      if (!excludeDefaultTypes) {
         return;
      }
      for (Iterator<?> iterator = debugableArgs.iterator(); iterator.hasNext();) {
         Object arg = iterator.next();
         if (isExcludedByDefaultType(arg)) {
            iterator.remove();
         }
      }
   }

   private void excludeByCustomType(List<Object> debugableArgs) {
      if (excludedCustomTypes == null) {
         return;
      }
      for (Iterator<?> iterator = debugableArgs.iterator(); iterator.hasNext();) {
         Object arg = iterator.next();
         if (isExcludedByCustomType(arg)) {
            iterator.remove();
         }
      }
   }

   private void excludeByAnnotationNotLogged(List<Object> debugableArgs, Annotation[][] paramAnnotation) {
      int argIndex = 0;
      for (Iterator<?> argIterator = debugableArgs.iterator(); argIterator.hasNext(); argIndex++) {
         argIterator.next();
         for (Annotation annotation : paramAnnotation[argIndex]) {
            if (NotLogged.class.isInstance(annotation)) {
               argIterator.remove();
               break;
            }
         }
      }
   }

   private boolean isExcludedByDefaultType(Object o) {
      if (!excludeDefaultTypes) {
         return false;
      }
      return EXCLUDED_DEFAULT_TYPES.contains(o.getClass().getName());
   }

   private boolean isExcludedByCustomType(Object o) {
      if (excludedCustomTypes == null) {
         return false;
      }
      return excludedCustomTypes.contains(o.getClass().getName());
   }

   private boolean isMethodExcludedByNotLoggedAnnotation() {
      Annotation[] methodAnnotations = method.getAnnotations();
      for (Annotation annotation : methodAnnotations) {
         if (NotLogged.class.isAssignableFrom(annotation.getClass())) {
            return true;
         }
      }
      return false;
   }

   protected abstract Object callRealMethod() throws Throwable;

   protected String getTargetName() {
      return target.getClass().getSimpleName();
   }

   protected boolean isVoidReturned() {
      return method.getReturnType().equals(Void.TYPE);
   }

}
