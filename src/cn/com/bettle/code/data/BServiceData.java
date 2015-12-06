package cn.com.bettle.code.data;

import  java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import cn.com.bettle.code.exception.ServiceDataException;


public class BServiceData extends Hashtable<String,Map<String,Object>> {
         
		  private final static String IN = "IN";
		  
		  private final static String OUT = "OUT";
		  
	       public final Object getInParameters(String seriveName) {
	    	   if(!this.containsKey(seriveName)){
	    		   this.put(seriveName, new HashMap<String,Object>());
	    	   }
		       return this.get(seriveName).get(IN);
		   }

		   public final Object getOutParameters(String seriveName) {
			   if(!this.containsKey(seriveName)){
	    		   this.put(seriveName, new HashMap<String,Object>());
	    	   }
			   return this.get(seriveName).get(OUT);
		   }
		   
		   public Map<String,Object> getParameters(String seriveName) {
			   if(!this.containsKey(seriveName)){
	    		   this.put(seriveName, new HashMap<String,Object>());
	    	   }
			   return this.get(seriveName);
		   }
		   
		   public final void setInParameters(String seriveName,Object data) {
			   if(!this.containsKey(seriveName)){
				   this.put(seriveName, new HashMap<String,Object>());
			   }
			   this.get(seriveName).put(IN, data);
		   }
			   
			 
		   public final void setOutParameters(String seriveName,Object data) {
			   if(!this.containsKey(seriveName)){
				   this.put(seriveName, new HashMap<String,Object>());
			   }
			   this.get(seriveName).put(OUT, data);
		   }
}
