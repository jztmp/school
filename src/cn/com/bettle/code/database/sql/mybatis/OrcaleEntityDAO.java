/*
 *    Copyright 2010 The myBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.com.bettle.code.database.sql.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;  
import java.lang.reflect.Type;  

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bettle.code.annotation.SQL;
import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.exception.DBException;
import cn.com.bettle.code.utils.clazz.DynamicBean;
import cn.com.bettle.code.utils.serialize.SerializeUtil;


/**
 * <p>关系数据库Dao的MyBatis实现</p>
 *  <b>功能描述：</b>		使用MyBatis实现DAO的功能
 * @author   John    zeng.xuming@zte.com.cn
 * @version 0.1
 */
public  class OrcaleEntityDAO<T> extends SqlSessionDaoSupport{
	
	private static Logger logger = Logger.getLogger(OrcaleEntityDAO.class);
	
		public Class<T> entityClass;  
		
		public OrcaleEntityDAO(){
		    Type genType = getClass().getGenericSuperclass();  
		    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
		    entityClass = (Class) params[0];  
		}
		
		public String getSqlId(int type)throws DBException{
			String sqlId = "";

			
			if(!entityClass.isAnnotationPresent(SQL.class)){
				//如果没有SQL注解则sqlId为  实体类名 + “_”+首字母大写的方法名
				throw new DBException("类"+entityClass.getName()+"没有@SQL注解");
			}else{
				//否则去注解中方法名对应的值
				SQL sqlAnnotation = (SQL)entityClass.getAnnotation(SQL.class);
				switch(type){
					case SQL.LOADLIST:
						sqlId = sqlAnnotation.loadList();
						break;
					case SQL.LOADONE:	
						sqlId = sqlAnnotation.loadOne();
						break;
					case SQL.LOADPAGEINFO:	
						sqlId = sqlAnnotation.loadPageInfo();
						break;
					case SQL.INSERT:
						sqlId = sqlAnnotation.insert();
						break;
					case SQL.DELETE:
						sqlId = sqlAnnotation.delete();
						break;
					case SQL.UPDATE:
						sqlId = sqlAnnotation.update();
						break;
		
				}
					String namespace = sqlAnnotation.namespace();
					sqlId = namespace+sqlId;
			}
			return sqlId;
		}
	
	
		@Transactional(readOnly=true)
		public List<T> loadList(T params)throws DBException{
			String sqlId = "";
			try{
				sqlId = getSqlId(SQL.LOADLIST);
				return this.getSqlSession().selectList(sqlId, params);
			}catch(Exception e){
				throw new DBException("获取数据失败OrcaleBatisBaseDAO::loadList(T params)::sqlId="+sqlId+";params="+SerializeUtil.bean2Map(params),e);
			}
		}
		
		@Transactional(readOnly=true)
		public List<T> loadList(String sqlId,T params)throws DBException{
			try{
				return this.getSqlSession().selectList(sqlId, params);
			}catch(Exception e){
				throw new DBException("获取数据失败OrcaleBatisBaseDAO::loadList(T params)::sqlId="+sqlId+";params="+SerializeUtil.bean2Map(params),e);
			}
		}
	
		@Transactional(readOnly=true)
		public T loadOne(T params)throws DBException{
			String sqlId = "";
			try{
				 sqlId = getSqlId(SQL.LOADONE);
				 return (T)this.getSqlSession().selectOne(sqlId, params);
			}catch(Exception e){
				throw new DBException("获取数据失败OrcaleBatisBaseDAO::loadOne(T params)::sqlId="+sqlId+";params="+SerializeUtil.bean2Map(params),e);
			}
		}

		@Transactional(readOnly=true)
		public T loadOne(String sqlId,T params)throws DBException{
			try{
				 return (T)this.getSqlSession().selectOne(sqlId, params);
			}catch(Exception e){
				throw new DBException("获取数据失败OrcaleBatisBaseDAO::loadOne(T params)::sqlId="+sqlId+";params="+SerializeUtil.bean2Map(params),e);
			}
		}
	
	//---------------------------------------分页查询----------------------------------------------------   
		/**
		 * @throws NoSuchMethodException 
		 * @throws InvocationTargetException 
		 * @throws InstantiationException 
		 * @throws IllegalAccessException 
		 * @throws ClassNotFoundException 
		 * 分页查询指定的记录条数
		 * @throws 
		 */
	    protected List pageQuery(String sqlId ,T entity,long total,int pageNum,int pageSize)throws InstantiationException, IllegalAccessException, IntrospectionException, CannotCompileException, RuntimeException, NotFoundException, NoSuchMethodException, InvocationTargetException{

	    	
	    	long start = pageNum*pageSize;
	    	long limit = pageSize;
			long last = this.getLastIndex(start, limit, total);
		      Map map = new HashMap();
		      map.put("pageStart", Integer.class.getName());
		      map.put("pageLimit", Integer.class.getName());
		      Class<T> clazz = (Class<T>) DynamicBean.addBeanField(entity,map);
		      T bean = clazz.newInstance();
		      Method setMethod = clazz.getMethod(
		              "setPageStart",
		              new Class[] { Integer.class });
		      setMethod.invoke(bean,
		                  new Object[] { start });

		      Method setMethod2 = clazz.getMethod(
		              "setPageLimit",
		              new Class[] { Integer.class });
		      setMethod2.invoke(bean,
		                  new Object[] { last });
		      
			return this.getSqlSession().selectList(sqlId, bean);
		}


	    
	    protected long getLastIndex(long start, long limit, long total) {
			if (total < (start + limit))
				return total;
			else
				return limit;
		}
		
		/**
		 * 返回符合分页查询条件的记录总数
		 */
		@Transactional(readOnly=true)
		protected long getRowCount(String sqlId ,Object params)throws DBException{
			try{
				return (Long)this.getSqlSession().selectOne(sqlId, params);
			}catch(Exception e){
				throw new DBException("获取数据失败OrcaleBatisBaseDAO::getRowCount(String sqlId,Object params)::sqlId="+sqlId+";params="+SerializeUtil.bean2Map(params),e);
			}
		}
		
		 /** 
		 * 返回符合分页查询条件的记录
		 * @param sqlId     SqlMap配置文件中对应的 ID
		 * @param params    待处理的数据对应SqlMap配置文件中的  parameterClass
		 * @param getSqlMapClientTemplate()  ibatis的getSqlMapClientTemplate()对象
		 * @return PageInfo  分页信息
		 * @throws NoSuchMethodException 
		 * @throws InvocationTargetException 
		 * @throws InstantiationException 
		 * @throws IllegalAccessException 
		 * @throws ClassNotFoundException 
		 * @throws NotFoundException 
		 * @throws RuntimeException 
		 * @throws CannotCompileException 
		 * @throws IntrospectionException 
		 * @throws VerificationException 
	     */
		@Transactional(readOnly=true)
	    public PageInfo<T> loadPageInfo(T entity,int pageNum,int pageSize)throws DBException {	
			PageInfo<T> pageInfo = new PageInfo<T>();
			String sqlId = "";
			try{
				sqlId = getSqlId(SQL.LOADPAGEINFO);
				long total = this.getRowCount(sqlId+"_COUNT_", entity);
				List<T> list = this.pageQuery(sqlId+"_PAGE_", entity,total, pageNum, pageSize);
				pageInfo.setPageNum(pageNum);
				pageInfo.setPageSize(pageSize);
				pageInfo.setRows(list);
				pageInfo.setTotal(total);
				return pageInfo;
		    }catch(Exception e){
		    	throw new DBException("获取数据失败OrcaleBatisBaseDAO::loadPageInfo(T entity,PageInfo<T> pageInfo)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity)+";pageInfo="+SerializeUtil.bean2Map(pageInfo),e);
			}
	    }

		@Transactional(readOnly=true)
	    public PageInfo<T> loadPageInfo(String sqlId,T entity,int pageNum,int pageSize)throws DBException {	
			PageInfo<T> pageInfo = new PageInfo<T>();
			try{
				long total = this.getRowCount(sqlId+"_COUNT_", entity);
				List<T> list = this.pageQuery(sqlId+"_PAGE_", entity,total, pageNum, pageSize);
				pageInfo.setPageNum(pageNum);
				pageInfo.setPageSize(pageSize);
				pageInfo.setRows(list);
				pageInfo.setTotal(total);
				return pageInfo;
		    }catch(Exception e){
		    	throw new DBException("获取数据失败OrcaleBatisBaseDAO::loadPageInfo(String sqlId,T entity,PageInfo<T> pageInfo)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity)+";pageInfo="+SerializeUtil.bean2Map(pageInfo),e);
			}
	    }
		
		@Transactional(propagation=Propagation.REQUIRED)
		public void insert(T entity)throws DBException{
			String sqlId = "";
			try{
				    sqlId = getSqlId(SQL.INSERT);
					this.getSqlSession().insert(sqlId, entity);
			 }catch(Exception e){
				 throw new DBException("获取数据失败OrcaleBatisBaseDAO::insert(T entity)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity),e);
		     }
		}
		
		@Transactional(propagation=Propagation.REQUIRED)
		public void insert(String sqlId,T entity)throws DBException{
			try{
					this.getSqlSession().insert(sqlId, entity);
			 }catch(Exception e){
				 throw new DBException("获取数据失败OrcaleBatisBaseDAO::insert(String sqlId,T entity)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity),e);
		     }
		}
		

		@Transactional(propagation=Propagation.REQUIRED)
		public void delete(T entity)throws DBException{
			String sqlId = "";
			try{
				sqlId = getSqlId(SQL.DELETE);
				this.getSqlSession().delete(sqlId, entity);
			 }catch(Exception e){
				 throw new DBException("获取数据失败OrcaleBatisBaseDAO::delete(T entity)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity),e);
		     }
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public void delete(String sqlId ,T entity)throws DBException{
			try{
				this.getSqlSession().delete(sqlId, entity);
			 }catch(Exception e){
				 throw new DBException("获取数据失败OrcaleBatisBaseDAO::delete(String sqlId ,T entity)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity),e);
		     }
		}
		

		@Transactional(propagation=Propagation.REQUIRED)
		public void update(T entity)throws DBException{
			String sqlId = "";
			try{
				sqlId = getSqlId(SQL.UPDATE);
				this.getSqlSession().update(sqlId, entity);	
			 }catch(Exception e){
				 throw new DBException("获取数据失败OrcaleBatisBaseDAO::update(T entity)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity),e);
		     }
		}
		@Transactional(propagation=Propagation.REQUIRED)
		public void update(String sqlId,T entity)throws DBException{
			try{
				this.getSqlSession().update(sqlId, entity);	
			 }catch(Exception e){
				 throw new DBException("获取数据失败OrcaleBatisBaseDAO::update(String sqlId,T entity)::sqlId="+sqlId+";entity="+SerializeUtil.bean2Map(entity),e);
		     }
		}
	
		
		

		

}
