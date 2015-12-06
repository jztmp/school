package cn.com.bettle.code.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.appcontext.factory.DaoFactory;
import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.database.entity.DaoEntity;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;

public abstract class CService {

	private static final Log log = LogFactory.getLog(CService.class);

	protected String seriveName;

	protected ThreadLocal<Hashtable<String,ISqlMapDAO>> sqlMapDAOMap = new ThreadLocal<Hashtable<String,ISqlMapDAO>>();
	
	
	protected ThreadLocal<BServiceData> serviceData = new ThreadLocal<BServiceData>();  //默认服务室单列，所以装有数据的属性为本地变量
	
	protected BettleApplicationContext bettleApplicationContext;
	

	public Object getServiceData()throws Exception{
		 this.getServiceName();
		 BServiceData _serviceData = serviceData.get();
		 return  _serviceData.getParameters(this.seriveName);
	}

	public Object getServiceInData()throws Exception{
		 this.getServiceName();
		 BServiceData _serviceData = serviceData.get();
		 return  _serviceData.getInParameters(this.seriveName);
	}
	
	
	public void setServiceOutData(Object data)throws Exception{
		 this.getServiceName();
		 BServiceData _serviceData = serviceData.get();
		 _serviceData.setInParameters(seriveName, data);
	}

	public void setServiceInData(Object data)throws Exception{
		 this.getServiceName();
		 BServiceData _serviceData = serviceData.get();
		 _serviceData.setInParameters(seriveName, data);
	}
	
	public Object getServiceOutData()throws Exception{
		this.getServiceName();
		BServiceData _serviceData = serviceData.get();
		return  _serviceData.getOutParameters(this.seriveName);
		
	} 
	
	public String getServiceName() throws Exception {
			if (seriveName == null) {
				if (this.getClass().isAnnotationPresent(Service.class)) {
					Service serviceAnnotation = (Service) this.getClass().getAnnotation(Service.class);
					seriveName = serviceAnnotation.value();
				}
			}
			return seriveName;
	}


	public ISqlMapDAO getSqlMapDAO(String key) throws ServiceException {
			return sqlMapDAOMap.get().get(key);
	}
	
	public void initSqlMapDAO() throws Exception {
			if (sqlMapDAOMap.get()==null || sqlMapDAOMap.get().isEmpty()) {
				sqlMapDAOMap.set(BettleApplicationContext.getSqlDaoSet());
			}
	}

	public BettleApplicationContext getBettleApplicationContext() {
		return bettleApplicationContext;
	}

	public void setBettleApplicationContext(BettleApplicationContext bettleApplicationContext) {
		this.bettleApplicationContext = bettleApplicationContext;;
	}
	
	
}
