package cn.com.bettle.code.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;

public interface IService {

	public String getServiceName() throws Exception;

	public void initSqlMapDAO() throws Exception;
	
	public ISqlMapDAO getSqlMapDAO(String key) throws Exception;
	
	public Object getServiceData()throws Exception;
	
	public Object getServiceInData()throws Exception;
	
	public Object getServiceOutData()throws Exception;
	
	public void setServiceInData(Object data)throws Exception;
	
	public void setServiceOutData(Object data)throws Exception;

	public BettleApplicationContext getBettleApplicationContext();

	public void setBettleApplicationContext(BettleApplicationContext bettleApplicationContext);

}
