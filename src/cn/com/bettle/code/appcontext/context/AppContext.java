package cn.com.bettle.code.appcontext.context;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.bettle.code.exception.ConfigParserException;


/** 
 * @author		John
 * @version		0.1
 */
public class AppContext implements Parser
{
	private   ApplicationContext context = null;
	
	//private  ConfigurableWebApplicationContext  webContext = null;
	
	private static AppContext appContext = null;

	
	private AppContext(){
		this.context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");   //类路径
	}
	
	private AppContext(String contextPath){  //类路径
		this.context = new FileSystemXmlApplicationContext(contextPath);//文件路径
	}
	
	private AppContext(ApplicationContext _context){
		this.context = _context;
	}
	
	private AppContext(ServletConfig config,String XMLPath)throws BeansException,IOException{
		if(XMLPath!=null){
			ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
			XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)wac.getBeanFactory());   
			reader.setResourceLoader(wac);   
			reader.setEntityResolver(new ResourceEntityResolver(wac));   
	            String[] configLocations = new String[]{XMLPath};   
	            for(int i=0;i<configLocations.length;i++){   
	            	reader.loadBeanDefinitions(wac.getResources(configLocations[i]));
	            } 
	            this.context = wac;//
		}else{
			this.context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		}
	}
	
	public static AppContext getWebInstance(ServletConfig servletConfig,String configFilePath,boolean refurbish) throws BeansException, IOException{
		if(appContext==null) {
			appContext = new AppContext(servletConfig,configFilePath);
		}else if(refurbish){
			
		}
		return appContext;
	}
	
	public static AppContext getInstance(String configFilePath) throws BeansException, IOException{
		if(appContext==null) {
			appContext = new AppContext(configFilePath);
		}
		return appContext;
	}
	
	public static AppContext getInstance(ApplicationContext context) throws BeansException, IOException{
		if(appContext==null) {
				appContext = new AppContext(context);
		}
		return appContext;
	}
	
	public static AppContext getInstance() throws BeansException, IOException{
		if(appContext==null) {
			appContext = new AppContext();
		}
		return appContext;
	}
	

	

	
	public static void refurbishInstance(){
		if(appContext!=null && appContext.context!=null){
			 ApplicationContext pcontext = appContext.context.getParent();
			 if (pcontext !=null && pcontext instanceof AbstractRefreshableApplicationContext) { 
				 ((AbstractRefreshableApplicationContext) pcontext).refresh(); 
			 } 
			 if(appContext.context instanceof AbstractRefreshableApplicationContext){
				 ((AbstractRefreshableApplicationContext) appContext.context).refresh();
			 }else{
				 if (pcontext !=null && pcontext instanceof ConfigurableWebApplicationContext) { 
					 ((ConfigurableWebApplicationContext) pcontext).refresh(); 
				 } 
				 if(appContext.context instanceof ConfigurableWebApplicationContext){
					 ((ConfigurableWebApplicationContext) appContext.context).refresh();
				 }
			 }
		}
		
	}
	

	
	public Object getBean(String name){
		if(this.context!=null){
			return this.context.getBean(name);
		}
		return null;
	}
	
	public ApplicationContext getApplicationContext(){
		return this.context;
	}
	

	@Override
	public  void parser(String namespace,String filePath)throws ConfigParserException {
		try {
			refurbishInstance();
		} catch (Exception e) {
			throw new ConfigParserException(e.getMessage(),e.getCause());
		}
	}




	
	
}
