package cn.com.map.api;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import org.osgeo.mapguide.MapGuideJavaApi;
import org.osgeo.mapguide.MgCoordinate;
import org.osgeo.mapguide.MgCoordinateSystem;
import org.osgeo.mapguide.MgCoordinateSystemFactory;
import org.osgeo.mapguide.MgException;
import org.osgeo.mapguide.MgFeatureService;
import org.osgeo.mapguide.MgGeometryFactory;
import org.osgeo.mapguide.MgLayer;
import org.osgeo.mapguide.MgLayerCollection;
import org.osgeo.mapguide.MgMap;
import org.osgeo.mapguide.MgPoint;
import org.osgeo.mapguide.MgResourceService;
import org.osgeo.mapguide.MgServiceType;
import org.osgeo.mapguide.MgSite;
import org.osgeo.mapguide.MgSiteConnection;
import org.osgeo.mapguide.MgUserInformation;
import cn.com.map.init.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ZMap extends MgMap{   
	/**
     * 串行化标识，无实际意义
     */
    private static final long serialVersionUID = 1439142487437643119L;
        
    public String mapName = null; //地图名称
    
    public String sMapName = null;
    
    public Map<String,Object> mapInfor = null;
    
    public String project = null;
    
    private static final Log log = LogFactory.getLog(ZMap.class);

    public MgSiteConnection siteConnection=null;  //站点链接
    
    public MgResourceService resourceService=null; //WRS
    
    public MgFeatureService featureService=null; //WFS

    /**
     * 构造函数。(适用于站点初始化前的构造)
     * 
     * @author     John         2008-02-01
     */
    public ZMap(String _mapName)throws Exception{
    	   this.mapName = _mapName;
    	    MapManage mapManage = MapManage.getInstance();
    	    this.mapInfor = mapManage.getMapInfor(_mapName);
    	    this.project = ((String)this.mapInfor.get("project"));
	}

    /**
     * 构造函数。(适用于站点初始化后的构造)
     * 
     */ 
    public ZMap(HttpServletRequest request, HttpServletResponse response,String _mapName,String _sessionId)throws Exception{
		MapManage mapManage = MapManage.getInstance(); 
		this.mapName= _mapName;
        this.mapInfor = mapManage.getMapInfor(_mapName);
        this.project = (String)this.mapInfor.get("project");
        this.openSiteConnection(request,response,_sessionId);
    }

	    /**
	     * 站点运行初始化。
	     * @author      zxm         2008-02-01
	     */ 
	 public  String siteStart(HttpServletRequest request, HttpServletResponse response) throws Exception{
		    // ----------------------站点初始化----------------------------//
	         initializeWebTier(request,response);
	         String username = (String)this.mapInfor.get("username");
	         String password = (String)this.mapInfor.get("password");
	         MgUserInformation userInfo = new MgUserInformation(username,password);
	         MgSite site = new MgSite();
	         site.Open(userInfo);
	         String sessionId = site.CreateSession();
	         return sessionId;
	 }
	 
	    /**
	     * 初始化地图站点 (webconfig.ini文件必须放到与Map.class文件同目录下)。
	     * @author      Cloud|Wind          2008-06-06
	     */
		public  void initializeWebTier(HttpServletRequest request, HttpServletResponse response) throws Exception{					
			    // Initialize a JSP session and register a variable to hold the
			    // session id, then initialize the Web Extensions, and connect
			    // to the site, and create a session.
			    String realpath = request.getSession().getServletContext().getRealPath("/");
				// Initialize web tier with the site configuration file.  The config
				// file should be in the same folder as this script.
				String configPath =  realpath + "webconfig.ini";
			    MapGuideJavaApi.MgInitializeWebTier(configPath);
		}
	    /**
	     * 初始化地图站点 (webconfig.ini文件必须放到与Map.class文件同目录下)。
	     * @author      Cloud|Wind          2008-06-06
	     */
		public static void initializeWebTier(String webconfig_ini_path) throws Exception{	
				MapGuideJavaApi.MgInitializeWebTier(webconfig_ini_path);
		}	
		
		public  void openSiteConnection(HttpServletRequest request, HttpServletResponse response,String mgSessionId) throws Exception{	
				initializeWebTier(request,response);
		        MgUserInformation userInfo = new MgUserInformation(mgSessionId);
		        this.siteConnection = new MgSiteConnection();
		        this.siteConnection.Open(userInfo);
				this.resourceService = (MgResourceService)this.siteConnection.CreateService(MgServiceType.ResourceService);
				this.featureService = (MgFeatureService)this.siteConnection.CreateService(MgServiceType.FeatureService);
				//this.Open(this.resourceService, this.mapName);
				MapManage mapManage = MapManage.getInstance();
				String mapResourceIdentifier = mapManage.getMapRS(this.mapName);
				this.Open(this.resourceService, this.sMapName);
		}	
		

	

	    /**
	     *获取图层通过层名
	     * @param name 层名
	     * @throws MgException
	     */
	    public MgLayer getlayerByName(String name) throws MgException{
	    		MgLayerCollection layers = this.GetLayers();
	    		return (MgLayer)layers.GetItem(name);
	    }
	
		    	    

					public  HashMap convertToLonLat(MgPoint thePoint)throws Exception  {
						HashMap map = new HashMap();
						MgGeometryFactory geometryFactory = new MgGeometryFactory();
						String systemCoord = this.project;
				         MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				         MgCoordinateSystem coordinate = coordinateFactory.Create(systemCoord);
			             MgCoordinate coord = geometryFactory.CreateCoordinateXY(Double.valueOf(thePoint.GetCoordinate().GetX()), Double.valueOf(thePoint.GetCoordinate().GetY()));
			             MgCoordinate LonLat = coordinate.ConvertToLonLat (coord);
			             String lat = String.valueOf(LonLat.GetX());
			             map.put("lat", lat);
			             String lon = String.valueOf(LonLat.GetY());
			             map.put("lon", lon);
						return map;
					} 
					
					public  double convertMetersToCoordinateSystemUnits(double meters) throws MgException, UnsupportedEncodingException, FileNotFoundException, MapException, JDOMException {
						MgGeometryFactory geometryFactory = new MgGeometryFactory();
						String systemCoord = this.project;
				         MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				         MgCoordinateSystem coordinate = coordinateFactory.Create(systemCoord);
				         return coordinate.ConvertMetersToCoordinateSystemUnits(meters);
					} 
					
					public  double convertCoordinateSystemUnitsToMeters(double units) throws MgException, UnsupportedEncodingException, FileNotFoundException, MapException, JDOMException {
						MgGeometryFactory geometryFactory = new MgGeometryFactory();
						String systemCoord = this.project;
				         MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				         MgCoordinateSystem coordinate = coordinateFactory.Create(systemCoord);
				         return coordinate.ConvertCoordinateSystemUnitsToMeters(units);
					}
					
					public  HashMap convertToLonLat(String point) throws MgException, UnsupportedEncodingException, FileNotFoundException, MapException, JDOMException  {
						HashMap map = new  HashMap();
						MgPoint thePoint = ZLayer.makePoint(point);
						MgGeometryFactory geometryFactory = new MgGeometryFactory();
						String systemCoord = this.project;
				         MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				         MgCoordinateSystem coordinate = coordinateFactory.Create(systemCoord);
			             MgCoordinate coord = geometryFactory.CreateCoordinateXY(Double.valueOf(thePoint.GetCoordinate().GetX()), Double.valueOf(thePoint.GetCoordinate().GetY()));
			             MgCoordinate LonLat = coordinate.ConvertToLonLat (coord);
			             String lat = String.valueOf(LonLat.GetX());
			             map.put("lat", lat);
			             String lon = String.valueOf(LonLat.GetY());
			             map.put("lon", lon);
						return map;
					} 
		    	   
					public  HashMap convertToLonLat2(MgPoint point)throws Exception  {
						HashMap map = new  HashMap();
						MgGeometryFactory geometryFactory = new MgGeometryFactory();
						String systemCoord = this.project;
				         MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				         MgCoordinateSystem coordinate = coordinateFactory.Create(systemCoord);
			             MgCoordinate coord = geometryFactory.CreateCoordinateXY(Double.valueOf(point.GetCoordinate().GetX()), Double.valueOf(point.GetCoordinate().GetY()));
			             MgCoordinate LonLat = coordinate.ConvertToLonLat (coord);
			             String lat = String.valueOf(LonLat.GetX());
			             map.put("lat", lat);
			             String lon = String.valueOf(LonLat.GetY());
			             map.put("lon", lon);
						return map;
					} 
					
					public  HashMap convertToXY(double dLongitude, double dLatitude)throws Exception  {
						HashMap map = new  HashMap();
						MgGeometryFactory geometryFactory = new MgGeometryFactory();
						String systemCoord = this.project;
				         MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				         MgCoordinateSystem coordinate = coordinateFactory.Create(systemCoord);
			             //MgCoordinate coord = geometryFactory.CreateCoordinateXY(Double.valueOf(thePoint.GetCoordinate().GetX()), Double.valueOf(thePoint.GetCoordinate().GetY()));
			             MgCoordinate XY = coordinate.ConvertFromLonLat(dLongitude,dLatitude);
			             String X = String.valueOf(XY.GetX());
			             map.put("X", X);
			             String Y = String.valueOf(XY.GetY());
			             map.put("Y", Y);
						return map;
					} 
		    	   

					  public String getsMapName() {
						    return this.sMapName;
					  }

						  public void setsMapName(String sMapName) {
						    this.sMapName = sMapName;
						  }
		    	   
}
