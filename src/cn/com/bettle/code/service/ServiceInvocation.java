package cn.com.bettle.code.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceInvocation {
	
	  private Object target;
	  private Method method;
	  private Object[] args;

	  public ServiceInvocation(Object target, Method method, Object[] args) {
	    this.target = target;
	    this.method = method;
	    this.args = args;
	  }

	  public Object getTarget() {
	    return target;
	  }

	  public Method getMethod() {
	    return method;
	  }

	  public Object[] getArgs() {
	    return args;
	  }

	  public Object proceed() throws InvocationTargetException, IllegalAccessException {
	    return method.invoke(target, args);
	  }
}
