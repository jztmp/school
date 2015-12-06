package cn.com.bettle.code.database.nosql.mongodb.bugu;

	import com.bugull.mongo.BuguConnection;  
	import javax.servlet.ServletContextEvent;  
	import javax.servlet.ServletContextListener;  
	  
	public class BuguInitListener implements ServletContextListener{  
	    @Override  
	    public void contextInitialized(ServletContextEvent event) {  
	        //连接数据库  由spring进行管理

	    }  
	   
	    @Override  
	    public void contextDestroyed(ServletContextEvent event) {
	        //关闭数据库连接
	        BuguConnection.getInstance().close();  
	    } 
	}  

