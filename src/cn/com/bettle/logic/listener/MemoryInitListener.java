package cn.com.bettle.logic.listener;

import java.util.LinkedHashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.bettle.code.annotation.BettleContextListener;
import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.appcontext.context.BettleApplicationContext.BettleContextEvent;
import cn.com.bettle.code.appcontext.context.BettleApplicationListener;
import cn.com.bettle.code.exception.HandleException;
import cn.com.bettle.logic.service.ISchoolService;
import cn.com.bettle.logic.service.impl.SchoolService;
import cn.com.bettle.net.controller.ApiController;


@BettleContextListener
public class MemoryInitListener  implements  BettleApplicationListener {

	protected final Logger log = LoggerFactory.getLogger(MemoryInitListener.class);

	@Override
	public void handle(BettleApplicationContext bettleApplicationContext,Properties properties) throws HandleException {
	  try { 
    		  ISchoolService schoolService = (ISchoolService)bettleApplicationContext.getBean("SCHOOL_SERVICE");
    		  schoolService.initSchoolMemory();
    		  LinkedHashMap params = new LinkedHashMap();
    		  schoolService.startService(params);
    		  
		} catch (Exception e) {
			log.error("初始化内存数据库失败", e);
			throw new HandleException(e);
		}
	}

	@Override
	public void handleEvent(BettleContextEvent event) {
		// TODO Auto-generated method stub
		
	}

 }