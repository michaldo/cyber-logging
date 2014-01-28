package com.cybercom.logging.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.cybercom.logging.core.DebugObjectMapper;

public class LoggedProxy<T> implements InvocationHandler {

   private DebugObjectMapper mapper = new DebugObjectMapper();

   private T bean;

   private LoggedProxy(T bean) {
      this.bean = bean;
   }

   @SuppressWarnings("unchecked")
   public static <T> T createProxy(T bean, Class<T> tClass) {
      return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[] {tClass}, new LoggedProxy<T>(bean));
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return new ProxyEntryExitMethodLogger(null, mapper, null, true, bean, method, args).logEntryExit();
   }

}
