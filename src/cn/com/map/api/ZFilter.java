package cn.com.map.api;

import org.osgeo.mapguide.MgException;
import org.osgeo.mapguide.MgFeatureQueryOptions;
import org.osgeo.mapguide.MgGeometry;

public class ZFilter {
	
  public String basicOperation;

  public int spatialOperation = -1;
  
  public MgGeometry geometry;
  
  public String geometryProperty;
  
  public int condition = -1;
  
  public static final int AND = 0;
  
  public static final int OR = 1;
  
  public static final int ONLY_BASIC = 3;
  
  public static final int ONLY_SPATIAL = 4;
  
  public ZFilter(String _basicOperation,String _geometryProperty,MgGeometry _geometry,int _spatialOperation,int _condition){
	  this.basicOperation = _basicOperation;
	  this.spatialOperation = _spatialOperation;
	  this.geometry = _geometry;
	  this.geometryProperty = _geometryProperty;
	  this.condition = _condition;
  }
  
  public MgFeatureQueryOptions createQueryOptions() throws MgException{
	  MgFeatureQueryOptions queryOptions = new MgFeatureQueryOptions();
	  if(basicOperation!=null && condition!=ONLY_SPATIAL){
		  queryOptions.SetFilter(basicOperation);
	  }
	  if(geometryProperty!=null && geometry!=null && spatialOperation!=-1 && condition!=ONLY_BASIC){
		  queryOptions.SetSpatialFilter(geometryProperty, geometry, spatialOperation);
	  }
	  
	  switch(condition){
		  case AND :
			  queryOptions.SetBinaryOperator(true);
			  break;
		  case OR :
			  queryOptions.SetBinaryOperator(false);
			  break;
	  }
	  
	  return queryOptions;
  }
}
