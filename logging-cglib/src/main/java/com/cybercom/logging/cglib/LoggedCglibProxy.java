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

import java.lang.reflect.Method;

import com.cybercom.logging.core.DebugObjectMapper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


public class LoggedCglibProxy implements MethodInterceptor {
   
   private DebugObjectMapper mapper = new DebugObjectMapper();
   
   private Object bean;

   private LoggedCglibProxy(Object bean) {
      this.bean = bean;
   }
   
   @SuppressWarnings("unchecked")
   public static <T> T createProxy(T bean) {
      Enhancer e = new Enhancer();
      e.setSuperclass(bean.getClass());
      e.setCallback(new LoggedCglibProxy(bean));
      return (T)e.create();
   }

   @Override
   public Object intercept(Object proxy, Method method, Object[] args, MethodProxy fastMethod) throws Throwable {
      return new CglibEntryExitMethodLogger(null, mapper, null, true, bean, method, args, proxy, fastMethod).logEntryExit();
   }

}
