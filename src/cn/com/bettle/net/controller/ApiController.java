package cn.com.bettle.net.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.appcontext.factory.ServiceFactory;
import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.service.IService;
import cn.com.bettle.code.utils.json.JsonGsonSerializeUtil;
import cn.com.bettle.code.utils.json.JsonLibSerializeUtil;
import cn.com.bettle.code.utils.tools.BasicUitl;
import cn.com.bettle.logic.entity.GPRMC;
import cn.com.bettle.net.constant.SystemConstant;
import cn.com.bettle.net.entity.Device;
import cn.com.bettle.net.entity.User;
import cn.com.bettle.net.entity.vo.ExtListVo;
import cn.com.bettle.net.exception.CreateServiceException;
import cn.com.bettle.net.exception.InterceptorNotFountDefinitionException;
import cn.com.bettle.net.exception.ServiceNameNotFoundException;
import cn.com.bettle.net.exception.ServiceNotFountDefinitionException;
import cn.com.bettle.net.utils.BeanUtils;

@Controller("api")
@RequestMapping("/api")
public class ApiController implements ApplicationContextAware {

	protected final Logger log = LoggerFactory.getLogger(ApiController.class);

	private static final String SERVICENAME_PARAM = "SERVICENAME_PARAM";
	private static final String ACTIV_PARAM = "ACTIV_PARAM";
	private static final String PARAMES_PARAM = "PARAMES_PARAM";
	
    
	private ApplicationContext applicationContext;
	
	private  BettleApplicationContext bettleApplicationContext;






	/**
	 * 执行相应的服务例如：/execute?service=userService&activ=login&parames={username:admin,pasword:pasword}
	 * /execute?service=SQL_SERVICE&activ=sqlMap&parames={sqlId:"",activ:"",daoName:"",data:{}/[]}
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/execute")
	public String execute(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {

		String serviceName = BasicUitl.transformNull(request.getParameter(SERVICENAME_PARAM),"");
		String activ = BasicUitl.transformNull(request.getParameter(ACTIV_PARAM),"");
		String parames = BasicUitl.transformNull(request.getParameter(PARAMES_PARAM),"");
		if (StringUtils.isEmpty(serviceName)) {
			throw new ServiceNameNotFoundException(SERVICENAME_PARAM + "为空，请传入服务名");
		}

		return this.execute(serviceName,activ,parames,request, response, modelMap);
	}

	/**
	 * 执行相应的服务，服务名写在请求路径中：/login?username=admin&pasword=pasword
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	//
	@RequestMapping(value = "/{serviceName}/{method}/{parames}")
	public String execute(@PathVariable(value = "serviceName") String serviceName,@PathVariable(value = "method") String method,@PathVariable(value = "parames") String parames, HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) throws Exception {


		Map<String, Object> parames2 = JsonGsonSerializeUtil.json2Map(parames);
		parames2.put("request",request);
		parames2.put("response",response);
		try {
				this.bettleApplicationContext = (BettleApplicationContext)this.applicationContext.getBean("BettleContext");
				BettleApplicationContext.bettleApplicationContext = this.bettleApplicationContext;
				IService service = ServiceFactory.getService(serviceName, this.bettleApplicationContext,false);
				service.initSqlMapDAO();
		
			
				Object _obj = invokeMethod(service,method,new Object[]{parames2});
	
				if(_obj instanceof PageInfo){
					PageInfo pageInfo = (PageInfo)_obj;
					modelMap.addAttribute("total", pageInfo.getTotal());
					modelMap.addAttribute("items", pageInfo.getRows());
				}else{
					// 返回执行结果
					modelMap.addAttribute("result", _obj);
					// 返回请求时的参数
					modelMap.addAttribute("parames", parames);
					// 标记成功
					modelMap.addAttribute("success", true);
				}
				if (log.isDebugEnabled()) {
					log.debug(serviceName + "：" + JSONObject.fromObject(modelMap).toString());
				}
		} catch (Exception e) {
			modelMap.addAttribute("success", false);
			String message = "调用服务出错：" + e.getMessage() + " parames::"+parames;
			modelMap.addAttribute("message", message);
			log.error(message, e);
			throw e;
		}

		return "data";
	}
	
	
	public String execute( String serviceName, String method, Map parames, HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) throws Exception {


		Map<String, Object> parames2 = new HashMap();
		parames2.put("request",request);
		parames2.put("response",response);
		parames2.putAll(parames);
		try {
			
				this.bettleApplicationContext = (BettleApplicationContext)this.applicationContext.getBean("BettleContext");
				BettleApplicationContext.bettleApplicationContext = this.bettleApplicationContext;
				IService service = ServiceFactory.getService(serviceName, this.bettleApplicationContext,false);
				service.initSqlMapDAO();
			
				Object _obj = invokeMethod(service,method,new Object[]{parames2});
	
				if(_obj instanceof PageInfo){
					PageInfo pageInfo = (PageInfo)_obj;
					modelMap.addAttribute("total", pageInfo.getTotal());
					modelMap.addAttribute("items", pageInfo.getRows());
				}else{
					// 返回执行结果
					modelMap.addAttribute("result", _obj);
					// 返回请求时的参数
					modelMap.addAttribute("parames", parames);
					// 标记成功
					modelMap.addAttribute("success", true);
				}
				if (log.isDebugEnabled()) {
					log.debug(serviceName + "：" + JSONObject.fromObject(modelMap).toString());
				}
		} catch (Exception e) {
			modelMap.addAttribute("success", false);
			String message = "调用服务出错：" + e.getMessage();
			modelMap.addAttribute("message", message);
			log.error(message, e);
			throw e;
		}

		return "data";
	}


	//http://localhost:8008/mapguide/api/getGPS?imei=353197040912641&rmc=$GPRMC,211155.000,A,3441.8446,N,08635.0899,W,0.58,73.14,210714,,*23,@SMS,007,042928,072214
	//imei=353197040912641&rmc=$GPRMC,211155.000,A,3441.8446,N,08635.0899,W,0.58,73.14,210714,,*23,@SMS,0007,042928,072214
	@RequestMapping(value = "/getGPS")
	public String getGPS(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {

		 String imei  = BasicUitl.nullToEmpty(request.getParameter("imei"));
		 String rmc  = BasicUitl.nullToEmpty(request.getParameter("rmc"));
		 log.info("====="+imei+rmc);
		 Map data = paseGps(rmc);
		 data.put("ORG_STRING", rmc);
		 data.put("IMEI", imei);
		 /*		
		 Map parames = new HashMap();
		 parames.put("sqlId", "school.saveGPS");
		 parames.put("sqlActive", "insert");
		 parames.put("daoName", "MySqlMap");
		 parames.put("data", data);
		 String parames2 = JsonGsonSerializeUtil.bean2Json(parames);
		 */
		 return this.execute("SCHOOL_SERVICE","saveGPS",data,request, response, modelMap);
	}
	
	

	@Override
	public void setApplicationContext( ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected void showParams(ServletRequest request) {
		System.out.println("------------------------------");
		@SuppressWarnings("unchecked")
		Map<String, String[]> tmp = request.getParameterMap();
		if (tmp != null) {
			for (String key : tmp.keySet()) {
				String[] values = tmp.get(key);
				System.out.println(key + ":" + (values.length == 1 ? values[0].trim() : values));
			}
		}
		System.out.println("------------------------------");
	}
	
	
	
	/**反射方法
	  * @param methodObject  方法所在的对象
	  * @param methodName    方法名
	  * @param args          方法名参数数组
	*/
	public Object invokeMethod(Object methodObject, String methodName, Object[] args)
	   throws Exception {
	  Class ownerClass = methodObject.getClass();
	  Class[] argsClass = new Class[args.length];
	  for (int i = 0, j = args.length; i < j; i++) {
	   argsClass[i] = args[i].getClass();
	  }
	  Method method = ownerClass.getMethod(methodName, argsClass);
	  return method.invoke(methodObject, args);
	 }
	
	
	 public static Map paseGps(String str) throws ParseException, IntrospectionException, IllegalAccessException, InvocationTargetException{
	    	Map a = null;
			final int size = 16;
			String[] st = str.split(",");
			GPRMC gprmc = new GPRMC();
	        int i = gprmc.parseGPRMC(str);
	        if(i==0){
	        	a = JsonLibSerializeUtil.convertBeanNoNull(gprmc);
	        }
			return a;
		}
	 
	 
	 
		@RequestMapping(value = "/Map")
		public String mapDo(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
			 String ativ  = BasicUitl.nullToEmpty(request.getParameter(ACTIV_PARAM));
			 String data  = BasicUitl.nullToEmpty(request.getParameter(PARAMES_PARAM));
			 return this.execute("MAP_SERVICE",ativ,data,request, response, modelMap);
		}

		
		@RequestMapping(value = "/sms")
		public String sms(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
			
			 String message  = BasicUitl.nullToEmpty(request.getParameter("message"));
			 String tel  = BasicUitl.nullToEmpty(request.getParameter("tel"));
			 System.out.println("-----------------"+message);
			 log.debug("====="+message);
			 Map data = new HashMap();
			 data.put("MESSAGE", message);
			 data.put("TEL", tel);
			 return this.execute("SCHOOL_SERVICE","sendMessage",data,request, response, modelMap);
		}
		
		@RequestMapping(value = "/clearSms")
		public String clearSms(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
			
			 String message  = BasicUitl.nullToEmpty(request.getParameter("message"));
			 System.out.println("-----------------"+message);
			 log.debug("====="+message);
			 Map data = new HashMap();
			 data.put("MESSAGE", message);
			 return this.execute("SCHOOL_SERVICE","clearSms",data,request, response, modelMap);
		}
}
