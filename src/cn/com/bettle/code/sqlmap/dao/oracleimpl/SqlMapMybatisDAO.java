package cn.com.bettle.code.sqlmap.dao.oracleimpl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.database.sql.mybatis.OrcaleEntityDAO;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;
import cn.com.bettle.code.utils.serialize.SerializeUtil;

public class SqlMapMybatisDAO extends SqlSessionDaoSupport  implements ISqlMapDAO{
	
	private static Logger logger = Logger.getLogger(SqlMapMybatisDAO.class);
	
	
	@Transactional(readOnly=true)
	public List<Map<String,Object>> loadList(String sqlId,Map params)throws RuntimeException{
		try{
			return this.getSqlSession().selectList(sqlId, params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	@Transactional(readOnly=true)
	public Map<String,Object> loadOne(String sqlId,Map params)throws RuntimeException{
		try{
			 return (Map<String,Object>)this.getSqlSession().selectOne(sqlId, params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}	

    protected long getLastIndex(long start, long limit, long total) {
		if (total < (start + limit))
			return total;
		else
			return start + limit;
	}
	
    protected List<Map<String,Object>> pageQuery(String sqlId ,Map params,long total,int pageNum,int pageSize){
    	long start = pageNum;
    	long limit = pageSize;
		long last = this.getLastIndex(start, limit, total);
	      params.put("pageStart", start);
	      params.put("pageLimit", limit);
		return this.getSqlSession().selectList(sqlId, params);
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insert(String sqlId,Map params)throws RuntimeException{
		try{
			return this.getSqlSession().insert(sqlId, params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(String sqlId,Map params)throws RuntimeException{
		try{
			return this.getSqlSession().delete(sqlId, params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int  update(String sqlId,Map params)throws RuntimeException{
		try{
			return this.getSqlSession().update(sqlId, params);	
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 返回符合分页查询条件的记录总数
	 */
	@Transactional(readOnly=true)
	protected long getRowCount(String sqlId ,Object params)throws RuntimeException{
		try{
			return (Long)this.getSqlSession().selectOne(sqlId, params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(readOnly=true)
    public PageInfo<Map<String,Object>> loadPageInfo(String sqlId, Map params,int pageNum,int pageSize)throws RuntimeException{
		try{
		PageInfo<Map<String,Object>> pageInfo;
			pageInfo  = new PageInfo<Map<String,Object>>();
			long total = this.getRowCount(sqlId+"_COUNT_", params);
			List<Map<String,Object>> list = this.pageQuery(sqlId+"_PAGE_", params,total,pageNum,pageSize);
			pageInfo.setPageNum(pageNum);
			pageInfo.setRows(list);
			pageInfo.setPageSize(pageSize);
			pageInfo.setTotal(total);
			return pageInfo;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
    }	
}
