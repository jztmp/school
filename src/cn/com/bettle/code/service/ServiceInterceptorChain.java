package cn.com.bettle.code.service;

import java.util.ArrayList;
import java.util.List;

import cn.com.bettle.code.exception.ServicePluginException;

public class ServiceInterceptorChain {

  private final List<ServiceInterceptor> interceptors = new ArrayList<ServiceInterceptor>();

  
	 /**
	  * @category 组装名为serviceId的服务的所有拦截器，返回服务的动态代理
	  * @param BService target 带组装的服务
	  * @return BService 服务的拦截器动态代理
	  * */
  public <T> T pluginAll(T target) throws ServicePluginException {
	  
    for (ServiceInterceptor interceptor : interceptors) {
      target = interceptor.plugin(target);
    }
    return target;
  }

  public void addInterceptor(ServiceInterceptor interceptor) {
    interceptors.add(interceptor);
  }

}

