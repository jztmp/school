package cn.com.bettle.code.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bettle.code.exception.SqlMapException;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;
import cn.com.bettle.code.utils.tools.BasicUitl;


@Service("SQL_SERVICE")
public class SqlService extends CService implements ISqlService{



	@Transactional
	public Object sqlMap(LinkedHashMap inp)throws Exception{

			this.initSqlMapDAO();
			Map obj = (Map)inp;
			HttpServletRequest request = (HttpServletRequest)obj.get("request");
			if(!obj.containsKey("sqlId")){
				throw new SqlMapException("SQL操作没有包含sqlId参数");
			}
			if(!obj.containsKey("sqlActive")){
				throw new SqlMapException("SQL操作没有包含sqlActive参数");
			}
			if(!obj.containsKey("daoName")){
				throw new SqlMapException("SQL操作没有包含daoName参数");
			}
			if(!obj.containsKey("data")){
				throw new SqlMapException("SQL操作没有包含data参数");
			}
		
			String sqlId  = (String)obj.get("sqlId");
			sqlId = sqlId.replaceAll("-",".");
			String sqlActiv = (String)obj.get("sqlActive");
			String daoName = (String)obj.get("daoName");
			Map data = (Map)obj.get("data");
			
			if(obj.containsKey("type")){
				String type  = (String)obj.get("type");
				if(type.equals("ExtComboBox")){
					String queryValue = BasicUitl.transformNull(request.getParameter("COMBOBOX_QUERY"),"");
					data.put("COMBOBOX_QUERY", queryValue);
					
				}
			}
			
			ISqlMapDAO sqlMapDAO = this.getSqlMapDAO(daoName);
			
			if(sqlActiv.equals("delete"))
				return sqlMapDAO.delete(sqlId, data);
			else if(sqlActiv.equals("insert"))
				return sqlMapDAO.insert(sqlId, data);
			else if(sqlActiv.equals("update"))
				return sqlMapDAO.update(sqlId, data);
			else if(sqlActiv.equals("loadPageInfo")){
				int pageNum = 0;
				int pageSize = 0;	
				if(!obj.containsKey("start")){
					String start = BasicUitl.transformNull(request.getParameter("start"),"");
					if(start.length()==0){
						throw new SqlMapException("分页查询没有包含start参数");
					}else{
						pageNum = Integer.parseInt(start);
					}
				}else{
					Double pageNum1 = (Double)obj.get("start");
					pageNum = Integer.parseInt(new java.text.DecimalFormat("0").format(pageNum1));    
				}
				if(!obj.containsKey("limit")){
					String limit = BasicUitl.transformNull(request.getParameter("limit"),"");
					if(limit.length()==0){
						throw new SqlMapException("分页查询没有包含limit参数");
					}else{
						pageSize = Integer.parseInt(limit);
					}
				}else{
					Double pageSize1 = (Double)obj.get("limit");
					pageSize = Integer.parseInt(new java.text.DecimalFormat("0").format(pageSize1));  
				}

				return sqlMapDAO.loadPageInfo(sqlId, data,pageNum, pageSize);
			}else if(sqlActiv.equals("loadOne"))
				return sqlMapDAO.loadOne(sqlId, data);
			else if(sqlActiv.equals("loadList"))
				return sqlMapDAO.loadList(sqlId, data);
		
		return null;

	}





}
