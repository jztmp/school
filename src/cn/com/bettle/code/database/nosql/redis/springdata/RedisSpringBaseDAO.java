package cn.com.bettle.code.database.nosql.redis.springdata;



import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.com.bettle.code.annotation.NeedRedisCached;
import cn.com.bettle.code.exception.NDBException;
import cn.com.bettle.code.exception.SerializeException;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;
import cn.com.bettle.code.utils.serialize.SerializeUtil;




 
 

/**
 * @author John
 * @version 
 * @param <K>
 * @param <V>
 */
public class RedisSpringBaseDAO<T> {

	private RedisTemplate redisTemplate;
	
	private Class<T> entityClass;  
	
	private long expire;
	
	private String namespace = "";
	
	private int cachType = 0;
	
	private static final Log log = LogFactory.getLog(RedisSpringBaseDAO.class); 

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(
			RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}  
	public RedisSpringBaseDAO(){
	    Type genType = getClass().getGenericSuperclass();  
	    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
	    entityClass = (Class) params[0];
	    if(entityClass.isAnnotationPresent(NeedRedisCached.class)){
	    	NeedRedisCached needRedisCachedAnnotation = (NeedRedisCached)entityClass.getAnnotation(NeedRedisCached.class);
	    	expire = needRedisCachedAnnotation.expire();
	    	namespace = needRedisCachedAnnotation.namespace();
	    	cachType = needRedisCachedAnnotation.cachType();
	    	
	    }
	   
	}
	/**  
     * 向redis里面添加key-value格式的数据  
     *  
     * @param key   key  
     * @param value value  
     */  
	 @SuppressWarnings("unchecked")
    public Boolean insert(final String key, final T value,final boolean jsonSerialize) {   
       return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {   
            @Override  
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {   
            	try {
            	if(jsonSerialize){
            		String json = JsonSerializeUtil.bean2Json(value);
            		switch(cachType){
            			case NeedRedisCached.COMMON:
            				byte[] key_ = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
            				byte[] value_ = redisTemplate.getStringSerializer().serialize(json);
            				if(expire!=0)
            					connection.setEx(key_, expire, value_);
            				else
            					connection.set(key_, value_);
            				break;
            			case NeedRedisCached.MAP:
            				byte[] namespace_ = redisTemplate.getStringSerializer().serialize(namespace); 
            				byte[] keyByte = redisTemplate.getStringSerializer().serialize(key); 
            				byte[] valueByte = redisTemplate.getStringSerializer().serialize(json); 
            				Map<byte[],byte[]> map = new HashMap();
            				map.put(keyByte, valueByte);
            				connection.hMSet(namespace_, map);
            				if(expire!=0)
            					connection.expire(namespace_, expire);
            			case NeedRedisCached.LIST :
            				byte[] key_1 = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
            				byte[] value_1 = redisTemplate.getStringSerializer().serialize(json);
            				connection.rPush(key_1, value_1);
            				if(expire!=0)
            					connection.expire(key_1, expire);
            				break;
            			
            		}
            		
            		return true;
            	}else{
	                switch(cachType){
        			case NeedRedisCached.COMMON:
        				byte[] key_ = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
        				byte[] value_ = SerializeUtil.serialize(value);
        				if(expire!=0)
        					connection.setEx(key_, expire, value_);
        				else
        					connection.set(key_, value_);
        				break;
        			case NeedRedisCached.MAP:
        				byte[] namespace_ = redisTemplate.getStringSerializer().serialize(namespace); 
        				byte[] keyByte = redisTemplate.getStringSerializer().serialize(key); 
        				byte[] valueByte = SerializeUtil.serialize(value); 
        				Map<byte[],byte[]> map = new HashMap();
        				map.put(keyByte, valueByte);
        				connection.hMSet(namespace_, map);
        				if(expire!=0)
        					connection.expire(namespace_, expire);
        			case NeedRedisCached.LIST :
        				byte[] key_1 = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
        				byte[] value_1 = SerializeUtil.serialize(value);
        				connection.rPush(key_1, value_1);
        				if(expire!=0)
        					connection.expire(key_1, expire);
        				break;
        			
        		}
					return true;
                }
            	} catch (SerializeException e) {
            		log.error("RedisSpringBaseDAO :: insert == ", e);
					return false;
				}
            }
        });   
    }   
  
    
    
    @SuppressWarnings("unchecked")  
    public Boolean insert(final String key, final String json,final boolean jsonSerialize)throws NDBException{ 
    	try{
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {   
             @Override  
             public Boolean doInRedis(RedisConnection connection) throws DataAccessException {   
             	try {
             	if(jsonSerialize){
             		switch(cachType){
             			case NeedRedisCached.COMMON:
             				byte[] key_ = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
             				byte[] value_ = redisTemplate.getStringSerializer().serialize(json);
             				if(expire!=0)
             					connection.setEx(key_, expire, value_);
             				else
             					connection.set(key_, value_);
             				break;
             			case NeedRedisCached.MAP:
             				byte[] namespace_ = redisTemplate.getStringSerializer().serialize(namespace); 
             				byte[] keyByte = redisTemplate.getStringSerializer().serialize(key); 
             				byte[] valueByte = redisTemplate.getStringSerializer().serialize(json); 
             				Map<byte[],byte[]> map = new HashMap();
             				map.put(keyByte, valueByte);
             				connection.hMSet(namespace_, map);
             				if(expire!=0)
             					connection.expire(namespace_, expire);
             			case NeedRedisCached.LIST :
             				byte[] key_1 = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
             				byte[] value_1 = redisTemplate.getStringSerializer().serialize(json);
             				connection.rPush(key_1, value_1);
             				if(expire!=0)
             					connection.expire(key_1, expire);
             				break;
             			
             		}
             		
             		return true;
             	}else{
             		T value =  JsonSerializeUtil.json2Bean(json, entityClass);
 	                switch(cachType){
         			case NeedRedisCached.COMMON:
         				byte[] key_ = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
         				byte[] value_ = SerializeUtil.serialize(value);
         				if(expire!=0)
         					connection.setEx(key_, expire, value_);
         				else
         					connection.set(key_, value_);
         				break;
         			case NeedRedisCached.MAP:
         				byte[] namespace_ = redisTemplate.getStringSerializer().serialize(namespace); 
         				byte[] keyByte = redisTemplate.getStringSerializer().serialize(key); 
         				byte[] valueByte = SerializeUtil.serialize(value); 
         				Map<byte[],byte[]> map = new HashMap();
         				map.put(keyByte, valueByte);
         				connection.hMSet(namespace_, map);
         				if(expire!=0)
         					connection.expire(namespace_, expire);
         			case NeedRedisCached.LIST :
         				byte[] key_1 = redisTemplate.getStringSerializer().serialize(namespace+"."+key); 
         				byte[] value_1 = SerializeUtil.serialize(value);
         				connection.rPush(key_1, value_1);
         				if(expire!=0)
         					connection.expire(key_1, expire);
         				break;
         			
         		}
 					return true;
                 }
             	} catch (SerializeException e) {
             		log.error("RedisSpringBaseDAO :: insert == ", e);
 					return false;
 				}
             }
         });   
    	}catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
     }   
 /**  
     * 根据key从redis里面取出value  
     *  
     * @param key   key  
     */  
    @SuppressWarnings("unchecked")
 public List<T> get(final String key,final boolean jsonSerialize)throws NDBException { 
	 try{
        return  (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {   
            @Override  
            public List<T> doInRedis(RedisConnection connection) throws DataAccessException { 
            	List list = new ArrayList();
            	T t = null;
            	try {
	                byte[] keyBytes =  null;
	                if(cachType == NeedRedisCached.MAP){
	                	keyBytes =  redisTemplate.getStringSerializer().serialize(namespace);
	                }else{
	                	keyBytes =  redisTemplate.getStringSerializer().serialize(namespace+"."+key);
	                }
	                
	             
	                
	                if(connection.exists(keyBytes))
	                {
		                if(jsonSerialize){
			                switch(cachType){
		        			case NeedRedisCached.COMMON:
		        				byte[] valueBytes = connection.get(keyBytes); 
		        				String json = (String)redisTemplate.getStringSerializer().deserialize(valueBytes); 
				                t =  JsonSerializeUtil.json2Bean(json, entityClass);
				                list.add(t);
		        				break;
		        			case NeedRedisCached.MAP:
		        				byte[]  key_1 = redisTemplate.getStringSerializer().serialize(key);
		        				List<byte[]> value = connection.hMGet(keyBytes, key_1);
		        				String jsonValue = (String)redisTemplate.getStringSerializer().deserialize(value.get(0));
		        				t =  JsonSerializeUtil.json2Bean(jsonValue, entityClass);
		        				list.add(t);
		        				break;
		        			case NeedRedisCached.LIST :
		        				List<byte[]> list1 = connection.lRange(keyBytes, 0, -1);
		        				//List<byte[]> list1 = redisTemplate.opsForList().range(namespace+"."+key, 0, -1);
		        				for (byte[] x : list1) { 
		        					String jsonValue2 = (String)redisTemplate.getStringSerializer().deserialize(x);
		        					t =  JsonSerializeUtil.json2Bean(jsonValue2, entityClass);
		        					list.add(t);
		        				}
		        				break;
			                }
		                }
		                
	                }else{
		                switch(cachType){
	        			case NeedRedisCached.COMMON:
	        				byte[] valueBytes = connection.get(keyBytes); 
	        				t = (T)SerializeUtil.unserialize(valueBytes);
			                list.add(t);
	        				break;
	        			case NeedRedisCached.MAP:
	        				byte[]  key_1 = redisTemplate.getStringSerializer().serialize(key);
	        				List<byte[]> value = connection.hMGet(keyBytes, key_1);
	        				t = (T)SerializeUtil.unserialize(value.get(0));
	        				list.add(t);
	        				break;
	        			case NeedRedisCached.LIST :
	        				List<byte[]> list1 = connection.lRange(keyBytes, 0, -1);
	        				//List<byte[]> list1 = redisTemplate.opsForList().range(namespace+"."+key, 0, -1);
	        				for (byte[] x : list1) { 
	        					t = (T)SerializeUtil.unserialize(x);
	        					list.add(t);
	        				}
	        				break;
		                }
	                }
            	}catch (SerializeException e) {
            		log.error("RedisSpringBaseDAO :: get == ", e);
				}
                return list;
            }   
        }); 
	 }catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
    }   
 @SuppressWarnings("unchecked")
 public boolean delete(final String key) throws NDBException{  
	 try{
	 return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
		  @Override 
	        public Boolean doInRedis(RedisConnection connection) {
			  Boolean rsl = false;
			     byte[] keyBytes =  null;
	                if(cachType == NeedRedisCached.MAP){
	                	keyBytes =  redisTemplate.getStringSerializer().serialize(namespace);
	                }else{
	                	keyBytes =  redisTemplate.getStringSerializer().serialize(namespace+"."+key);
	                }
	            if(connection.exists(keyBytes)){
	            	
	            	
	            	switch(cachType){
	        			case NeedRedisCached.MAP:
		    				byte[] hkey = redisTemplate.getStringSerializer().serialize(key);
		    				rsl = connection.hDel(keyBytes, hkey);
		    				break;
	        			 default:
	        				 if(connection.del(keyBytes)>0){
		        					rsl = true;  
		       	             }
		        			break;
	            	}
	            	
	            }
	            return rsl; 
	        }  
	    }); 
	 }catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
 }

 @SuppressWarnings("unchecked")
public boolean updata(final String key,final String propertyName,final Object value,final boolean jsonSerialize) throws NDBException{
	 try{
	 return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
		  @Override 
	        public Object doInRedis(RedisConnection connection)throws DataAccessException {
			  try {
			  byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);  
	            if(connection.exists(keyBytes)){
	                byte[] bytes = connection.get(keyBytes);
	                T t = null;
	                if(jsonSerialize){
	                String json = (String)redisTemplate.getStringSerializer().deserialize(bytes); 
	                	t =  JsonSerializeUtil.json2Bean(json, entityClass);
	                }else{
	                	t = (T)SerializeUtil.unserialize(bytes);
	                }
	                String methodName = "set"+StringUtils.capitalize(propertyName);
	                Class valueClass = value.getClass();
	                Method simpleMethod = entityClass.getDeclaredMethod(methodName,  new Class[]{valueClass});
	                simpleMethod.setAccessible(true);
	                simpleMethod.invoke(t, value);
	                if(delete(key)){
		                insert(key,t,jsonSerialize);
		                return true;
	                }
	            }
	            } catch (SecurityException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (NoSuchMethodException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (IllegalArgumentException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (IllegalAccessException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (InvocationTargetException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (SerializeException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				}
			  return false; 
	        }  
	    }); 
	 }catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
 }
 
 @SuppressWarnings("unchecked") 
public boolean updata(final String key,final Map<String,Object> valeMap,final boolean jsonSerialize)throws NDBException{
	 try{
	 return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
		  @Override 
	        public Object doInRedis(RedisConnection connection)throws DataAccessException {
			  try {
			  byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);  
	            if(connection.exists(keyBytes)){
	                byte[] bytes = connection.get(keyBytes); 
	                String json = (String)redisTemplate.getStringSerializer().deserialize(bytes); 
	                T t =  JsonSerializeUtil.json2Bean(json, entityClass);
	                
	                for(String propertyName : valeMap.keySet()){
	                    String methodName = "set"+StringUtils.capitalize(propertyName);
	                    Object value = valeMap.get(propertyName);
		                Class valueClass = value.getClass();
		                Method simpleMethod = entityClass.getDeclaredMethod(methodName,  new Class[]{valueClass});
		                simpleMethod.setAccessible(true);
		                simpleMethod.invoke(t, value);
	                }
	                
	                if(delete(key)){
		                insert(key,t,jsonSerialize);
		                return true;
	                }
	            }
	            } catch (SecurityException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (NoSuchMethodException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (IllegalArgumentException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (IllegalAccessException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				} catch (InvocationTargetException e) {
					throw new NDBException(e.getMessage(),e.getCause());
				}
			  return false; 
	        }  
	    }); 
	 }catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
 }


}