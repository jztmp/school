package cn.com.bettle.code.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
public class JsonSerializeUtil {
	
	    /** 
	         * 序列化方法 
	         * @param bean 
	         * @param type 
	         * @return 
	         */  
	       public static String bean2Json(Object bean){
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
	           return gson.toJson(bean);  
	       }  
	     
		    /** 
	         * 带泛型的集合的序列化方法 
	         * @param bean 
	         * @param type 
	         * @return 
	         */  	       	       
	       public static <T> String list2Json(List<T> list){
	    	   Type type = new com.google.gson.reflect.TypeToken<T>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
	           return gson.toJson(list,type);  
	       }
	       
	       public static <T> List<T> json2List(String json){
	    	   Type type = new com.google.gson.reflect.TypeToken<List<T>>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
	           return gson.fromJson(json, type) ;
	       }
	       
		    /** 
	         * 带泛型的hash的序列化方法 
	         * @param bean 
	         * @param type 
	         * @return 
	         */  	       	       
	       public static <S,T> String map2Json(Map<S,T> map){
	    	   Type type = new com.google.gson.reflect.TypeToken<T>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
	           return gson.toJson(map,type);  
	       }	       
	       
	       
	       public static <S,T> Map<S,T>  json2Map(String json){
	    	   Type type = new com.google.gson.reflect.TypeToken<Map<S,T>>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
	           return gson.fromJson(json,type); 
	       }	
	       /** 
	        * 反序列化方法 
	        * @param json 
	        * @param type 
	        * @return T 泛型表示任意类型
	        */  
	       public static <T> T json2Bean(String json,Class<T> _class) {  
	    	   Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	           return gson.fromJson(json, _class);
	       }
	       

//--------------------------------------测试---------------------------------------------//	       
	    /*   public static class Book{
	    	   private int index;
	    	   private String bookName;
	    	
		    	public Book(int id,String bookName){
		    		this.index = id;
		    		this.bookName = bookName;
		    	}
				public String getBookName() {
					return bookName;
				}
		    	
	       }
	       
	       public static class User{
	    	   private int id;
	    	   private String name;
	    	   private Book book;
	    	
		    	public User(int id,String name,Book book){
		    		this.id = id;
		    		this.name = name;
		    		this.book = book;
		    	}

				public String getName() {
					return name;
				}
	       }
	       */
	       public static void main(String[] args) {
	    	   String aa = "{\"message\": \"用户登录成功\",\"success\": true,\"user\": {\"phone\": \"\",\"sort\": 0,\"status\": \"A\",\"ipad\": \"\",\"type\": \"\",\"apiKey\": \"b89937e93e6398be53ee4af187e92afe38b987bdd5b781d61ea5a56f6a6b1c4b7d6445dcab7097ce28f7b4beeca27da5c0185b014481ebe527caecf8fb1aa4c6\",\"username\": \"zengxm\",\"phoneNumber\": \"13913913999\",\"email\": \"zengxm@breetle.com\",\"userId\": \"6\",\"name\": \"曾徐明\",\"mobile\": \"\"}}";
	    
	    	   Map<String,Object> amp = json2Map(aa); 
	    	   
	    	   Object user = amp.get("user");
	    	   
	    	   return;
	    	   
	       }
}
