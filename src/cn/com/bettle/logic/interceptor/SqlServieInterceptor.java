package cn.com.bettle.logic.interceptor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bettle.code.annotation.ServiceIntercepts;
import cn.com.bettle.code.annotation.ServiceSignature;
import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.service.ISqlService;
import cn.com.bettle.code.service.ServiceInterceptor;
import cn.com.bettle.code.service.ServiceInvocation;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;
import cn.com.bettle.code.utils.tools.BasicUitl;
import cn.com.bettle.net.controller.ApiController;


	@ServiceIntercepts({  
        @ServiceSignature(method = "sqlMap", type = ISqlService.class, args = {LinkedHashMap.class}),  
    })  
	
 public class SqlServieInterceptor extends ServiceInterceptor {  
    
	 protected final Logger log = LoggerFactory.getLogger(SqlServieInterceptor.class);
		
     public Object intercept(ServiceInvocation invocation) throws Throwable {  
    	 
    	String  sqlId = this.interceptorProperties.getProperty("sqlId");
    	String  sqlActive = this.interceptorProperties.getProperty("sqlActive");
    	String  daoName = this.interceptorProperties.getProperty("daoName");
    	
    	Object[] args = invocation.getArgs();
    	if(args.length>0){
    		LinkedHashMap map =  (LinkedHashMap)args[0];
    		String  sqlId2 =BasicUitl.transformNull(map.get("sqlId"),"");
    		String  sqlActive2 = BasicUitl.transformNull(map.get("sqlActive"),"");
    		String  daoName2 =BasicUitl.transformNull( map.get("daoName"),"");
    		Object  data = map.get("data");
    		if(sqlActive.equals(sqlActive2) && sqlId.equals(sqlId2)){
        		log.info("--------------args-----------------------"+JsonSerializeUtil.bean2Json(args));
        		log.info("--------------data-----------------------"+JsonSerializeUtil.bean2Json(data));
        	}
        	if(sqlActive.equals("loadOne")){
        		log.info("======================================");
        	}
    	}
    	
    	
    	/*--------befor-------*/
        Object result = invocation.proceed();  
        /*------end-----------*/
        
        return result;  
     }   
 } 

