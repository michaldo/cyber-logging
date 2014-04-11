/**
 * The MIT License
 * Copyright (c) 2014 Michal Domagala
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cybercom.logging.spring;

import java.util.ArrayList;
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

   private List<Class<?>> excludedCustomTypes = new ArrayList<Class<?>>();

   private boolean excludeDefaultTypes = true;

   @PostConstruct
   void init() {
      if (serializers != null) {
         for (JsonSerializer<?> jsonSerializer : serializers) {
            mapper.addJsonSerializer(jsonSerializer);
         }
      }
   }

   @Around("(@within(com.cybercom.logging.spring.Logged) || @annotation(com.cybercom.logging.spring.Logged)) "
         + "&& !@annotation(com.cybercom.logging.core.NotLogged)")
   public Object debug(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

      return new SpringEntryExitMethodLogger(markerProvider, mapper, excludedCustomTypes, excludeDefaultTypes,
            proceedingJoinPoint).logEntryExit();
   }

   public MarkerProvider getMarkerProvider() {
      return markerProvider;
   }

   public void setMarkerProvider(MarkerProvider markerProvider) {
      this.markerProvider = markerProvider;
   }

   public void setExcludedCustomTypes(List<String> excludedNames) throws ClassNotFoundException {
      for (String className : excludedNames) {
         excludedCustomTypes.add(Class.forName(className));
      }
   }

   public void setSerializers(List<JsonSerializer<?>> serializers) {
      this.serializers = serializers;
   }

   
   public void setExcludeDefaultTypes(boolean excludeDefaultTypes) {
      this.excludeDefaultTypes = excludeDefaultTypes;
   }

}
