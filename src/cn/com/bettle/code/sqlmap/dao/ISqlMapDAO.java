package cn.com.bettle.code.sqlmap.dao;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.exception.DBException;

public interface ISqlMapDAO {


	public List<Map<String,Object>> loadList(String sqlId,Map params)throws RuntimeException;
	

	public Map<String,Object> loadOne(String sqlId,Map params)throws RuntimeException;


    public PageInfo<Map<String,Object>> loadPageInfo(String sqlId, Map params,int pageNum,int pageSize)throws RuntimeException;
	
	
	public int insert(String sqlId,Map params)throws RuntimeException;
	

	public int delete(String sqlId,Map params)throws RuntimeException;

	public int update(String sqlId,Map params)throws RuntimeException;
	
	
}
