package cn.com.bettle.logic.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class JacksonContextListener extends ContextLoaderListener {
	
	private static List<Runnable> tasks = new ArrayList<Runnable>();
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		for (Runnable run : tasks){
			run.run();
		}
	}
	
	static void addDestoryTask(Runnable task){
		tasks.add(task);
	}

}
