package cn.com.bettle.code.appcontext.factory;


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import cn.com.bettle.code.appcontext.context.AppContext;
import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.database.entity.DaoEntity;
import cn.com.bettle.code.database.nosql.redis.jedis.RedisJedisBaseDAO;
import cn.com.bettle.code.database.nosql.redis.jedis.RedisJedisPool;
import cn.com.bettle.code.database.nosql.redis.springdata.RedisSpringBaseDAO;
import cn.com.bettle.code.exception.ConfigLoadException;
import cn.com.bettle.code.exception.DaoParserException;
import cn.com.bettle.code.utils.clazz.ClassUtils2;

import com.bugull.mongo.BuguConnection;
import com.google.code.morphia.Datastore;



/**
 * @author  <b>Name:</b>Jason  <br> <b>Mail:</b>zxmapple@gmail.com  <br>  <b>Mobile :</b>13770763836  <br>  <b>QQ :</b>343816974
 * @category bettle dao 管理工具类，通过它获取具体的dao
 * */

public class DaoFactory {
	
	private static Logger logger = Logger.getLogger(DaoFactory.class);
	
	private static  Hashtable daoMap = new Hashtable();
	
	/**
	 * <b>功能：</b>获取实体对应的Dao
	 * @param clazz  要获取Dao对应的接口
	 * @param applicationContext  Spring容器上下文
	 * @param newInstance  是否创建新的实例
	 * @throws DaoParserException 
	 * @throws IOException 
	 * @throws BeansException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * */
 
     
     @SuppressWarnings("unchecked")
	public static <I> I getDao(Class<I> clazz,String daoConfigName,ApplicationContext applicationContext,boolean newInstance) throws ConfigLoadException, DaoParserException, InstantiationException, IllegalAccessException, BeansException, IOException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    	       

    	 
		       	 Class clazz2 = null;
		    	 I dao = null;
		    	 String dbName = "";
		    	 String technology = "";
		    	 String dbidd = "";
		    	 String classAllName = clazz.getName();
		    	 String className = clazz.getSimpleName();
		    	 String packageName = clazz.getPackage().getName();
		    	 String entityname = StringUtils.substring(className,1,className.length()-3);
		    	 
		    	 //获取Dao.xml里的配置实体
		    	 DaoEntity daoEntity = BettleApplicationContext.getDao(daoConfigName);
		    	 if(daoEntity!=null){
			    	  dbName = daoEntity.getDbtype().toLowerCase().trim();
			    	  technology = StringUtils.capitalize(daoEntity.getTechnology().toLowerCase().trim());
			    	  dbidd = daoEntity.getDbid();
		    	 }

		    	 
		    	 /*如果内存里已经有这个dao则直接返回*/
		    	 String daoStaticName = classAllName + "."+daoConfigName;
		    	 if(daoMap.containsKey(daoStaticName) && !newInstance){
		    		return (I)daoMap.get(daoStaticName);
		    	 }
		    	 
		    	 String dbtype = dbName;
		    	 if(dbName.contains("_")){
		    		 dbtype = dbName.split("_")[0];
		    	 }
		    	 
		    	 String daoName = packageName+"."+dbtype+"impl."+entityname+technology+"DAO";
		    	 if(!StringUtils.isBlank(daoName)){
					
					 try {
						 clazz2 = Class.forName(daoName);
					 } catch (ClassNotFoundException e) {
						 throw new DaoParserException(classAllName+"的dao实现不存在,请查实,"+e.getMessage(),e.getCause());
					 }
		    	 }else{
		    		 throw new ConfigLoadException("您请求的Dao接口"+classAllName+"，没有相应的配置，请查实dao.xml");
		    	 }
		    	 
		    	 if(!ClassUtils2.isInterface(clazz2,classAllName)){
					 throw new DaoParserException("您定义的名为"+daoName+"的Dao没有实现接口"+classAllName);
				 }
		    	 
		         //给具体的Dao赋值数据源 
		     	 
		     	 if(StringUtils.isBlank(dbidd)){
		     		dbidd = dbName+technology;
		     	 }
		     	 Object datasouce = applicationContext.getBean(dbidd);
		     	 if(logger.isDebugEnabled())
		     		 logger.debug("dataSouce::"+datasouce.getClass().getName());
			          if(datasouce instanceof Datastore){
			        	  //mongodb  morphia
							Class[] params = new Class[1];
							params[0] = Datastore.class;
							Constructor con = clazz2.getConstructor(params);
								Datastore datastore = (Datastore) datasouce;
								dao = (I)con.newInstance(datastore);
							
			          }else if(datasouce instanceof BuguConnection){
			        	  //mongodb  bugu
			        	  dao = (I)clazz2.newInstance();
			        	        
			          }else if(datasouce instanceof RedisTemplate){
			        	  //redis  spring
			        	  RedisSpringBaseDAO redisSpringBaseDAO = (RedisSpringBaseDAO) clazz2.newInstance();
			        	  redisSpringBaseDAO.setRedisTemplate((RedisTemplate)datasouce);
			        	  dao = (I)redisSpringBaseDAO;
			        	        
			          }else if(datasouce instanceof RedisJedisPool){
			        	  //redis  jedis
			        	  RedisJedisPool redisJedisPool = (RedisJedisPool)datasouce;
			        	  RedisJedisBaseDAO redisJedisBaseDAO = (RedisJedisBaseDAO)clazz2.newInstance(); 
			        	  redisJedisBaseDAO.setShardedPool(redisJedisPool.getShardedPool());
			        	  redisJedisBaseDAO.setPool(redisJedisPool.getPool());
			        	  dao = (I)redisJedisBaseDAO;
			          }else if(datasouce instanceof SqlSessionFactory){
			        	  //rdbms  mybatis
			        	  SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)datasouce;
			        	  SqlSessionDaoSupport sqlSessionDaoSupport = (SqlSessionDaoSupport)clazz2.newInstance();
			        	  sqlSessionDaoSupport.setSqlSessionFactory(sqlSessionFactory);
			        	  dao = (I)sqlSessionDaoSupport;  
			          }
			          daoMap.put(daoStaticName, dao);
		          
          return dao;
     }
     


}
