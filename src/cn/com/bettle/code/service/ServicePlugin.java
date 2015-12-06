package cn.com.bettle.code.service;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.reflection.ExceptionUtil;

import cn.com.bettle.code.annotation.*;
import cn.com.bettle.code.exception.ServicePluginException;

public class ServicePlugin implements InvocationHandler {

  private Object _target;
  private ServiceInterceptor _serviceInterceptor;
  private Map<Class<?>, Set<Method>> _signatureMap;

  private ServicePlugin(Object target, ServiceInterceptor serviceInterceptor, Map<Class<?>, Set<Method>> signatureMap) {
    this._target = target;
    this._serviceInterceptor = serviceInterceptor;
    this._signatureMap = signatureMap;
  }

  public static <T> T wrap(T target, ServiceInterceptor serviceInterceptor) throws ServicePluginException {
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(serviceInterceptor);
    Class<?> type = target.getClass();
    Set<Class<?>> interfaces2 = getAllInterfaces2(type, signatureMap);
    Class<?>[] interfaces = interfaces2.toArray(new Class<?>[interfaces2.size()]);
    if (interfaces.length > 0) {
      return (T)Proxy.newProxyInstance(
          type.getClassLoader(),
          interfaces,
          new ServicePlugin(target, serviceInterceptor, signatureMap));
    }
    return target;
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      boolean up = true;
      Class<?> clazz = method.getDeclaringClass();
      Set<Method> methods = _signatureMap.get(clazz);
      if (methods != null && methods.contains(method)) {
        return _serviceInterceptor.intercept(new ServiceInvocation(_target, method, args));
      }else{
    	  	Class<?> clazz2 = clazz.getSuperclass();
    	    while (clazz2 != null) {
    	    	methods = _signatureMap.get(clazz2);
    	    	if (methods != null && methods.contains(method)) {
    	            return _serviceInterceptor.intercept(new ServiceInvocation(_target, method, args));
    	          }
    	    	clazz2 = clazz2.getSuperclass();
    	      }
    	    up = false;
      }
      if(!up){
    	  for (Class<?> key : _signatureMap.keySet()) {
    		  if(clazz.isAssignableFrom(key)){
    			  methods = _signatureMap.get(key);
    			  if (methods != null && methods.contains(method)) {
      	            return _serviceInterceptor.intercept(new ServiceInvocation(_target, method, args));
      	          }
    		  }
    	  }
    	  
      }
      return method.invoke(_target, args);
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }

  private static Map<Class<?>, Set<Method>> getSignatureMap(ServiceInterceptor serviceInterceptor) throws ServicePluginException {
	ServiceSignature[] sigs = serviceInterceptor.getClass().getAnnotation(ServiceIntercepts.class).value();
    Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
    for (ServiceSignature sig : sigs) {
      Set<Method> methods = signatureMap.get(sig.type());
      if (methods == null) {
        methods = new HashSet<Method>();
        signatureMap.put(sig.type(), methods);
      }
      try {
        Method method = sig.type().getMethod(sig.method(), sig.args());
        methods.add(method);
      } catch (NoSuchMethodException e) {
        throw new ServicePluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
      }
    }
    return signatureMap;
  }

  private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
    Set<Class<?>> interfaces = new HashSet<Class<?>>();
    while (type != null) {
      for (Class<?> c : type.getInterfaces()) {
        if (signatureMap.containsKey(c)) {
          interfaces.add(c);
        }
      }
      type = type.getSuperclass();
    }
    return interfaces.toArray(new Class<?>[interfaces.size()]);
  }

  
  public  static Set<Class<?>> getAllInterfaces2(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
	  	Set<Class<?>> interfaces = new HashSet<Class<?>>();
	    if (type != null) {
	      for (Class<?> c : type.getInterfaces()) {
	    	  if (signatureMap.containsKey(c)) {
	              interfaces.add(c);
	            }
	          interfaces.addAll(getAllInterfaces2(c,signatureMap)); 
	      }
	    }
	    return interfaces;
	  }
  
  private static Class<?>[] getAllSuper(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
	    Set<Class<?>> interfaces = new HashSet<Class<?>>();
	    while (type != null) {
	    	Class<?> c = type.getSuperclass();
	        if (signatureMap.containsKey(c)) {
	          interfaces.add(c);
	        }
	        type = type.getSuperclass();
	    }
	    return interfaces.toArray(new Class<?>[interfaces.size()]);
	  }
}

