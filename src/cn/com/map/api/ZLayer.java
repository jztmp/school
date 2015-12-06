package cn.com.map.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import org.osgeo.mapguide.MgAgfReaderWriter;
import org.osgeo.mapguide.MgBatchPropertyCollection;
import org.osgeo.mapguide.MgBlobProperty;
import org.osgeo.mapguide.MgBooleanProperty;
import org.osgeo.mapguide.MgByteProperty;
import org.osgeo.mapguide.MgByteReader;
import org.osgeo.mapguide.MgByteSource;
import org.osgeo.mapguide.MgClassDefinition;
import org.osgeo.mapguide.MgClobProperty;
import org.osgeo.mapguide.MgCoordinate;
import org.osgeo.mapguide.MgCoordinateCollection;
import org.osgeo.mapguide.MgCoordinateSystem;
import org.osgeo.mapguide.MgCoordinateSystemFactory;
import org.osgeo.mapguide.MgDataPropertyDefinition;
import org.osgeo.mapguide.MgDateTime;
import org.osgeo.mapguide.MgDateTimeProperty;
import org.osgeo.mapguide.MgDeleteFeatures;
import org.osgeo.mapguide.MgDoubleProperty;
import org.osgeo.mapguide.MgException;
import org.osgeo.mapguide.MgFeatureCommandCollection;
import org.osgeo.mapguide.MgFeatureQueryOptions;
import org.osgeo.mapguide.MgFeatureReader;
import org.osgeo.mapguide.MgFeatureSchema;
import org.osgeo.mapguide.MgGeometricPropertyDefinition;
import org.osgeo.mapguide.MgGeometry;
import org.osgeo.mapguide.MgGeometryFactory;
import org.osgeo.mapguide.MgGeometryProperty;
import org.osgeo.mapguide.MgGeometryType;
import org.osgeo.mapguide.MgInsertFeatures;
import org.osgeo.mapguide.MgInt16Property;
import org.osgeo.mapguide.MgInt64Property;
import org.osgeo.mapguide.MgLayer;
import org.osgeo.mapguide.MgLayerBase;
import org.osgeo.mapguide.MgLineString;
import org.osgeo.mapguide.MgLinearRing;
import org.osgeo.mapguide.MgLinearRingCollection;
import org.osgeo.mapguide.MgMimeType;
import org.osgeo.mapguide.MgPoint;
import org.osgeo.mapguide.MgPolygon;
import org.osgeo.mapguide.MgPropertyCollection;
import org.osgeo.mapguide.MgPropertyDefinitionCollection;
import org.osgeo.mapguide.MgPropertyType;
import org.osgeo.mapguide.MgRaster;
import org.osgeo.mapguide.MgRasterProperty;
import org.osgeo.mapguide.MgResourceIdentifier;
import org.osgeo.mapguide.MgResourceService;
import org.osgeo.mapguide.MgSelection;
import org.osgeo.mapguide.MgSingleProperty;
import org.osgeo.mapguide.MgStringProperty;
import org.osgeo.mapguide.MgInt32Property;
import org.osgeo.mapguide.MgUpdateFeatures;
import org.osgeo.mapguide.MgWktReaderWriter;

import cn.com.map.init.*;


import org.osgeo.mapguide.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ZLayer {   
	/**
     * 串行化标识，无实际意义
     */
    private static final long serialVersionUID = 1439142487437643118L;
    
    private ZMap zmap = null;
    
    private MgLayer layer = null;
    
    private String sessionId = "";

    private String layerName = ""; //图层名
    
    private String project = ""; //图层投影
    
    private String projectName = "";//图层投影名称
    
    private String className  = ""; //图层类名
    
    private String type = ""; //图层几何类型  点，线，面
    
    private boolean temporaryLayers = false;
    
    private String permanentlayername = ""; //如果是临时层（其有可能有对应的永久层）
    
    private String temporary_layer_xml_file_path = ""; //如果是临时图层则有相应的xml对应文件
    
    private String geometryName = "";
    
    private Map<String, Integer> propertyCollection;  //<属性名，类型>
    
    private static final Log log = LogFactory.getLog(ZLayer.class);

    public ZLayer() throws MgException{
    	super();
    }
    /**
     * 构造函数。(适用于站点初始化后的构造)
     * 
     * @author     zxm         2008-02-01
     */
    public ZLayer(String _sessionId,String _layerName, ZMap zmap)throws MgException,Exception{
    		this.sessionId = _sessionId;
    		this.zmap = zmap;
    		MapManage mapManage = MapManage.getInstance(); 
    		this.layerName = _layerName;
    		String _mapName = zmap.mapName;
	        Map map = mapManage.getLayerInfor(_mapName, _layerName);
	        log.debug("WMap :: "+JSONArray.fromObject(map).toString());
	        this.className = (String)map.get("className");
	        this.type = (String)map.get("type");
	        this.project = (String)map.get("project");
	        this.permanentlayername = (String)map.get("permanentlayername");
	        this.temporaryLayers = Boolean.parseBoolean((String)map.get("temporaryLayers")); 
	        this.geometryName = (String)map.get("geometryName");
	        if(StringUtils.isEmpty( this.geometryName)){
	        	 this.geometryName = "Geometry";
	        }
	        this.temporary_layer_xml_file_path = (String)map.get("temporary_layer_xml_file_path");
	        this.propertyCollection = (Map<String,Integer>)map.get("propertyCollection");
	        this.layer = zmap.getlayerByName(_layerName);
	}

    public void refreshLayer()throws MgException{
	        if(!this.layer.IsVisible()){
	        	this.layer.SetVisible(true);
	        }
	        this.layer.ForceRefresh();
    }
  
    
    /**
     * 通过特征获取器获取名为propertyName的属性值
     * @param reader
     * @param propertyType
     * @param propertyName
     * @return String propertyValue
     * @throws JDOMException 
     * @throws MapException 
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
    private String getPropertyValueFromReader(MgFeatureReader reader,int propertyType, String propertyName)throws MgException, UnsupportedEncodingException, FileNotFoundException, MapException, JDOMException{
    	String dateTimeStr = "''";
        Boolean isNull = reader.IsNull(propertyName);
    	if(isNull)
    	{
    		dateTimeStr = "''";
    	}
    	else
    	{
        if (propertyType == MgPropertyType.Blob)
        {
        	MgByteReader  aDateTime = reader.GetBLOB(propertyName);
            dateTimeStr = aDateTime.ToString();
        }
        else if (propertyType == MgPropertyType.Boolean)
        {
        	boolean aDateTime = reader.GetBoolean(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.Byte)
        {
        	short  aDateTime = reader.GetByte(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.Clob)
        {
            	MgByteReader  aDateTime = reader.GetCLOB(propertyName);
                dateTimeStr = aDateTime.ToString();
        }
        else if (propertyType == MgPropertyType.DateTime)
        {
                MgDateTime aDateTime = reader.GetDateTime(propertyName);
                dateTimeStr = String.valueOf(aDateTime.GetMonth()) + '.'
                    + String.valueOf(aDateTime.GetDay())+ '.' + String.valueOf(aDateTime.GetYear())
                    + ':' + String.valueOf(aDateTime.GetHour()) + ':' + String.valueOf(aDateTime.GetMinute())
                    + ':' + String.valueOf(aDateTime.GetSecond());
        }
        else if (propertyType == MgPropertyType.Double)
        {
        	double aDateTime = reader.GetDouble(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.Geometry)
        {
            String systemCoord = this.zmap.project;
            MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
            MgCoordinateSystem coordinateSystem = coordinateFactory.Create(systemCoord);
            MgByteReader byteReader = reader.GetGeometry(propertyName);
            MgAgfReaderWriter geometryReaderWriter = new MgAgfReaderWriter();
            MgGeometry geometry = geometryReaderWriter.Read(byteReader);
            MgPoint centerPoint = geometry.GetCentroid();
            double length = this.zmap.convertMetersToCoordinateSystemUnits(geometry.GetLength());
            MgCoordinate coordinate = centerPoint.GetCoordinate();
            ZPoint zpoint = new ZPoint();
            zpoint.setX(coordinate.GetX());
            zpoint.setY(coordinate.GetY());
            zpoint.setLength(length);
            MgCoordinate LonLat = coordinateSystem.ConvertToLonLat(coordinate);
            zpoint.setLat(LonLat.GetX());
            zpoint.setLon(LonLat.GetY());
            return zpoint.toString();
        }
        else if (propertyType == MgPropertyType.Int16)
        {
        	int aDateTime = reader.GetInt16(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.Int32)
        {
        	int aDateTime = reader.GetInt32(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.Int64)
        {
        	long  aDateTime = reader.GetInt64(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.Raster)
        {
        	MgRaster aDateTime = reader.GetRaster(propertyName);
            dateTimeStr = aDateTime.toString();
        }
        else if (propertyType == MgPropertyType.Single)
        {
        	float aDateTime = reader.GetSingle(propertyName);
            dateTimeStr = String.valueOf(aDateTime);
        }
        else if (propertyType == MgPropertyType.String)
        {
        	dateTimeStr = reader.GetString(propertyName);
        }
    	}
        return dateTimeStr;
	  
    }
    		
    private Object getPropertyValueFromReader2(MgFeatureReader reader,int propertyType, String propertyName)throws MgException{
    	Object dateTimeStr = null;
        Boolean isNull = reader.IsNull(propertyName);
    	if(!isNull)
    	{
	        if (propertyType == MgPropertyType.Blob){
	        	dateTimeStr = reader.GetBLOB(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Boolean){
	        	dateTimeStr = reader.GetBoolean(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Byte){
	        	dateTimeStr = reader.GetByte(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Clob){
	        	dateTimeStr = reader.GetCLOB(propertyName);
	        }
	        else if (propertyType == MgPropertyType.DateTime){
	        	dateTimeStr = reader.GetDateTime(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Double){
	        	dateTimeStr = reader.GetDouble(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Geometry){
	        	dateTimeStr = reader.GetGeometry(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Int16){
	        	dateTimeStr = reader.GetInt16(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Int32){
	        	dateTimeStr = reader.GetInt32(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Int64){
	        	dateTimeStr = reader.GetInt64(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Raster){
	        	dateTimeStr = reader.GetRaster(propertyName);
	        }
	        else if (propertyType == MgPropertyType.Single){
	        	dateTimeStr = reader.GetSingle(propertyName);
	        }
	        else if (propertyType == MgPropertyType.String){
	        	dateTimeStr = reader.GetString(propertyName);
	        }
    	}
        return dateTimeStr;
	  
    }
    /**
     * 根据过滤条件获取层中相应的特征源
     * @param layer
     * @param filter
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList getFeatureList(ZFilter zFilter)throws MgException,MapException{
    	
    	ArrayList propertyValueCollection=new ArrayList();			
		MgResourceIdentifier featureResId = null;
		MgFeatureQueryOptions queryOptions =null;
		MgFeatureReader featureReader =null;
		try{
			featureResId = new MgResourceIdentifier(this.layer.GetFeatureSourceId());
				queryOptions = zFilter.createQueryOptions();
				featureReader = zmap.featureService.SelectFeatures(featureResId, this.layer.GetFeatureClassName(), queryOptions);
				while (featureReader.ReadNext()){
					HashMap propertyValue=new HashMap();
					for(int i=0;i<featureReader.GetPropertyCount();i++){
		              String  propertyName = featureReader.GetPropertyName(i);
		              int propertyType = featureReader.GetPropertyType(propertyName);
		              propertyValue.put(propertyName, this.getPropertyValueFromReader(featureReader,propertyType,propertyName));
					}
					propertyValueCollection.add(propertyValue);
				}
		 }catch(Exception e){
	        	throw new MapException(e);
	        }finally{
	        	if(featureReader!=null)
	        		featureReader.Close();
	        }
    	return propertyValueCollection;
    }
    
    
    /**
     *  获得图层中最末的几何要素ID 通过排序获取
     * @return  最后一个id号
     * @throws MgException
     */
    public int getLastId()throws MgException{
        int LastId = -1;
        MgResourceIdentifier resoureId = new MgResourceIdentifier(this.layer.GetFeatureSourceId());
        MgFeatureQueryOptions queryOptions = new MgFeatureQueryOptions();
        //根据FeatId倒序排序查询结果
        MgStringCollection stringCollection = new MgStringCollection();
        stringCollection.Add("FeatId");
        queryOptions.SetOrderingFilter(stringCollection, MgOrderingOption.Descending);
        MgFeatureReader reader = zmap.featureService.SelectFeatures(resoureId, this.layer.GetFeatureClassName(), queryOptions);
        try{
	        if (reader.ReadNext()){
	           LastId = reader.GetInt32("FeatId"); //读取第一条数据的FeatId
	        }
        }catch(MgException e){
        	throw e;
        }finally{
        	if(reader!=null)
        		reader.Close();
        }
        return LastId;
    }
    /**
     *  获得图层中最末的几何要素ID  通过Max函数获取
     * @return  最后一个id号
     * @throws MgException
     */
    public int getLastId2()throws MgException{
        int LastId = -1;
        MgResourceIdentifier resoureId = new MgResourceIdentifier(this.layer.GetFeatureSourceId());
        MgFeatureQueryOptions queryOptions = new MgFeatureQueryOptions();
        queryOptions.AddComputedProperty("FeatId", "Max(FeatId)");
        MgFeatureReader reader = zmap.featureService.SelectFeatures(resoureId, this.layer.GetFeatureClassName(), queryOptions);
        try{
	        if (reader.ReadNext()){
	           LastId = reader.GetInt32("FeatId"); //读取第一条数据的FeatId
	        }
        }catch(MgException e){
        	throw e;
        }finally{
        	if(reader!=null)
        		reader.Close();
        }
        return LastId;
    }
    
    /**
     *  获得图层中所有数据的ID
     * @return  ArrayList 
     * @throws MgException
     */
    public ArrayList getIds()throws MgException,MapException{
        ArrayList Ids = new ArrayList();
        MgResourceIdentifier resoureId = new MgResourceIdentifier(this.layer.GetFeatureSourceId());
        MgFeatureReader reader = zmap.featureService.SelectFeatures(resoureId, this.layer.GetFeatureClassName(), null);
        try{
	        while (reader.ReadNext()){
	        	int Id = reader.GetInt32("FeatId"); //FeatId是每个层都有的
	        	Ids.add(Id);
	        }
        }catch(Exception e){
        	throw new MapException(e);	
        }finally{
        	if(reader!=null)
        		reader.Close();
        }
        return Ids;
    }
    
		
    
	   /**
	    * 获取用户选择的几何元素集合的选择条件
	     *获得选中要素选择集过滤条件
	     * @param filter 
	     * @param layer 
	     * @throws MgException
	     */ 
	    public String getFilterByXml(String filterXml)throws MgException{
	        	String Outfilter="";      
	            MgSelection sel = new MgSelection(this.zmap, filterXml);
	            String str = sel.GenerateFilter((MgLayerBase)this.layer, this.layer.GetFeatureClassName());
	            Outfilter = str;
	            return Outfilter;
	    }
	    
	    
	    
	    
	    /**
	     * 使resoureId标识的要素源设置为可写
	     * @param resoureId 要素源标识符
	     * @param resourceService 要素源
	     * @throws MgException
	     */
	    private void setResourceWriteAble(MgResourceIdentifier resoureId, MgResourceService resourceService)throws MgException{
	        //-----------------------------------------------------------------------------
	        String buffer;
	        MgByteReader reader = resourceService.GetResourceContent(resoureId);
	        buffer = reader.ToString();
	        if (buffer.contains("<Value>TRUE</Value>")){
	            buffer = buffer.replaceAll("<Value>TRUE</Value>", "<Value>FALSE</Value>");
	            byte[] buf_Byte = buffer.getBytes();
	            MgByteSource byteSource = new MgByteSource(buf_Byte, buf_Byte.length);
	            resourceService.SetResource(resoureId, byteSource.GetReader(), null);
	        }
	    }
	    
	    /**
	     * 执行要素操作
	     * @param ftrCmdCol 要素操作集合
	     * @param layer 要素所属的图层
	     * @throws MgException
	     */
	    public  void  handleFeatures(MgFeatureCommandCollection ftrCmdCol, MgLayer layer,boolean showLayer)throws MgException{
	        MgResourceIdentifier resoureId = new MgResourceIdentifier(layer.GetFeatureSourceId());	        
	        this.setResourceWriteAble(resoureId, this.zmap.resourceService);
	        this.zmap.featureService.UpdateFeatures(resoureId, ftrCmdCol, false);
	        if(showLayer){
	        	this.refreshLayer();
	        }
	        //this.zmap.Save(this.zmap.resourceService);
	        //this.zmap.Save(this.zmap.resourceService, resoureId);
	    }
	    
	    
	    /**
	     * 执行插入要素操作
	     * @param layer 要素所属的图层
	     * @param propertyCol 插入要素的属性集合
	     * @throws MgException
	     */
	    public void insertFeature(MgPropertyCollection propertyCol,boolean showLayer)throws MgException{
	        MgFeatureCommandCollection ftrCmdCol = new MgFeatureCommandCollection();
	        ftrCmdCol.Add(new MgInsertFeatures(this.layer.GetFeatureClassName(), propertyCol));
	        handleFeatures(ftrCmdCol, this.layer, showLayer);
	        if(showLayer){
	        	this.refreshLayer();
	        }
	    }
	    
	    /**
	     * 批插入
	     * @param layer
	     * @param batchPropertyCol
	     * @return
	     * @throws MgException
	     */
	    public void insertFeature(MgBatchPropertyCollection batchPropertyCol,boolean showLayer)throws MgException {
	    		MgFeatureCommandCollection ftrCmdCol = new MgFeatureCommandCollection();
	    		for(int i=0;i<batchPropertyCol.GetCount();i++){
	    			MgPropertyCollection propertyCol = batchPropertyCol.GetItem(i);
			        ftrCmdCol.Add(new MgInsertFeatures(this.layer.GetFeatureClassName(), propertyCol));
	    		}
	    		 handleFeatures(ftrCmdCol, this.layer,false);
			        if(showLayer){
			        	this.refreshLayer();
			        }
	    }
	    
	    public void insertFeature(String geoText,Map<String,Object> propertyValuesCollection,boolean showLayer)throws MgException{
	    		MgPropertyCollection propertyValues = createPropertyCollection(geoText,propertyValuesCollection);
			    this.insertFeature(propertyValues,showLayer);
	    }
	   
	    
	    public MgPropertyCollection createPropertyCollection(String geoText, Map<String, Object> propertyValuesCollection) throws MgException
	    {
	      MgAgfReaderWriter agfWriter = new MgAgfReaderWriter();
	      MgByteReader byteReader = null;
	      MgPropertyCollection propertyValues = new MgPropertyCollection();
	      if (geoText != null) {
	        if (this.type.equals("point")) {
	          MgPoint point = makePoint2(geoText);
	          byteReader = agfWriter.Write(point);
	        }
	        else if (this.type.equals("polygon")) {
	          MgPolygon polygon = makePolygon2(geoText);
	          byteReader = agfWriter.Write(polygon);
	        }
	        else if (this.type.equals("line")) {
	          MgLineString lineString = makeLine2(geoText);
	          byteReader = agfWriter.Write(lineString);
	        }
	        propertyValues.Add(new MgGeometryProperty(this.geometryName, byteReader));
	      }
	      Map layerProperty = this.propertyCollection;
	      for (String key : propertyValuesCollection.keySet()) {
	        if (layerProperty.containsKey(key)) {
	          int type = (int)Double.parseDouble(String.valueOf(layerProperty.get(key)));
	          Object value = propertyValuesCollection.get(key);
	          setPropertyValueFromReader(propertyValues, type, key, value);
	        }
	      }
	      return propertyValues;
	    }
	    
	    public MgPropertyCollection createPropertyCollection(MgByteReader byteReader, Map<String, Object> propertyValuesCollection) throws MgException {
	        MgPropertyCollection propertyValues = new MgPropertyCollection();
	        if (byteReader != null) {
	          propertyValues.Add(new MgGeometryProperty(this.geometryName, byteReader));
	        }
	        Map layerProperty = this.propertyCollection;
	        for (String key : propertyValuesCollection.keySet()) {
	          if (layerProperty.containsKey(key)) {
	            int type = (int)Double.parseDouble(String.valueOf(layerProperty.get(key)));
	            Object value = propertyValuesCollection.get(key);
	            setPropertyValueFromReader(propertyValues, type, key, value);
	          }
	        }
	        return propertyValues;
	      }
	    
	    public MgPropertyCollection createPropertyCollection(MgByteReader byteReader, MgFeatureReader featureReader)
	    	    throws MgException
	    	  {
	    	    MgPropertyCollection propertyValues = new MgPropertyCollection();
	    	    Map<String,Integer> layerProperty = this.propertyCollection;
	    	    for (String key : layerProperty.keySet()) {
	    	      int type = (int)Double.parseDouble(String.valueOf(layerProperty.get(key)));
	    	      Object value = getPropertyValueFromReader2(featureReader, type, key);
	    	      setPropertyValueFromReader(propertyValues, type, key, value);
	    	    }

	    	    if (byteReader != null)
	    	    {
	    	      propertyValues.Add(new MgGeometryProperty(this.geometryName, byteReader));
	    	    }
	    	    return propertyValues;
	    	  }
	    
	    /**
	     * 执行删除要素操作
	     * @param layer 要素所属的图层
	     * @param filter 删除要素的查询过滤条件（从要素读取器中得到要删除要素）
	     * @throws MgException
	     */
	    public void deleteFeature(String filter,boolean showLayer)throws MgException{
	        MgFeatureCommandCollection ftrCmdCol = new MgFeatureCommandCollection();
	        ftrCmdCol.Add(new MgDeleteFeatures(this.layer.GetFeatureClassName(), filter));
	        handleFeatures(ftrCmdCol, this.layer,showLayer);
	    }
	    

	    
	    /**
	     * 执行更新要素操作
	     * @param layer 要素所属的图层
	     * @param filter 更新要素的查询过滤条件（从要素读取器中得到要删除要素）
	     * @param propertyCol 更新要素的属性集合
	     * @throws MgException
	     */ 
	    public void updateFeature(String filter, MgPropertyCollection propertyCol,boolean showLayer)throws MgException{
	        MgFeatureCommandCollection ftrCmdCol = new MgFeatureCommandCollection();	        
	        ftrCmdCol.Add(new MgUpdateFeatures(this.layer.GetFeatureClassName(), propertyCol, filter));
	        handleFeatures(ftrCmdCol, this.layer,showLayer);
	    }
	    
	
	    
	    /**
	     * 执行更新要素操作
	     * @param layer 要素所属的图层
	     * @param filter 更新要素的查询过滤条件（从要素读取器中得到要删除要素）
	     * @param propertyCol 更新要素的属性集合
	     * @throws MgException
	     */ 
	    public void updateFeature(String filter, MgBatchPropertyCollection batchPropertyCol,boolean showLayer)throws MgException{
	    	 	MgFeatureCommandCollection ftrCmdCol = new MgFeatureCommandCollection();
	    		for(int i=0;i<batchPropertyCol.GetCount();i++){
	    			MgPropertyCollection propertyCol = batchPropertyCol.GetItem(i);
			        ftrCmdCol.Add(new MgUpdateFeatures(this.layer.GetFeatureClassName(), propertyCol, filter));
	    		}
	    		 handleFeatures(ftrCmdCol, this.layer,false);
	    		if(showLayer){
	    			this.refreshLayer();
	    		}
	    }
	    public void update(String filter, String geoText, Map<String, Object> propertyMap, boolean showLayer) throws MgException
	    {
	      MgPropertyCollection propertyCol = createPropertyCollection(geoText, propertyMap);
	      updateFeature(filter, propertyCol, showLayer);
	    }

	    /**
	     * 获得几何要素属性
	     * @param geoText 坐标字符串(个数，xy……)
	     * @param name 几何属性名
	     * @return MgGeometryProperty 对象
	     * @throws MgException
	     */
	    public static MgGeometryProperty getPolygonGeoPro(String geoText, String name)throws MgException{
	        MgAgfReaderWriter agfReaderWriter = new MgAgfReaderWriter();
	        MgGeometryProperty geometryProperty = null;
	        MgPolygon Polygon = makePolygon(geoText);
	        MgByteReader geometryByteReader = agfReaderWriter.Write(Polygon);
	        geometryProperty = new MgGeometryProperty(name, geometryByteReader);
	        return geometryProperty;
	    }
	    
	    

	    /**
	     *  获得点几何要素属性
	     * @param geoText  点坐标
	     * @param name    几何属性名
	     * @return  MgGeometryProperty对象
	     * @throws MgException
	     */ 
	      public static MgGeometryProperty getPointGeoPro(String geoText, String name)throws MgException{
	        MgAgfReaderWriter agfReaderWriter = new MgAgfReaderWriter();
	        MgGeometryProperty geometryProperty = null;
	        MgPoint Point = makePoint(geoText);
	        MgByteReader geometryByteReader = agfReaderWriter.Write(Point);
	        geometryProperty = new MgGeometryProperty(name, geometryByteReader);
	        return geometryProperty;
	     }
	      

	      public MgGeometryProperty getPointGeoPro2(String geoText, String name) throws MgException {
	        MgAgfReaderWriter agfReaderWriter = new MgAgfReaderWriter();
	        MgGeometryProperty geometryProperty = null;
	        MgPoint Point = makePoint2(geoText);
	        MgByteReader geometryByteReader = agfReaderWriter.Write(Point);
	        geometryProperty = new MgGeometryProperty(name, geometryByteReader);
	        return geometryProperty;
	      }
	        
	      /**
		     *  获得点几何要素属性
		     * @param geoText  点坐标
		     * @param name    几何属性名
		     * @return  MgGeometryProperty对象
		     * @throws MgException
		     */ 
		      public static MgGeometryProperty getLineGeoPro(String geoText, String name)throws MgException{
		        MgAgfReaderWriter agfReaderWriter = new MgAgfReaderWriter();
		        MgGeometryProperty geometryProperty = null;
		        MgLineString line = makeLine(geoText);
		        MgByteReader geometryByteReader = agfReaderWriter.Write(line);
		        geometryProperty = new MgGeometryProperty(name, geometryByteReader);
		        return geometryProperty;
		     }

	 	   /**创建Line
	 	    * @param geoText   矩形坐标
	 	    * @throws MgLineString
	 	    */
		public static MgLineString makeLine(String  geoText)throws MgException{
		        String[] coods = geoText.split(",");
		        MgCoordinateCollection CoordinateCollection = new MgCoordinateCollection();
		        MgGeometryFactory geoFac = new MgGeometryFactory();
		        for (int i = 1; i <= Integer.parseInt(coods[0]) * 2; i += 2){
		            double x = Double.parseDouble(coods[i]);
		            double y = Double.parseDouble(coods[i + 1]);
		            MgCoordinate coordinate = geoFac.CreateCoordinateXY(x, y);
		            CoordinateCollection.Add(coordinate);
		        }
		        MgLineString lineString = geoFac.CreateLineString(CoordinateCollection);
		        return lineString;
		}

		
		  public MgLineString makeLine2(String geoText) throws MgException {
			    String[] coods = geoText.split(",");
			    MgCoordinateCollection CoordinateCollection = new MgCoordinateCollection();
			    MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
			    MgCoordinateSystem mapCoordinate = coordinateFactory.Create(this.zmap.project);
			    MgCoordinateSystem FDOCoordinate = coordinateFactory.Create(this.project);
			    MgCoordinateSystemTransform coordinateSystemTransform = coordinateFactory.GetTransform(mapCoordinate, FDOCoordinate);
			    MgGeometryFactory geoFac = new MgGeometryFactory();
			    for (int i = 1; i <= Integer.parseInt(coods[0]) * 2; i += 2) {
			      double x = Double.parseDouble(coods[i]);
			      double y = Double.parseDouble(coods[(i + 1)]);
			      MgCoordinate coordinate = geoFac.CreateCoordinateXY(x, y);
			      MgCoordinate FDOCoord = coordinateSystemTransform.Transform(coordinate);
			      CoordinateCollection.Add(FDOCoord);
			    }
			    MgLineString lineString = geoFac.CreateLineString(CoordinateCollection);
			    return lineString;
			  }
	   /**创建Polygon
	    * @param geoText   矩形坐标
	    * @throws MgException
	    */
	    public static MgPolygon makePolygon(String  geoText)throws MgException{
	        String[] coods = geoText.split(",");
	        MgLinearRingCollection LinearRingCollection = new MgLinearRingCollection();
	        MgCoordinateCollection CoordinateCollection = new MgCoordinateCollection();
	        MgGeometryFactory geoFac = new MgGeometryFactory();

	        for (int i = 1; i <= Integer.parseInt(coods[0]) * 2; i += 2)
	        {
	            double x = Double.parseDouble(coods[i]);
	            double y = Double.parseDouble(coods[i + 1]);
	            MgCoordinate coordinate = geoFac.CreateCoordinateXY(x, y);
	            CoordinateCollection.Add(coordinate);
	        }

	        MgLinearRing LinearRing = geoFac.CreateLinearRing(CoordinateCollection);
	        CoordinateCollection.Clear();
	        LinearRingCollection.Add(LinearRing);
	        MgPolygon Polygon = geoFac.CreatePolygon(LinearRing, null);
	        return Polygon;
	    }
	    
	    public MgPolygon makePolygon2(String geoText) throws MgException {
	        String[] coods = geoText.split(",");
	        MgLinearRingCollection LinearRingCollection = new MgLinearRingCollection();
	        MgCoordinateCollection CoordinateCollection = new MgCoordinateCollection();

	        MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();

	        MgCoordinateSystem mapCoordinate = coordinateFactory.Create(this.zmap.project);
	        MgCoordinateSystem FDOCoordinate = coordinateFactory.Create(this.project);
	        MgCoordinateSystemTransform coordinateSystemTransform = coordinateFactory.GetTransform(mapCoordinate, FDOCoordinate);

	        MgGeometryFactory geoFac = new MgGeometryFactory();

	        for (int i = 1; i <= Integer.parseInt(coods[0]) * 2; i += 2)
	        {
	          double x = Double.parseDouble(coods[i]);
	          double y = Double.parseDouble(coods[(i + 1)]);
	          MgCoordinate coordinate = geoFac.CreateCoordinateXY(x, y);
	          MgCoordinate FDOCoord = coordinateSystemTransform.Transform(coordinate);
	          CoordinateCollection.Add(FDOCoord);
	        }

	        MgLinearRing LinearRing = geoFac.CreateLinearRing(CoordinateCollection);
	        CoordinateCollection.Clear();
	        LinearRingCollection.Add(LinearRing);
	        MgPolygon Polygon = geoFac.CreatePolygon(LinearRing, null);
	        return Polygon;
	      }
	    
	    /**创建Point
	     * @param geoText   点坐标
	     * @throws MgException
	     */
	    public static MgPoint  makePoint(String  geoText)throws MgException{
	        MgGeometryFactory geometryFactory = new MgGeometryFactory();
		    String[] vertices = geoText.split(",");
		    //创建X,Y坐标
			MgCoordinate coord = geometryFactory.CreateCoordinateXY(Double.valueOf(vertices[0]).doubleValue(), Double.valueOf(vertices[1]).doubleValue());
		    MgPoint Apoint = geometryFactory.CreatePoint(coord);
		    return Apoint; 
	    }
	    public MgPoint makePoint2(String geoText) throws MgException
	    {
	      MgGeometryFactory geometryFactory = new MgGeometryFactory();
	      String[] vertices = geoText.split(",");

	      MgCoordinate mapCoord = geometryFactory.CreateCoordinateXY(Double.valueOf(vertices[0]).doubleValue(), Double.valueOf(vertices[1]).doubleValue());
	      MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();

	      MgCoordinateSystem mapCoordinate = coordinateFactory.Create(this.zmap.project);
	      MgCoordinateSystem FDOCoordinate = coordinateFactory.Create(this.project);
	      MgCoordinateSystemTransform coordinateSystemTransform = coordinateFactory.GetTransform(mapCoordinate, FDOCoordinate);
	      MgCoordinate FDOCoord = coordinateSystemTransform.Transform(mapCoord);
	      MgPoint Apoint = geometryFactory.CreatePoint(FDOCoord);
	      return Apoint;
	    }
	    
	    private void setPropertyValueFromReader(MgPropertyCollection propertyValues,int propertyType, String propertyName,Object propertyValue)throws MgException,ClassCastException{

	    	switch(propertyType){
	    		case MgPropertyType.Blob :
    				propertyValues.Add(new MgBlobProperty(propertyName, (MgByteReader)propertyValue));
	    			break;
	    		case MgPropertyType.Boolean :
	    			propertyValues.Add(new MgBooleanProperty(propertyName,(Boolean)propertyValue));
	    			break;
	    		case MgPropertyType.Byte :
	    			propertyValues.Add(new MgByteProperty(propertyName,(Byte)propertyValue));
	    			break;
	    		case MgPropertyType.Clob :
	    			propertyValues.Add(new MgClobProperty(propertyName, (MgByteReader)propertyValue));
	    			break;
	    		case MgPropertyType.DateTime :
	    			MgDateTime mgDateTime = new MgDateTime();	    			
	    			String strt = propertyValue.toString();	    
	    			
	    			try{
	    				SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");	
	    				java.util.Date d = dfs.parse(strt);
	    				
	    				int idx = strt.indexOf("-");
	    				if (idx>-1){
	    					String yy = strt.substring(0,idx);
	        				strt = strt.substring(idx+1);
	    					idx = strt.indexOf("-");
	    					String mm = strt.substring(0,idx);
	    					strt = strt.substring(idx+1);
	    					String dd = "1";
	    					idx = strt.indexOf(" ");				
	    					if (idx > -1){
	    						dd = strt.substring(0,idx);
	    					}else{
	    						dd = strt.substring(0,strt.length());
	    					}						
	    					
	        				short year = new Short(yy);
	        				short mouth = new Short(mm);
	        				short day = new Short(dd);
	        				mgDateTime.SetDay(day);
	        				mgDateTime.SetMonth(mouth);
	        				mgDateTime.SetYear(year);		    		
	        				propertyValues.Add(new MgDateTimeProperty(propertyName, mgDateTime)); 
	    				}else{
	    					log.error(propertyName);
	        				log.error(strt);
	        				propertyValues.Add(new MgDateTimeProperty(propertyName,(MgDateTime)null));        
	    				}
	    				
		    				    			
		    			break;
	    			}catch(Exception e){
	    				throw new RuntimeException(e);
	    			}
	    			
	    			
	    			
	    		case MgPropertyType.Double :	    			
	    			propertyValues.Add(new MgDoubleProperty(propertyName, (Double)propertyValue));
	    			break;
	    		case MgPropertyType.Geometry :
	    			if ((propertyValue instanceof String))
	    		        propertyValues.Add(new MgDoubleProperty(propertyName, Double.parseDouble((String)propertyValue)));
	    		      else {
	    		        propertyValues.Add(new MgDoubleProperty(propertyName, ((Double)propertyValue).doubleValue()));
	    		      }
	    			break;
	    		case MgPropertyType.Int16 :
	    			   if ((propertyValue instanceof String))
	    			        propertyValues.Add(new MgDoubleProperty(propertyName, Integer.parseInt((String)propertyValue)));
	    			      else {
	    			        propertyValues.Add(new MgInt16Property(propertyName, ((Integer)propertyValue).shortValue()));
	    			      }
	    			break;
	    		case MgPropertyType.Int32 :
	    			  if ((propertyValue instanceof String))
	    			        propertyValues.Add(new MgDoubleProperty(propertyName, Integer.parseInt((String)propertyValue)));
	    			      else {
	    			        propertyValues.Add(new MgInt16Property(propertyName, ((Integer)propertyValue).shortValue()));
	    			      }
	    			break;
	    		case MgPropertyType.Int64 :
	    			if ((propertyValue instanceof String))
	    		        propertyValues.Add(new MgDoubleProperty(propertyName, Integer.parseInt((String)propertyValue)));
	    		      else {
	    		        propertyValues.Add(new MgInt16Property(propertyName, ((Integer)propertyValue).shortValue()));
	    		      }
	    			break;
	    		case MgPropertyType.Raster :
	    			propertyValues.Add(new MgRasterProperty(propertyName, (MgRaster)propertyValue));
	    			break;
	    		case MgPropertyType.Single :
	    			propertyValues.Add(new MgSingleProperty(propertyName, ((Float)propertyValue).floatValue()));
	    			break;
	    		case MgPropertyType.String :
	    			propertyValues.Add(new MgStringProperty(propertyName, (String)propertyValue));
	    			break;
	    	}
    	  
        }
	    
	    
	    private void setPropertyValueFromWirter(MgPropertyDefinitionCollection properties,int propertyType, String propertyName)throws MgException,ClassCastException{
		    MgDataPropertyDefinition StringProperty = new MgDataPropertyDefinition(propertyName);
	    	switch(propertyType){
	    		case MgPropertyType.Blob :
	    			 StringProperty.SetDataType(MgPropertyType.Blob);
	    			break;
	    		case MgPropertyType.Boolean :
	    			StringProperty.SetDataType(MgPropertyType.Boolean);
	    			break;
	    		case MgPropertyType.Byte :
	    			StringProperty.SetDataType(MgPropertyType.Byte);
	    			break;
	    		case MgPropertyType.Clob :
	    			StringProperty.SetDataType(MgPropertyType.Clob);
	    			StringProperty.SetLength(4000);
	    			break;
	    		case MgPropertyType.DateTime :
	    			StringProperty.SetDataType(MgPropertyType.DateTime);
	    			break;
	    		case MgPropertyType.Double :
	    			StringProperty.SetDataType(MgPropertyType.Double);
	    			break;
	    		case MgPropertyType.Geometry :
	    			StringProperty.SetDataType(MgPropertyType.Geometry);
	    			break;
	    		case MgPropertyType.Int16 :
	    			StringProperty.SetDataType(MgPropertyType.Int16);
	    			break;
	    		case MgPropertyType.Int32 :
	    			StringProperty.SetDataType(MgPropertyType.Int32);
	    			break;
	    		case MgPropertyType.Int64 :
	    			StringProperty.SetDataType(MgPropertyType.Int64);
	    			break;
	    		case MgPropertyType.Raster :
	    			StringProperty.SetDataType(MgPropertyType.Raster);
	    			break;
	    		case MgPropertyType.Single :
	    			StringProperty.SetDataType(MgPropertyType.Single);
	    			break;
	    		case MgPropertyType.String :
	    			StringProperty.SetDataType(MgPropertyType.String);
	    			StringProperty.SetLength(500);
	    			break;
	    	}
	    	properties.Add(StringProperty);
    	  
        }
	    
	    
	   
	    
		/**<p>创建临时特征数据源，存放临时数据（临时层）这里主要设计临时层的属性，而临时层里的要素样式则来自xml文件</p>
		 * <p>Session://MgTutorial/Data/Filter.FeatureSource临时特征数据源</p>
		 * <p>Library://MgTutorial/Data/Filter.FeatureSource永久特征数据源</p>
	     * @param parcelMarkerDataResId     要素源标识符
	     * @throws MgException
	     */
	    private void createTemporaryFeatureSource(MgResourceIdentifier parcelMarkerDataResId) throws MgException{
//			==================================Create a temporary Feature Source to store the parcel marker locations.=======================
		   // String ll84Wkt = "GEOGCS[\"LL84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.25722293287],TOWGS84[0,0,0,0,0,0,0]],PRIMEM[\"Greenwich\",0],UNIT[\"Degrees\",1]]";//地图投影系WKT字符串
            String ll84Wkt = this.project;
            log.info("ll84Wkt::"+ll84Wkt);
		    MgClassDefinition parcelMarkerClass = new MgClassDefinition();
		    parcelMarkerClass.SetName(this.className);
		    MgPropertyDefinitionCollection properties = parcelMarkerClass.GetProperties();

		    
		    MgDataPropertyDefinition idProperty = new MgDataPropertyDefinition("FeatId");//要素ID
		    idProperty.SetDataType(MgPropertyType.Int32);
		    idProperty.SetReadOnly(true);//只读
		    idProperty.SetNullable(false);//非空
		    idProperty.SetAutoGeneration(true);//自动递增
		    properties.Add(idProperty);
	         

		    for(String key : propertyCollection.keySet()){
		    	setPropertyValueFromWirter(properties, propertyCollection.get(key).intValue(), key);
		    }
			    
		    MgGeometricPropertyDefinition locationProperty = new MgGeometricPropertyDefinition(this.geometryName);//要素几何属性
		    if(type.equals("point"))
		    {
		    	locationProperty.SetGeometryTypes(MgGeometryType.Point);
		    }
		    else if(type.equals("polygon"))
		    {
		    	 locationProperty.SetGeometryTypes(MgGeometryType.Polygon);	
		    } 	
		    else if(type.equals("line"))
		    {
		    	 locationProperty.SetGeometryTypes(MgGeometryType.LineString);	
		    }
		    locationProperty.SetHasElevation(false);
		    locationProperty.SetHasMeasure(false);
		    locationProperty.SetReadOnly(false);
		    locationProperty.SetSpatialContextAssociation(projectName);
		    properties.Add(locationProperty);

		    MgPropertyDefinitionCollection idProperties = parcelMarkerClass.GetIdentityProperties();
		    idProperties.Add(idProperty);

		    parcelMarkerClass.SetDefaultGeometryPropertyName(this.geometryName);

		    MgFeatureSchema parcelMarkerSchema = new MgFeatureSchema();
		    parcelMarkerSchema.SetName(className+"Schema");
		    parcelMarkerSchema.GetClasses().Add(parcelMarkerClass);
         
		    MgCreateSdfParams sdfParams = new MgCreateSdfParams(projectName, ll84Wkt, parcelMarkerSchema);
		    this.zmap.featureService.CreateFeatureSource(parcelMarkerDataResId, sdfParams);

		}
		
		
		private MgLayer CreateTemporaryLayer(MgResourceIdentifier ResourceIdentifier,int index) throws MgException, ParserConfigurationException, SAXException, IOException, TransformerException{
	           //=====================Create a temporary Layer to display the parcel markers.========================

		    // Load the ParcelMarker layer definition template into
		    // a JSP DOM object, find the "ResourceId" element, and
		    // modify it's content to reference the temporary
		    // feature source.
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document = builder.parse(new File(temporary_layer_xml_file_path));
		    NodeList nodes = document.getElementsByTagName("ResourceId");
		    Node resNode = nodes.item(0);
		    Node resContent = document.createTextNode(ResourceIdentifier.ToString());
		    resNode.appendChild(resContent);

		    // write the modified layer XML definition into a byte stream
		    //
		    TransformerFactory tFactory = TransformerFactory.newInstance();
		    Transformer transformer = tFactory.newTransformer();
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    DOMSource source = new DOMSource(document);
		    StreamResult result = new StreamResult(bos);
		    transformer.transform(source, result);


		    byte[] layerDefinition = bos.toByteArray();
		    MgByteSource byteSource = new MgByteSource(layerDefinition, layerDefinition.length);
		    byteSource.SetMimeType(MgMimeType.Xml);

		    MgResourceIdentifier tempLayerResId = new MgResourceIdentifier("Session:" + sessionId + "//"+className+".LayerDefinition");

		    this.zmap.resourceService.SetResource(tempLayerResId, byteSource.GetReader(), null);

		    // Create an MgLayer object based on the new layer definition
		    // and return it to the caller.

		    MgLayer parcelMarkerLayer = new MgLayer(tempLayerResId, this.zmap.resourceService);
		    parcelMarkerLayer.SetName(layerName);//设置图层名为className
		    parcelMarkerLayer.SetLegendLabel(layerName);//设置图层标注为className
		    parcelMarkerLayer.SetDisplayInLegend(true);//不显示标注
		    parcelMarkerLayer.SetSelectable(false);//设置图层可选
		    if(index==-1){
		    	this.zmap.GetLayers().Add(parcelMarkerLayer);
		    }else{
		    	this.zmap.GetLayers().Insert(index, parcelMarkerLayer);
		    }
		    return parcelMarkerLayer;
		}	
		
		
		/**<p>展示临时标注（非连续标注，既当展示第二个临时标注时关闭前一个临时标注）</p>
	     * @param geoText    标注的几何坐标
	     * @param propertyValuesCollection 标准的属性信息
	     * @param index  临时图层的覆盖层级
	     * @param showOld 是否显示原来已经有的特征
	     * @throws MgException
		 * @throws MapException 
		 * @throws TransformerException 
		 * @throws IOException 
		 * @throws SAXException 
		 * @throws ParserConfigurationException 
	     */	
		
		 public void showtTemporaryMarker(String geoText,Map<String,Object> propertyValuesCollection,int index,boolean showOld) throws MgException, MapException, ParserConfigurationException, SAXException, IOException, TransformerException{
			 if(this.temporaryLayers){
		        MgLayer parcelMarkerLayer = this.layer;
			    MgResourceIdentifier parcelMarkerFeatureSourceId = new MgResourceIdentifier("Session:" + sessionId  + "//"+this.className+".FeatureSource");//图层名应该和图层类名保持一致，否则这里就会找不到图层所对应的要素标识符
			    MgFeatureCommandCollection parcelMarkerCommands = new MgFeatureCommandCollection();
		           if (parcelMarkerLayer == null) {    
		        		   this.createTemporaryFeatureSource(parcelMarkerFeatureSourceId);
		        		   parcelMarkerLayer = this.CreateTemporaryLayer(parcelMarkerFeatureSourceId,index);
		        		   this.layer = parcelMarkerLayer;
		           }
		           else{
		        	   int LastId = this.getLastId2();//获取临时图层最末的几何要素ID
		        	   if(LastId>1 && !showOld){
		        		   parcelMarkerCommands.Add(new MgDeleteFeatures(layerName, "FeatId <="+String.valueOf(LastId)));//删除前一个标注
		        	   }
		           }  
		           MgPropertyCollection propertyValues = createPropertyCollection(geoText,propertyValuesCollection);
		           // Create an insert command for this parcel.		
		           parcelMarkerCommands.Add(new MgInsertFeatures(layerName, propertyValues));
				   if (parcelMarkerCommands.GetCount() > 0) {
		                  handleFeatures(parcelMarkerCommands, this.layer, true);
		           }
				   }else{
		     			 throw new MapException("the layer named ' "+this.layerName+" ' is not temporary layer");
		     		 }
		   }
		
		 
		 public void showtTemporaryMarker2(List<String> geoTextList,List<Map<String,Object>> propertyValuesCollectionList,int index,boolean showOld) throws MgException, MapException, ParserConfigurationException, SAXException, IOException, TransformerException{
			 if(this.temporaryLayers){
			        MgLayer parcelMarkerLayer = this.layer;
				    MgResourceIdentifier parcelMarkerFeatureSourceId = new MgResourceIdentifier("Session:" + sessionId  + "//"+this.className+".FeatureSource");//图层名应该和图层类名保持一致，否则这里就会找不到图层所对应的要素标识符
				    MgFeatureCommandCollection parcelMarkerCommands = new MgFeatureCommandCollection();
			           if (parcelMarkerLayer == null) {    
			        		   this.createTemporaryFeatureSource(parcelMarkerFeatureSourceId);
			        		   parcelMarkerLayer = this.CreateTemporaryLayer(parcelMarkerFeatureSourceId,index);
			        		   this.layer = parcelMarkerLayer;
			           }
			           else{
			        	   int LastId = this.getLastId2();//获取临时图层最末的几何要素ID
			        	   if(LastId>1 && !showOld){
			        		   parcelMarkerCommands.Add(new MgDeleteFeatures(layerName, "FeatId <="+String.valueOf(LastId)));//删除前一个标注
			        	   }
			           }  
			           for(int i=0;i<geoTextList.size();i++){
			        	   String geoText = geoTextList.get(i);
			        	   Map<String,Object> propertyValuesCollection = propertyValuesCollectionList.get(i);
				           MgPropertyCollection propertyValues = createPropertyCollection(geoText,propertyValuesCollection);
				           // Create an insert command for this parcel.		
				           parcelMarkerCommands.Add(new MgInsertFeatures(layerName, propertyValues));
			           }
					   if (parcelMarkerCommands.GetCount() > 0) {
			                  handleFeatures(parcelMarkerCommands, this.layer, true);
			           }
					   }else{
			     			 throw new MapException("the layer named ' "+this.layerName+" ' is not temporary layer");
			     		 }
		   }
		 
		 public void showtTemporaryMarker(MgByteReader byteReader,Map<String,Object> propertyValuesCollection,int index,boolean showOld) throws MgException, MapException, ParserConfigurationException, SAXException, IOException, TransformerException{
			 if(this.temporaryLayers){
		        MgLayer parcelMarkerLayer = this.layer;
			    MgResourceIdentifier parcelMarkerFeatureSourceId = new MgResourceIdentifier("Session:" + sessionId  + "//"+this.className+".FeatureSource");//图层名应该和图层类名保持一致，否则这里就会找不到图层所对应的要素标识符
			    MgFeatureCommandCollection parcelMarkerCommands = new MgFeatureCommandCollection();
		           if (parcelMarkerLayer == null) {    
		        		   this.createTemporaryFeatureSource(parcelMarkerFeatureSourceId);
		        		   parcelMarkerLayer = this.CreateTemporaryLayer(parcelMarkerFeatureSourceId,index);
		        		   this.layer = parcelMarkerLayer;
		           }
		           else{
		        	   int LastId = this.getLastId2();//获取临时图层最末的几何要素ID
		        	   if(LastId>1 && !showOld){
		        		   parcelMarkerCommands.Add(new MgDeleteFeatures(layerName, "FeatId <="+String.valueOf(LastId)));//删除前一个标注
		        	   }
		           }  
		           
			           MgPropertyCollection propertyValues = createPropertyCollection(byteReader,propertyValuesCollection);
			           // Create an insert command for this parcel.		
			           parcelMarkerCommands.Add(new MgInsertFeatures(layerName, propertyValues));
				   if (parcelMarkerCommands.GetCount() > 0) {
		                  handleFeatures(parcelMarkerCommands, this.layer, true);
		           }
				   }else{
		     			 throw new MapException("the layer named ' "+this.layerName+" ' is not temporary layer");
		     		 }
		   }
		 
		 public void showtTemporaryMarker(List<MgByteReader> byteReaderList,List<Map<String,Object>> propertyValuesCollectionList,int index,boolean showOld) throws MgException, MapException, ParserConfigurationException, SAXException, IOException, TransformerException{
			 if(this.temporaryLayers){
				
		        MgLayer parcelMarkerLayer = this.layer;
		        MgBatchPropertyCollection batchPropertyCol = new MgBatchPropertyCollection();
			    MgResourceIdentifier parcelMarkerFeatureSourceId = new MgResourceIdentifier("Session:" + sessionId  + "//"+this.className+".FeatureSource");//图层名应该和图层类名保持一致，否则这里就会找不到图层所对应的要素标识符
			    MgFeatureCommandCollection parcelMarkerCommands = new MgFeatureCommandCollection();
		           if (parcelMarkerLayer == null) {    
		        		   this.createTemporaryFeatureSource(parcelMarkerFeatureSourceId);
		        		   parcelMarkerLayer = this.CreateTemporaryLayer(parcelMarkerFeatureSourceId,index);
		        		   this.layer = parcelMarkerLayer;
		           }
		           else{
		        	   int LastId = this.getLastId2();//获取临时图层最末的几何要素ID
		        	   if(LastId>1 && !showOld){
		        		   parcelMarkerCommands.Add(new MgDeleteFeatures(layerName, "FeatId <="+String.valueOf(LastId)));//删除所有数据
		        	   }
		           } 
		           
		           for(int i=0;i<byteReaderList.size();i++){
		        	   MgByteReader byteReader = byteReaderList.get(i);
		        	   Map<String,Object> propertyValuesCollection = propertyValuesCollectionList.get(i);
		        	   MgPropertyCollection propertyValues = createPropertyCollection(byteReader,propertyValuesCollection);
		        	   parcelMarkerCommands.Add(new MgInsertFeatures(layerName, propertyValues));
		           }
		           // Create an insert command for this parcel.		
		           
				   if (parcelMarkerCommands.GetCount() > 0) {
		                  handleFeatures(parcelMarkerCommands, this.layer, true);
		           }
				   }else{
		     			 throw new MapException("the layer named ' "+this.layerName+" ' is not temporary layer");
		     		}
		   }
		 
		 
		 
		   public void clearTemporaryMarker(boolean refresh) throws MgException, MapException{
			   if(this.temporaryLayers){
			        ArrayList ids = this.getIds();
			        String filter = "";
			        if(ids.size()>0){
				  	    for(int i=0;i<ids.size();i++){
				  	    	filter += ","+String.valueOf(ids.get(i));
				  	    	
				  	    }
				  	    if(!filter.equals("")){
				  	    	filter = " FeatId in ("+filter.substring(1)+")";
				  	    }
				  	    this.deleteFeature(filter,refresh);
			        }
			   }else{
	     			 throw new MapException("the layer named ' "+this.layerName+" ' is not temporary layer");
			   }
		   }
		   
		   
		/**<p>用临时标注展示用户查询的永久层数据（当永久层要素过多时，全部显示太密集。因此一般是不显示永久层要素数据，而是通过临时标注层来临时显示）</p>
		 * @param filte   过滤条件
		 * @param index   临时图层的顺序
		 * @throws TransformerException 
		 * @throws IOException 
		 * @throws SAXException 
		 * @throws ParserConfigurationException 
		 * @throws MapException 
		     * @throws Exception
		     */	   
	     	 public void showMarkerForPermanentLayerDate(String filte,int index) throws MgException, ParserConfigurationException, SAXException, IOException, TransformerException, MapException{	 
	     		 if(this.temporaryLayers){
					    MgLayer layer = this.zmap.getlayerByName(permanentlayername);
				        MgLayer parcelMarkerLayer = this.zmap.getlayerByName(layerName);
				        MgResourceIdentifier parcelMarkerFeatureSourceId = new MgResourceIdentifier("Session:" + sessionId  + "//"+className+".FeatureSource");
				        MgFeatureCommandCollection parcelMarkerCommands = new MgFeatureCommandCollection();
				        
			            if (parcelMarkerLayer == null){    
			        		   this.createTemporaryFeatureSource(parcelMarkerFeatureSourceId);
			        		   parcelMarkerLayer = this.CreateTemporaryLayer(parcelMarkerFeatureSourceId,index);
			            }
						//---------------------------------------------------------------------------------
						
				        MgResourceIdentifier featureResId = null;
						MgFeatureQueryOptions queryOptions =null;
						MgFeatureReader featureReader =null;
						MgByteReader byteReader=null;
						try{
								MgAgfReaderWriter geometryReaderWriter = new MgAgfReaderWriter();
								MgWktReaderWriter wktReaderWriter=new MgWktReaderWriter();     
				
							    featureResId = new MgResourceIdentifier(layer.GetFeatureSourceId());
								queryOptions = new MgFeatureQueryOptions();
								queryOptions.SetFilter(filte);
								featureReader = this.zmap.featureService.SelectFeatures(featureResId, layer.GetFeatureClassName(), queryOptions);
				
					        	while (featureReader.ReadNext()){
							        byteReader = featureReader.GetGeometry(this.geometryName);/*默认永久层是point*/
						            MgGeometry geometry = geometryReaderWriter.Read(byteReader);
						  
						            if(geometry.GetGeometryType ()==3){/*如果永久层是polygon*/
						            
						            	MgPolygon polygon=(MgPolygon)geometry;
						            	byteReader=geometryReaderWriter.Write(polygon.GetCentroid());
						            }
						            
							    	MgPropertyCollection propertyValues = createPropertyCollection(byteReader,featureReader);
						            // Create an insert command for this parcel.		
						            parcelMarkerCommands.Add(new MgInsertFeatures(layerName, propertyValues));
					            }
					    
								if (parcelMarkerCommands.GetCount() > 0){
									handleFeatures(parcelMarkerCommands, parcelMarkerLayer,true);
					            }
			     		}catch(Exception e){
				                throw new MapException();	
				        }finally {
				     			try{
					     			 if(featureReader!=null){
					     				featureReader.Close();
					     			  }
				     			}
				     			catch(Exception e){            
				     				throw new MapException(e);
				     			}
				         	}
	     		 }else{
	     			 throw new MapException("the layer named ' "+this.layerName+" ' is not temporary layer");
	     		 }
	     }
	     	 
	     	 
	     	 public String createSelectionXML(String queryString) throws MgException,MapException{
	     		 String returnString=null;
	             MgResourceIdentifier resId = new MgResourceIdentifier(layer.GetFeatureSourceId());
	             MgFeatureQueryOptions queryOption = new MgFeatureQueryOptions();
	             //queryOption.AddFeatureProperty("Autogenerated_SDF_ID");
	             queryOption.SetFilter(queryString);
	             MgFeatureReader featureReader = this.zmap.featureService.SelectFeatures(resId, layer.GetFeatureClassName(), queryOption);
	             try{
			             featureReader.ReadNext();
			             MgSelection selection = new MgSelection(this.zmap);
			             selection.AddFeatures(layer, featureReader, 0);
			             returnString = selection.ToXml();
		         	}
		         	catch(Exception e){
		         		throw new MapException(e);
		         	}finally {
		     			try{
			     			 if(featureReader!=null){
			     				featureReader.Close();
			     			  }
		     			}
		     			catch(Exception e){            
		     				throw new MapException(e);
		     			}
		         	}
	             return  returnString;
	         }
	     	 
	     	 	/**
		     	 * 
		     	 *  使用临时图层_lyaer来展示本图层中filter所过滤的数据
		     	 * @param filte   过滤条件
		     	 * @param _lyaer  临时图层
		     	 * @param  index  临时图层的图层顺序
		     	 * @return ArrayList<Map>  包含了所有要展示的数据信息和几何中心点信息
	     	 	 * @throws MapException 
	     	 	 * @throws ParserConfigurationException 
	     	 	 * @throws SAXException 
	     	 	 * @throws IOException 
	     	 	 * @throws TransformerException 
	     	 	 * @throws JDOMException 
		     	 */
		     public ArrayList<Map> showGeometryByfilter(ZFilter filter,ZLayer _lyaer,int index)throws MgException, MapException, ParserConfigurationException, SAXException, IOException, TransformerException, JDOMException{
		         	ArrayList<Map> value=new ArrayList();
		            MgResourceIdentifier featureResId = null;
		     		MgFeatureQueryOptions queryOptions =null;
		     		MgFeatureReader featureReader =null;

		         	try
		         	{
		     		    featureResId = new MgResourceIdentifier(this.layer.GetFeatureSourceId());
		     			queryOptions = filter.createQueryOptions();
		     			String systemCoord = this.zmap.project;
				        MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				        MgCoordinateSystem coordinateSystem = coordinateFactory.Create(systemCoord);
		     			featureReader = this.zmap.featureService.SelectFeatures(featureResId, this.layer.GetFeatureClassName(), queryOptions);
		     			
		     			List<MgByteReader> list1 = new ArrayList();
		     			List<Map<String,Object>> list2 = new ArrayList();
		     			while(featureReader.ReadNext()){
		     				    Map<String,Object> hm1 = new HashMap();
		     				    Map<String,Object> hm2 = new HashMap();
		             		    MgAgfReaderWriter geometryReaderWriter = new MgAgfReaderWriter();
		             		    MgByteReader byteReader = featureReader.GetGeometry(this.geometryName);/*默认永久层是point*/
					            MgGeometry geometry = geometryReaderWriter.Read(byteReader);
					            //获取几何中心点
					            MgPoint centerPoint = geometry.GetCentroid();
					            double length = this.zmap.convertMetersToCoordinateSystemUnits(geometry.GetLength());
					            MgCoordinate coordinate = centerPoint.GetCoordinate();							       
					            ZPoint zpoint = new ZPoint();
					            zpoint.setX(coordinate.GetX());
					            zpoint.setY(coordinate.GetY());
					            zpoint.setLength(length);
					            MgCoordinate LonLat = coordinateSystem.ConvertToLonLat (coordinate);
					            zpoint.setLat(LonLat.GetX());
					            zpoint.setLon(LonLat.GetY());

					            
					            hm1.put("centerPoint", zpoint);
								for(int i=0;i<featureReader.GetPropertyCount();i++){
					              String  propertyName = featureReader.GetPropertyName(i);
					              int propertyType = featureReader.GetPropertyType(propertyName);
					              hm1.put(propertyName, this.getPropertyValueFromReader(featureReader,propertyType,propertyName));
					              hm2.put(propertyName, this.getPropertyValueFromReader2(featureReader,propertyType,propertyName));
								}
							 list1.add(byteReader);
							 list2.add(hm2);
		             		 value.add(hm1);
		             		 
		                 }
		     			 _lyaer.showtTemporaryMarker(list1, list2,index,false);
		             return value;
		         	}
		         	catch(Exception e){
		         		throw new MapException(e);	
		         	}finally {
		     			try{
			     			 if(featureReader!=null){
			     				featureReader.Close();
			     			  }
		     			}
		     			catch(Exception e){            
		     				throw new MapException(e);
		     			}
		         	}
		         }
		     
	     	 	/**
		     	 * 
		     	 *  获取本图层中filter所过滤的数据
		     	 * @param filte   过滤条件
		     	 * @param _lyaer  临时图层
		     	 * @param  index  临时图层的图层顺序
		     	 * @return ArrayList<Map>
	     	 	 * @throws MapException 
	     	 	 * @throws ParserConfigurationException 
	     	 	 * @throws SAXException 
	     	 	 * @throws IOException 
	     	 	 * @throws TransformerException 
	     	 	 * @throws JDOMException 
		     	 */
		         public ArrayList getGeometryProptyByfilter(ZFilter filter)throws MgException, MapException, ParserConfigurationException, SAXException, IOException, TransformerException, JDOMException
		         {
		         	ArrayList value=new ArrayList();
		            MgResourceIdentifier featureResId = null;
		     		MgFeatureQueryOptions queryOptions =null;
		     		MgFeatureReader featureReader =null;

		         	try
		         	{
		     			String systemCoord = this.zmap.project;
				        MgCoordinateSystemFactory coordinateFactory = new MgCoordinateSystemFactory();
				        MgCoordinateSystem coordinateSystem = coordinateFactory.Create(systemCoord);
		     		    featureResId = new MgResourceIdentifier(this.layer.GetFeatureSourceId());
		     		    queryOptions = filter.createQueryOptions();
		     			featureReader = this.zmap.featureService.SelectFeatures(featureResId, this.layer.GetFeatureClassName(), queryOptions);
		     			while(featureReader.ReadNext()){
		     					Map<String,Object> hm = new HashMap();
		             		    MgAgfReaderWriter geometryReaderWriter = new MgAgfReaderWriter();
		             		    MgByteReader byteReader = featureReader.GetGeometry(this.geometryName);/*默认永久层是point*/
					            MgGeometry geometry = geometryReaderWriter.Read(byteReader);			            
					            MgPoint centerPoint = geometry.GetCentroid();
					            double length = this.zmap.convertMetersToCoordinateSystemUnits(geometry.GetLength());
					            MgCoordinate coordinate = centerPoint.GetCoordinate();							       
					            ZPoint zpoint = new ZPoint();
					            zpoint.setX(coordinate.GetX());
					            zpoint.setY(coordinate.GetY());
					            zpoint.setLength(length);
					            MgCoordinate LonLat = coordinateSystem.ConvertToLonLat (coordinate);
					            zpoint.setLat(LonLat.GetX());
					            zpoint.setLon(LonLat.GetY());
					            hm.put("centerPoint", zpoint);
								for(int i=0;i<featureReader.GetPropertyCount();i++){
					              String  propertyName = featureReader.GetPropertyName(i);
					              int propertyType = featureReader.GetPropertyType(propertyName);
					              hm.put(propertyName, this.getPropertyValueFromReader(featureReader,propertyType,propertyName));
								}
		             		 value.add(hm);
		     			}
		             return value;
		         	}
		         	catch(Exception e)
		         	{
		         		throw new MapException(e);	
		         	}finally {
		     			
		     			try
		     			{
		     				if(featureReader!=null)
		     				{
		     				featureReader.Close();
		     			  }
		     			}
		     			catch(Exception e){            
		     				throw new MapException(e);
		     			}
		         	}
		         }	         
  
		         
		         /**
		     	     *  判断图层中某个要素是否存在
		     	     * @param ZFilter  过滤器
		     	     * @return  boolean
		     	     * @throws MgException 
		     	     * @throws Exception
		     	     */
		     	    public boolean isExist(ZFilter filter) throws MgException,MapException{
		     	    	boolean a=false;
		     	        MgResourceIdentifier resoureId = null;
		     	        MgFeatureReader featureReader =null;
		     	        try{
		     	    		resoureId = new MgResourceIdentifier(layer.GetFeatureSourceId());
		     	    		 MgFeatureQueryOptions queryOptions = filter.createQueryOptions();
		     	    		featureReader =  this.zmap.featureService.SelectFeatures(resoureId, layer.GetFeatureClassName(), queryOptions);
		     	    		a=featureReader.ReadNext();
		     	        }catch(Exception e){
		     	        	throw new MapException(e);		
			     	     }finally {
			     			try{
			     				if(featureReader!=null){
			     				featureReader.Close();
			     			  }
			     			}catch(Exception e){            
			     				throw new MapException(e);	
			     			}
			     	     }
		     	        return a;
		     	    }


}
