package cn.com.map.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import cn.com.bettle.code.utils.date.DateUtils;
import cn.com.bettle.code.utils.json.JsonGsonSerializeUtil;




/**
 * @author zenxuming
 *<p>此类是用来管理地图服务的静态资料的。</p>
 *<p>静态资料:既软件的使用者不能更改的一些数据库数据或配置文件的内容。</p>
 *<p>扩展Hashtable用此类来装载静态资料。</p>
 */
/**
 *工厂继承Hashtable（线程安全）
 */
public class MapManage  extends Hashtable{
	
		private static final Log log = LogFactory.getLog(MapManage.class);
	    /**
	     *版本号
	     */
	 	private static final long serialVersionUID = 20081204001L;
	 
	    /**
	     *唯一实列
	     */
	    private static MapManage instance = null;
	    
	    

	    
	    /**
	     *最后一次刷新时间
	     */
	    private String lastRefreshTime = "";
	    
	    
	    private String mapWebXml = "";
	    /**
	     *定时更新工厂的Timer（定时器）
	     */
	    private Timer timer = new Timer(true);

	    /**
	     *更新工厂的任务（必备线程）
	     */
	    private TimerTask task = null;
	    	    
	    /**
	     * 构造
	     * <br>
	     * @author      zengxuming          2008-12-04
	     * @throws JDOMException 
	     * @throws MapException 
	     * @throws IOException 
	     */
	    private MapManage() throws MapException, JDOMException, IOException{
	    	 super();
		     init();
		     // 启动心跳任务定时刷新
		     start();
		}
   
	    
	    private MapManage(String mapweb) throws MapException, JDOMException, IOException{
	    	    this.mapWebXml = mapweb;
	    	    init();
	    	    start();
	    }
	    /**
	     *获取实列
	     * <br>
	     * @author      zenxuming          2008-12-04
	     * @throws JDOMException 
	     * @throws MapException 
	     * @throws IOException 
	     */
	    public static synchronized MapManage getInstance () throws MapException, JDOMException, IOException{
	        if (instance == null)
	        { throw new MapException("no instance init");}
	    	return instance;
	    }
	    
	    public static synchronized MapManage getInstance(String mapweb) throws MapException, JDOMException, IOException {
	    	    if (instance == null)
	    	      instance = new MapManage(mapweb);
	    	    return instance;
	  }

	    /**
	     * 初始化
	     * @author     zenxuming          2008-12-04
	     * @throws JDOMException 
	     * @throws MapException 
	     * @throws IOException 
	     */
	    private  void init () throws MapException, JDOMException, IOException{
	        load();
	    }
	    

	    
	    
	    
		
	    /**
	     *解析xml配置文件(使用SAX解析)
	     * @param       InputStream  in      xml配置文件输入流 
	     * @author      zenxuming          2008-12-04
	     * @throws JDOMException 
	     * @throws IOException 
	     */
	    private void parseXML(InputStream in)
	    	    throws MapException, JDOMException, IOException
	    	  {
	    	    SAXBuilder dombuilder = new SAXBuilder();
	    	    Document rootDoc = dombuilder.build(in);

	    	    List mapList = rootDoc.getRootElement().getChildren("map");

	    	    for (int j = 0; j < mapList.size(); j++) {
	    	      Map mapInfor = new HashMap();
	    	      Element mapset = (Element)mapList.get(j);
	    	      Element project1 = mapset.getChild("project");
	    	      if (project1 == null) {
	    	        throw new MapException("'project' Contact does not exist");
	    	      }

	    	      mapInfor.put("project", project1.getTextTrim());

	    	      Element mapResourceIdentifier = mapset.getChild("mapResourceIdentifier");
	    	      if (mapResourceIdentifier == null) {
	    	        throw new MapException("'mapResourceIdentifier' Contact does not exist");
	    	      }

	    	      mapInfor.put("mapResourceIdentifier", mapResourceIdentifier.getTextTrim());

	    	      Element projectName = mapset.getChild("projectName");
	    	      if (projectName == null) {
	    	        throw new MapException("'projectName' Contact does not exist");
	    	      }

	    	      mapInfor.put("projectName", projectName.getTextTrim());

	    	      Element webLayout = mapset.getChild("webLayout");
	    	      if (webLayout == null) {
	    	        throw new MapException("'webLayout' Contact does not exist");
	    	      }

	    	      mapInfor.put("webLayout", webLayout.getTextTrim());

	    	      Element username = mapset.getChild("username");
	    	      if (username == null) {
	    	        throw new MapException("'username' Contact does not exist");
	    	      }

	    	      mapInfor.put("username", username.getTextTrim());

	    	      Element password = mapset.getChild("password");
	    	      if (password == null) {
	    	        throw new MapException("'password' Contact does not exist");
	    	      }

	    	      mapInfor.put("password", password.getTextTrim());

	    	      Element mapName = mapset.getChild("mapName");
	    	      if (mapName == null) {
	    	        throw new MapException("'mapName' Contact does not exist");
	    	      }

	    	      mapInfor.put("mapName", mapName.getTextTrim());

	    	      Element refreshtime = rootDoc.getRootElement().getChild("refreshtime");
	    	      if (refreshtime == null) {
	    	        throw new MapException("'refreshtime' Contact does not exist");
	    	      }

	    	      put("refreshtime", refreshtime.getTextTrim());

	    	      List layers = mapset.getChild("layers").getChildren("layer");
	    	      List nameList = new ArrayList();
	    	      Map lyaerInfor = new HashMap();
	    	      for (int i = 0; i < layers.size(); i++) {
	    	        Element element = (Element)layers.get(i);
	    	        String layerName = element.getAttributeValue("name");
	    	        Map map = new HashMap();
	    	        Element type = element.getChild("type");
	    	        map.put("type", type.getTextTrim());
	    	        Element className = element.getChild("className");
	    	        map.put("className", className.getTextTrim());
	    	        Element project = element.getChild("project");
	    	        map.put("project", project.getTextTrim());
	    	        Element permanentlayername = element.getChild("permanentlayername");
	    	        map.put("permanentlayername", permanentlayername.getTextTrim());
	    	        Element geometryName = element.getChild("geometryName");
	    	        map.put("geometryName", geometryName.getTextTrim());
	    	        Element temporary_layer_xml_file_path = element.getChild("temporary_layer_xml_file_path");
	    	        map.put("temporary_layer_xml_file_path", temporary_layer_xml_file_path.getTextTrim());
	    	        Element propertyObject = element.getChild("propertyObject");
	    	        String propertyObjectString = propertyObject.getTextTrim();
	    	        map.put("propertyObject", propertyObjectString);

	    	        Map propertyCollection = JsonGsonSerializeUtil.json2Map(propertyObjectString);
	    	        map.put("propertyCollection", propertyCollection);
	    	        Element temporaryLayers = element.getChild("temporaryLayers");
	    	        map.put("temporaryLayers", temporaryLayers.getTextTrim());
	    	        lyaerInfor.put(layerName, map);
	    	        nameList.add(layerName);
	    	      }
	    	      mapInfor.put("layers", lyaerInfor);
	    	      mapInfor.put("layerName", nameList);

	    	      put(mapName.getTextTrim(), mapInfor);
	    	    }
	    	  }
			
			
		public Map getMapInfor(String mapName){
			return (Map)this.get(mapName);
		}
		
		public String getMapRS(String mapName) {
		    return (String)((Map)get(mapName)).get("mapResourceIdentifier");
		  }
		
		public Map getLayerInfor(String mapName,String layerName){
			return (Map)((Map)this.getMapInfor(mapName).get("layers")).get(layerName);
		}
		
	    /**
	     *加载MapWeb.xml配置文件
	     * @author      zenxuming          2008-12-04
	     * @throws MapException 
	     * @throws JDOMException 
	     * @throws IOException 
	     */
		 private synchronized void load() throws MapException, JDOMException, IOException 
		{
			 
			File file = new File(mapWebXml);
			if(file==null){
				 throw new MapException("MapManage.load() -- Load configuration file "+ mapWebXml +" failed! File does not exist");
			}
			InputStream in = new FileInputStream(file);
			
			  if(!file.exists())
			  {
				  throw new MapException("MapManage.load() -- Load configuration file "+ mapWebXml +" failed! File does not exist");
			  }
			  if (! file.isFile())
			  {
	              throw new MapException("MapManage.load() -- Load configuration file "+ mapWebXml +" failed! File does not exist");
	          }
			  parseXML(in);
	          log.info("Configuration file successfully resolved");
		}
		    /**
		     *获得类实列
		     *@param Class 类名
		     * @return    Object
		     * @author      zenxuming          2008-02-01
		     * @throws IllegalAccessException 
		     * @throws InstantiationException 
		     */
		 public  Object getInstance(Class name) throws InstantiationException, IllegalAccessException {
			Class dao = (Class)super.get(name);
			return dao.newInstance(); 
		 }
		 
		    /**
		     *获得最后更新时间
		     * @return    lastRefreshTime
		     * @author      zenxuming          2008-02-01
		     */
		    public String getLastRefreshTime () {
		        return this.lastRefreshTime;
		    }
		    

		    /**
		     * 更新工厂  由于工厂是在服务器起动时就建造好的，所以当用户修改了工厂配置文件后如果不及时更新工厂那么用户修改的信息就得不到体现
		     * @author      zenxuming         2008-02-01
		     * @throws JDOMException 
		     * @throws MapException 
		     * @throws IOException 
		     */
		    private synchronized void refresh () throws MapException, JDOMException, IOException {
		            if (instance != null) {
		                this.load();
		                this.lastRefreshTime =DateUtils.getFormatNowDate("yyyy-MM-dd HH:mm:ss");
		                log.info("Static data refresh success！");
		            }
		    }
		    
		    
		    
		    /**
		     *启动工厂更新
		     * @author      zenxuming          2008-02-01
		     */
		    private void start () {
		        this.task = new TimerTask() {
		            public void run () {
		            	try
		            	{
		            	refresh();
		            	}
		            	catch(Exception e)
		            	{
		            		log.info("TimerTask.run()\r\n Start Static Data Refresh Failed!" + e);	
		            	}
		            }
		        };
		        this.timer.schedule(this.task,0L,Long.parseLong((String)get("refreshtime")) * 60 * 1000L);  
		    }

		    /**
		     * 取消工厂更新
		     * @author      zenxuming         2008-02-01
		     */
		    public synchronized void cancel (){
		            timer.cancel();
		            task = null;
		    }		 
}
