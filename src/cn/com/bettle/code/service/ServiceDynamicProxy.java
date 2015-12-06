package cn.com.bettle.code.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;

public class ServiceDynamicProxy<T extends IService> implements InvocationHandler {

	protected final Logger log = LoggerFactory.getLogger(ServiceDynamicProxy.class);

	// 被代理类的实例
	T obj = null;

	// 将被代理者的实例传进动态代理类的构造函数中
	public ServiceDynamicProxy(T obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object result = null;
		if (method.getName().equals("execute")) {


			Object inParameters =  args[1];

			BServiceData serviceData = (BServiceData) args[2];

			String seriveName = this.obj.getServiceName();


			serviceData.setInParameters(seriveName, inParameters);
			if(log.isDebugEnabled())
				log.debug("--------IN-----" + JsonSerializeUtil.bean2Json(serviceData));
			/*
			 * before ：doSomething();
			 */

			result = method.invoke(this.obj, args);

			/*
			 * after : doSomething();
			 */
			serviceData.setOutParameters(seriveName, result);
			if(log.isDebugEnabled())
				log.debug("--------OUT-----" + JsonSerializeUtil.bean2Json(serviceData));

		} else {
			result = method.invoke(this.obj, args);
		}

		return result;
	}
}
