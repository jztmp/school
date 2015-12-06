package cn.com.bettle.code.appcontext.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import cn.com.bettle.code.annotation.BettleContextListener;
import cn.com.bettle.code.appcontext.factory.DaoFactory;
import cn.com.bettle.code.database.entity.DaoEntity;
import cn.com.bettle.code.exception.BettleHandleAfterException;
import cn.com.bettle.code.exception.BettleHandleBeforException;
import cn.com.bettle.code.exception.ConfigLoadException;
import cn.com.bettle.code.exception.ConfigParserException;
import cn.com.bettle.code.exception.DaoParserException;
import cn.com.bettle.code.exception.HandleException;
import cn.com.bettle.code.exception.InterfaceException;
import cn.com.bettle.code.service.ServiceInterceptor;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;
import cn.com.bettle.code.utils.clazz.ClassUtils2;
import cn.com.bettle.code.utils.clazz.LoadPackageClasses;
import cn.com.bettle.code.utils.file.FileUtils;
import cn.com.bettle.code.utils.tools.BasicUitl;
import cn.com.bettle.net.controller.ApiController;



public class BettleApplicationContext extends SpringContextHolder {
	
	public static BettleApplicationContext bettleApplicationContext;
	
	private String bettleContextListenerPath;
	
	private Vector bettleContextListener = new Vector();//监听自己的监听器队列  

	
	private Logger logger = LoggerFactory.getLogger(BettleApplicationContext.class);
	
	private static Hashtable<String, Properties>  propertieContext = new Hashtable<String, Properties>();
	/* 封装配置文件的属性信息 
	 * 配置元数据
	 * */
	private static Hashtable<String, Map<String, String>> metadata = new  Hashtable<String, Map<String, String>>();
    
	/* 
	 * 封装 Dao实体
	 * */
	private static Hashtable<String, DaoEntity> dao = new Hashtable<String, DaoEntity>();
	
	
	/* 
	 * 封装 服务拦截器的配置信息
	 * 一个服务可以用多个连接器
	 * */
	private static Hashtable<String, Vector<Map<String, Properties>>> serviceInterceptor = new Hashtable<String, Vector<Map<String, Properties>>>();
	
	private static Hashtable<String, Map<String, Properties>> bettleListener = new Hashtable<String, Map<String, Properties>>();
	
	
	private static Hashtable<String,ISqlMapDAO>  sqlMapDAOSet = new Hashtable<String,ISqlMapDAO>();

	// -----------------------------------Propertie context------------------------------

	public static void addPropertieContext(String namespace, Properties propertie) {
		propertieContext.put(namespace, propertie);
	}

	public static void addPropertieContext(String namespace, String key, String value) {
		propertieContext.get(namespace).put(key, value);
	}

	public static void removePropertieContext(String namespace) {
		propertieContext.remove(namespace);
	}

	public static void removePropertieContext(String namespace, String key) {
		propertieContext.get(namespace).remove(key);
	}

	public static String getPropertieContext(String namespace, String key) {
		return BasicUitl.nullToEmpty(propertieContext.get(namespace).get(key)) ;
	}

	public static Properties getPropertieContext(String namespace) {
		return  propertieContext.get(namespace);
	}
	// -----------------------------------metadata------------------------------

	public static void addMetadata(String namespace, Map propertie) {
		metadata.put(namespace, propertie);
	}

	public static void addMetadata(String namespace, String key, String value) {

		if(!metadata.containsKey(namespace)){
			metadata.put(namespace, new HashMap<String, String>());
		}
		metadata.get(namespace).put(key, value);
	}



	public static void removeMetadata(String namespace) {
		if (metadata.containsKey(namespace)) {
			metadata.remove(namespace);
		}
	}

	public static void removeMetadata(String namespace, String key) {
		if(metadata.containsKey(namespace) && metadata.get(namespace).containsKey(key))
			metadata.get(namespace).remove(key);
	}

	public static String getMetadata(String namespace, String key) {
		if(metadata.containsKey(namespace) && metadata.get(namespace).containsKey(key))
			return metadata.get(namespace).get(key);
		else
			return null;	
	}

	// -----------------------------------------database-----------------------------------------

	public static void addDao(String namespace, DaoEntity daoEntity) {
		dao.put(namespace, daoEntity);
	}

	public static void removeDao(String namespace) {
		if(dao.containsKey(namespace))
			dao.remove(namespace);
	}

	public static DaoEntity getDao(String namespace) {
		return dao.get(namespace);
	}
	
	private static Hashtable<String, DaoEntity> getDaos() {
		return dao;
	}
	
	public static Hashtable<String,ISqlMapDAO> getSqlDaoSet() throws BeansException, ConfigLoadException, DaoParserException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException{
		if(sqlMapDAOSet.isEmpty()){
			 Hashtable<String, DaoEntity> daoMap = getDaos();
			 Enumeration<DaoEntity> e1 = daoMap.elements();
			 while (e1.hasMoreElements()) {
				 DaoEntity de = e1.nextElement();
				 if(de.isSqlMap()){
					 String name = de.getName();
					 sqlMapDAOSet.put(name, DaoFactory.getDao(ISqlMapDAO.class,name,applicationContext,false));
				 }
			 }
		}
		return sqlMapDAOSet;
	}  

	// -----------------------------------------service-interceptor-----------------------------------------
	
	
	public static void addServiceInterceptor(String namespace, Map<String, Properties> propertie) {
		if(!serviceInterceptor.containsKey(namespace)){
			Vector vr = new Vector();
			serviceInterceptor.put(namespace, vr);
		}
		serviceInterceptor.get(namespace).add(propertie);
	}


	public static void removeServiceInterceptor(String namespace) {
		serviceInterceptor.remove(namespace);
	}
	
	public static Vector<Map<String, Properties>> getServiceInterceptor(String serviceId) {
		return serviceInterceptor.get(serviceId);
	}


	// -----------------------------------------bettle-listen-----------------------------------------
	
	
	public static void addBettleListen(String eventName, String listenerClass, Properties propertie) {
		if(!bettleListener.containsKey(eventName)){
			bettleListener.put(eventName, new HashMap<String, Properties>());
		}
		bettleListener.get(eventName).put(listenerClass, propertie);
	}


	public static void removeBettleListen(String eventName) {
		bettleListener.remove(eventName);
	}
	
	public static void removeBettleListenItem(String eventName,String listenerClass) {
		if(bettleListener.containsKey(eventName)){
			bettleListener.get(eventName).remove(listenerClass);
		}
	}
	
	public static Map<String, Properties> getBettleListen(String eventName) {
		return bettleListener.get(eventName);
	}
	
	

	public static BettleApplicationContext getInstance() throws BeansException,
			IOException, InterfaceException, ConfigParserException,
			BettleHandleAfterException, BettleHandleBeforException,
			ParserConfigurationException, SAXException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, HandleException {
		if (bettleApplicationContext == null) {
			bettleApplicationContext = (BettleApplicationContext) applicationContext
					.getBean("BettleContext");
			bettleApplicationContext.init();
		}
		return bettleApplicationContext;
	}	

	
	
	public void init()
			throws ParserConfigurationException, SAXException, IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InterfaceException, ConfigParserException, HandleException {
		if ( metadata.isEmpty()) {
			scanContextListener();
			
			handleBefor();
			notifyDemoEvent(BettleContextEvent.INIT_BEFOR);
			
			String fileName = FileUtils.getClassPathFilePath("bettle.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			NodeList rootList = document.getChildNodes();
			for (int i = 0; i < rootList.getLength(); i++) {
				NodeList configuration = rootList.item(i).getChildNodes();
				for (int k = 0; k < configuration.getLength(); k++) {
					Node configurationItem = configuration.item(k);
					logger.debug(configurationItem.getNodeName());
					if (configurationItem.getNodeName().equals("bettle")) {
						NodeList bettleItems = configurationItem
								.getChildNodes();
						// 循环bettle下的所有节点
						for (int j = 0; j < bettleItems.getLength(); j++) {
							Node node = bettleItems.item(j);
							logger.debug(node.getNodeName());
							if (node.getNodeName().equals("#text")) {
								continue;
							}
							// 获取节点的所有属性
							NamedNodeMap map = node.getAttributes();
							String namespace = "";
							// 加载配置文件的元数据
							Map<String, String> propertie = new HashMap();
							for (int h = 0; h < map.getLength(); h++) {
								String key = map.item(h).getNodeName();
								String value = map.item(h).getTextContent();
								if (key.equals("name")) {
									namespace = value;
								}
								propertie.put(key, value);
							}
							propertie.put("type", node.getNodeName());
							addMetadata(namespace,
									propertie);

							// 获取解析器，开始解析配置文件
							String parser = propertie.get("parser");
							
							String cfgPath =  propertie.get("filePath");
							String filePath = cfgPath;
							if(cfgPath.indexOf(":")>0){
								String cfgPath2 = cfgPath.split(":")[1];
								filePath = FileUtils.getClassPathFilePath(cfgPath2);
							}
							
							String autoReload = propertie.get("autoReload");
							String listener = propertie.get("listener");
							String interval = propertie.get("interval");
							//初始化解析器并执行
							if (parser != null && !StringUtils.isBlank(parser)) {
								Class clazz = Class.forName(parser);
								String xmlInterfaceName = "cn.com.bettle.code.appcontext.context.Parser";
								if (ClassUtils2.isInterface(clazz,
										xmlInterfaceName)) {
									Parser parser2;
									if (parser
											.equals("cn.com.bettle.code.appcontext.context.AppContext")) {
										parser2 = AppContext.getInstance(applicationContext);
									} else {
										parser2 = (Parser) clazz.newInstance();
										parser2.parser(namespace, filePath);
									}
									
								} else {
									throw new InterfaceException(parser
											+ "没有实现接口" + xmlInterfaceName);
								}
							}

							// 如果autoReload为true表示自动更新，则启动监视器监听配置文件的变换并同步相应的变化
							if (autoReload != null
									&& !StringUtils.isBlank(autoReload)
									&& autoReload.toLowerCase().trim()
											.equals("true")) {
								if (listener != null
										&& !StringUtils.isBlank(listener)) {
									Class clazz = Class.forName(listener);
									String xmlInterfaceName = "cn.com.bettle.code.appcontext.context.FileListener";
									if (ClassUtils2.isInterface(clazz,
											xmlInterfaceName)) {
										FileListener fileListener = (FileListener) clazz
												.newInstance();
										fileListener.setNameSpace(namespace);
										if (!StringUtils.isBlank(interval)) {
											fileListener.startListener(
													filePath,
													Long.parseLong(interval));
										} else {
											fileListener
													.startListener(filePath);
										}
									} else {
										throw new InterfaceException(listener
												+ "没有实现接口" + xmlInterfaceName);
									}
								}
							}

						}
					}

				}
			}
			
			handleAfter();
			notifyDemoEvent(BettleContextEvent.INIT_AFTER);
		}
	}

	public void destroy(){
		dao.clear();
		serviceInterceptor.clear();
		metadata.clear();
		propertieContext.clear();
	}
	
	
  public void scanContextListener() throws ClassNotFoundException, IOException, InterfaceException, InstantiationException, IllegalAccessException{
	  if(!StringUtils.isBlank(bettleContextListenerPath)){
		  String[] packageNames = bettleContextListenerPath.split(",");
		  LoadPackageClasses loadPackageClasses = new LoadPackageClasses(packageNames,BettleContextListener.class);
		  Set<Class<?>> classSet = loadPackageClasses.getClassSet();
		  for (Class cl : classSet) {  
			  if(ClassUtils2.isInterface(cl, "cn.com.bettle.code.appcontext.context.BettleApplicationListener")){
				  addDemoListener((BettleApplicationListener)cl.newInstance());
			  }else{
				  throw new InterfaceException(cl.getName()+"没有实现cn.com.bettle.code.appcontext.context.BettleApplicationListener接口方法"); 
			  }
		  }  
	  }
  }
	
   public void addDemoListener(BettleApplicationListener dl) {     
	   bettleContextListener.addElement(dl);     
   }     
   public void notifyDemoEvent(int _eventType) {//通知所有的监听器     
          Enumeration enums = bettleContextListener.elements();     
          while(enums.hasMoreElements()) {     
        	  BettleApplicationListener dl = (BettleApplicationListener)enums.nextElement();     
              dl.handleEvent(new BettleContextEvent(this,_eventType));     
          }     
   }
	
	
	private void handleBefor()throws InstantiationException, IllegalAccessException, ClassNotFoundException, HandleException{
		Map<String, Properties> map = getBettleListen("INIT_BEFOR");
		if(map!=null && !map.isEmpty()){
			for (String key : map.keySet()) {
				Class clazz = Class.forName(key);
				Properties properties = map.get(key);
				if (ClassUtils2.isInterface(clazz,"cn.com.bettle.code.appcontext.context.BettleApplicationListener")){
					BettleApplicationListener listener = (BettleApplicationListener) Class.forName(key).newInstance();
					listener.handle(this,properties);
				}else{
					throw new HandleException(key+"没有实现接口cn.com.bettle.code.appcontext.context.BettleApplicationListener");
				}
			}
		}
	}
	
	private void handleAfter() throws InstantiationException, IllegalAccessException, ClassNotFoundException, HandleException{
		Map<String, Properties> map = getBettleListen("INIT_AFTER");
		if(map!=null && !map.isEmpty()){
			for (String key : map.keySet()) {
				Class clazz = Class.forName(key);
				Properties properties = map.get(key);
				if (ClassUtils2.isInterface(clazz,"cn.com.bettle.code.appcontext.context.BettleApplicationListener")){
					BettleApplicationListener listener = (BettleApplicationListener) Class.forName(key).newInstance();
					listener.handle(this,properties);
				}else{
					throw new HandleException(key+"没有实现接口cn.com.bettle.code.appcontext.context.BettleApplicationListener");
				}
			}
		}
	}

	
	public class BettleContextEvent extends java.util.EventObject { 
		
		
		public static final int INIT_BEFOR = 0;
		public static final int INIT_AFTER = 1;
		//事件类型，默认为-1表示没有任何事件无效事件
		public int mEventType = -1; 
		
	    public BettleContextEvent(BettleApplicationContext source,int _eventType) {     
	      super(source);//source—事件源对象—如在界面上发生的点击按钮事件中的按钮     
	       //所有 Event 在构造时都引用了对象 "source"，在逻辑上认为该对象是最初发生有关 Event 的对象     
	      mEventType = _eventType;
	    }     
	       
	}


	public String getBettleContextListenerPath() {
		return bettleContextListenerPath;
	}

	public void setBettleContextListenerPath(String bettleContextListenerPath) {
		this.bettleContextListenerPath = bettleContextListenerPath;
	}  
	
	
}
