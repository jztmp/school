package cn.com.bettle.code.appcontext.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.springframework.context.ApplicationContext;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.exception.ServicePluginException;
import cn.com.bettle.code.service.IService;
import cn.com.bettle.code.service.ServiceDynamicProxy;
import cn.com.bettle.code.service.ServiceInterceptor;
import cn.com.bettle.code.service.ServiceInterceptorChain;


/**
 * @author  <b>Name:</b>Jason  <br> <b>Mail:</b>zxmapple@gmail.com  <br>  <b>Mobile :</b>13770763836  <br>  <b>QQ :</b>343816974
 * @category bettle服务管理工具类，通过它获取具体的服务
 * */

public class ServiceFactory {
	
	
	private static  Hashtable<String, Object> serviceMap = new Hashtable<String, Object>();

	private static BettleApplicationContext bettleApplicationContext;
	
	/**
	 * <b>功能 ：</b> 获取具体的服务
	 * @param serviceName 服务ID Spirng的@service注解对应1的名称
	 * @param context BettleApplicationContext bettle应用上下文
	 * @param newInstance 是否需要重新生成服务 ,否则调用已经在ServiceFactory缓存的服务
	 * */
	public static <T extends IService> T getService(String serviceName, final BettleApplicationContext context,boolean newInstance)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			ServicePluginException {
		
		if(!newInstance && serviceMap.containsKey(serviceName)){
			return (T)serviceMap.get(serviceName);
		}
		
		
		bettleApplicationContext = context;
		
		
		
		T bs = (T) context.getBean(serviceName);
		bs.setBettleApplicationContext(context);
		T plugin = bs;

		Vector<Map<String, Properties>> list = bettleApplicationContext.getServiceInterceptor(serviceName);
		ServiceInterceptorChain serviceInterceptorChain = new ServiceInterceptorChain();
		if (list != null) {
			for (Map<String, Properties> map : list) {
				for (String key : map.keySet()) {
					Properties value = map.get(key);
					ServiceInterceptor serviceInterceptor = (ServiceInterceptor) Class.forName(key)
							.newInstance();
					serviceInterceptor.setProperties(value);
					serviceInterceptorChain.addInterceptor(serviceInterceptor);
				}

			}

			plugin = (T) serviceInterceptorChain.pluginAll(bs);
		}

		ClassLoader loader = plugin.getClass().getClassLoader();

		// 获得被代理类已实现的所有接口interface,使得动态代理类的实例
		Class<?>[] interfaces = plugin.getClass().getInterfaces();

		// 用被代理类的实例创建动态代理类的实例，用于真正调用处理程序
		InvocationHandler handler = new ServiceDynamicProxy(plugin);

		/*
		 * loader : 被代理类的类加载器 interfaces ：被代理类已实现的所有接口，而这些是动态代理类要实现的接口列表 handler
		 * ： 用被代理类的实例创建动态代理类的实例，用于真正调用处理程序
		 * 
		 * return ：返回实现了被代理类所实现的所有接口的Object对象，即动态代理，需要强制转型
		 */
		// 获得代理的实例
		T proxy = (T) Proxy.newProxyInstance(loader, interfaces, handler);
		
		serviceMap.put(serviceName, proxy);

		return proxy;
	}

	
	
	
	public static <T extends IService> T getService(String serviceName,boolean newInstance)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			ServicePluginException {
		
		if(!newInstance && serviceMap.containsKey(serviceName)){
			return (T)serviceMap.get(serviceName);
		}
		
		
		T bs = (T) bettleApplicationContext.getBean(serviceName);
		bs.setBettleApplicationContext(bettleApplicationContext);
		T plugin = bs;
		Vector<Map<String, Properties>> list = BettleApplicationContext.getServiceInterceptor(serviceName);
		ServiceInterceptorChain serviceInterceptorChain = new ServiceInterceptorChain();
		if (list != null) {
			for (Map<String, Properties> map : list) {
				for (String key : map.keySet()) {
					Properties value = map.get(key);
					ServiceInterceptor serviceInterceptor = (ServiceInterceptor) Class.forName(key)
							.newInstance();
					serviceInterceptor.setProperties(value);
					serviceInterceptorChain.addInterceptor(serviceInterceptor);
				}

			}

			plugin = (T) serviceInterceptorChain.pluginAll(bs);
		}

		ClassLoader loader = plugin.getClass().getClassLoader();

		// 获得被代理类已实现的所有接口interface,使得动态代理类的实例
		Class<?>[] interfaces = plugin.getClass().getInterfaces();

		// 用被代理类的实例创建动态代理类的实例，用于真正调用处理程序
		InvocationHandler handler = new ServiceDynamicProxy(plugin);

		/*
		 * loader : 被代理类的类加载器 interfaces ：被代理类已实现的所有接口，而这些是动态代理类要实现的接口列表 handler
		 * ： 用被代理类的实例创建动态代理类的实例，用于真正调用处理程序
		 * 
		 * return ：返回实现了被代理类所实现的所有接口的Object对象，即动态代理，需要强制转型
		 */
		// 获得代理的实例
		T proxy = (T) Proxy.newProxyInstance(loader, interfaces, handler);
		
		serviceMap.put(serviceName, proxy);

		return proxy;
	}




	public BettleApplicationContext getBettleApplicationContext() {
		return bettleApplicationContext;
	}

	public void setBettleApplicationContext(BettleApplicationContext bettleApplicationContext) {
		this.bettleApplicationContext = bettleApplicationContext;
	}
	
	
	
	
}
