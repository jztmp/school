package cn.com.bettle.logic.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.appcontext.factory.ServiceFactory;
import cn.com.bettle.net.controller.ApiController;
import cn.com.bettle.logic.service.ISchoolService;

/**
 * spring 容器关闭
 * */
public class ContextStoppedEventProcessor  implements  ApplicationListener<ContextClosedEvent> {

	protected final Logger log = LoggerFactory.getLogger(ContextStoppedEventProcessor.class);
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

      //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
      if(event.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
    	  try { 
    		  ApplicationContext applicationContext = event.getApplicationContext();
    		  BettleApplicationContext bettleApplicationContext = (BettleApplicationContext)applicationContext.getBean("BettleContext");
			  ISchoolService schoolService = ServiceFactory.getService("SCHOOL_SERVICE",
					  bettleApplicationContext, false);
			  schoolService.stopService(null);

			} catch (Exception e) {
				log.error("运行时spring容器关闭失败", e);
			}
    	  
       }
 }


}