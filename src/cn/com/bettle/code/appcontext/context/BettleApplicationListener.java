package cn.com.bettle.code.appcontext.context;

import java.util.Properties;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext.BettleContextEvent;
import cn.com.bettle.code.exception.HandleException;

public interface BettleApplicationListener {
	public void handle(BettleApplicationContext bettleApplicationContext,Properties properties)throws HandleException;
	public void handleEvent(BettleContextEvent event); 
}
