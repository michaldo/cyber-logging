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
package com.cybercom.logging.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import com.cybercom.logging.core.DebugObjectMapper;
import com.cybercom.logging.core.EntryExitMethodLogger;
import com.cybercom.logging.core.MarkerProvider;


public class CglibEntryExitMethodLogger extends EntryExitMethodLogger {
   
   private Object proxy;
   private MethodProxy fastMethod;
   

   public CglibEntryExitMethodLogger(MarkerProvider markerProvider, DebugObjectMapper mapper,
         List<Class<?>> excludedCustomTypes, boolean excludeDefaultTypes, Object target, Method method, Object[] args, Object proxy, MethodProxy fastMethod) {
      super(markerProvider, mapper, excludedCustomTypes, excludeDefaultTypes, target, method, args);
      this.fastMethod = fastMethod;
      this.proxy = proxy;
   }

   @Override
   protected Object callRealMethod() throws Throwable {
      try {
         return fastMethod.invokeSuper(proxy, args);
      } catch (InvocationTargetException e) {
         throw e.getCause();
      }
   }

}
