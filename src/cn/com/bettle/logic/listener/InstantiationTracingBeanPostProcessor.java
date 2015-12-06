package cn.com.bettle.logic.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.logic.service.ISchoolService;
import cn.com.bettle.logic.service.impl.SchoolService;
import cn.com.bettle.net.controller.ApiController;

/**
 * spring初始化后的事件监听
 * */
public class InstantiationTracingBeanPostProcessor  implements  ApplicationListener<ContextRefreshedEvent> {

	protected final Logger log = LoggerFactory.getLogger(InstantiationTracingBeanPostProcessor.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

      //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
      if(event.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
    	  try { 
	    		  ApplicationContext applicationContext = event.getApplicationContext();
	    		  BettleApplicationContext bettleApplicationContext = (BettleApplicationContext)applicationContext.getBean("BettleContext");
			} catch (Exception e) {
				log.error("初始化内存数据库失败", e);
			}
    	  
       }
 }

}