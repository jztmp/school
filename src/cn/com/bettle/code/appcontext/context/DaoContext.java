package cn.com.bettle.code.appcontext.context;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.com.bettle.code.database.entity.DaoEntity;
import cn.com.bettle.code.exception.ConfigParserException;




/**
 * @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
 * @category
 * <b>功能：</b>解析dao.xml文件，将dao信息保存到内存中
 * */
public class DaoContext implements Parser{

	private static Logger logger = Logger.getLogger(DaoContext.class);
	
	@Override
	public void parser(String namespace, String filePath)
			throws ConfigParserException {
		
		File inputXml=new File(filePath);   
		SAXReader saxReader = new SAXReader();  
		try {   
				Document document = saxReader.read(inputXml);  
				List<Node> dblist = document.selectNodes("//items/database/db");
				List<Node> daolist = document.selectNodes("//items/daos/dao");
				Map<String,Map<String,String>> map = new HashMap();
				Iterator it = dblist.iterator();  
				while (it.hasNext()) { 
					 Map<String,String> map2 = new HashMap();
					 String name = "";
					 Element element = (Element) it.next();  
					 for ( Iterator i = element.attributeIterator(); i.hasNext(); ) {
					       Attribute attribute = (Attribute) i.next();
					       map2.put(attribute.getName(), attribute.getValue());
					       if(attribute.getName().equals("database")){
					    	   name = attribute.getValue();
					       }
					    }
					 map.put(name, map2);
				 }
				 
				
				Iterator it2 = daolist.iterator(); 
				while (it2.hasNext()) { 
					 Element element = (Element) it2.next();  
					 String key = element.attributeValue("database");
					 String name = element.attributeValue("name");
					 String sqlMap = element.attributeValue("sqlMap");
					 boolean isSqlMap = false;
					 if(!StringUtils.isEmpty(sqlMap)){
						 isSqlMap = true;
					 }
					 
					 DaoEntity daoEntity = new DaoEntity();
					 String dbtype = map.get(key).get("dbtype");
					 String technology = map.get(key).get("technology");
					 String dbid =  map.get(key).get("dbid");
					 daoEntity.setDbtype(dbtype);
					 daoEntity.setName(name);
					 daoEntity.setTechnology(technology);
					 daoEntity.setSqlMap(isSqlMap);
					 
					 if(dbid!=null && !StringUtils.isEmpty(dbid))
						 daoEntity.setDbid(dbid);
					 BettleApplicationContext.addDao(name,daoEntity);
				 }
				
			} catch (DocumentException e) {  
				 throw new ConfigParserException("解析xml文件"+filePath+"出错，"+e.getMessage(),e.getCause()); 
			}   
	}

 	public static void main(String[] args) {
		try{
			DaoContext daoContext = new DaoContext();
			daoContext.parser("dao", "WebRoot\\etc\\params\\dao.xml");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
