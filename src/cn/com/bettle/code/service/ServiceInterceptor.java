package cn.com.bettle.code.service;

import java.util.Properties;  

import cn.com.bettle.code.exception.ServicePluginException;




/**
 * <B> 服务拦截器接口 </B>
 * <code>
 * <br>
 * <B><I>Example::</I></B>
 * 
 *  @ServiceIntercepts( {  
		        @ServiceSignature(method = "query", type = MyService.class, args = { Object.class }),  
		        @ServiceSignature(method = "prepare", type = MyService.class, args = { Object.class }) })  
		 public class MyInterceptor implements AServiceInterceptor {  
		    
		     public Object intercept(ServiceInvocation invocation) throws Throwable {  
		        Object result = invocation.proceed();  
		        System.out.println("Invocation.proceed()");  
		        return result;  
		     }   
		 } 
 * </code>
 * */

public abstract class ServiceInterceptor  {

	protected Properties interceptorProperties;
	
	
	 /**
	  * <B> 具体的拦截方法 </B>
	  * @param  <I>ServiceInvocation  服务的拦截器动态代理<I>
	  * @return <I> Object 返回运行结果<I>
	  * 
	  * */
	public abstract  Object intercept(ServiceInvocation invocation) throws Throwable;  
	   
	  
	  
		 /**
		  * <b> 返回服务的动态代理 </b>
		  * @param BService  带组装的服务
		  * @return BService 服务的拦截器动态代理
		  * */
	  
	  <T> T plugin(T target) throws ServicePluginException {
		  return ServicePlugin.wrap(target, this);  
	  }  
	   
	  public void setProperties(Properties properties){
		  interceptorProperties = properties;
	  } 
}
