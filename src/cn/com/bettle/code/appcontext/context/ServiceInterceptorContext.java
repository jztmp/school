package cn.com.bettle.code.appcontext.context;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.com.bettle.code.exception.ConfigParserException;




/**
 * @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
 * @category
 * <b>功能：</b>解析service-interceptor.xml文件，将ServiceInterceptor信息保存到内存中
 * */
public class ServiceInterceptorContext implements Parser{

	private static Logger logger = Logger.getLogger(ServiceInterceptorContext.class);
	
	@Override
	public void parser(String namespace, String filePath)
			throws ConfigParserException {
		
		File inputXml=new File(filePath);   
		SAXReader saxReader = new SAXReader();  
		try {   
				Document document = saxReader.read(inputXml);  
				List<Node> interceptorlist = document.selectNodes("//interceptors/item");
				Iterator it = interceptorlist.iterator();  
				while (it.hasNext()) { 

					 Element itemNode = (Element) it.next();  
					 String interceptor =  itemNode.attributeValue("interceptor");
					 String sevice =  itemNode.attributeValue("service");
					 List<Node> propertylist = itemNode.selectNodes("property");
					 Iterator it2 = propertylist.iterator(); 
					 Map<String, Properties> map = new HashMap();
					 Properties  properties = new Properties();
					 while (it2.hasNext()) { 
						 Element propertyNode = (Element) it2.next();  
						 String name = propertyNode.attributeValue("name");
						 String value = propertyNode.attributeValue("value");
						 properties.put(name, value);
					 }
					 map.put(interceptor, properties);
					 BettleApplicationContext.addServiceInterceptor(sevice, map);
				 }
			} catch (DocumentException e) {  
				 throw new ConfigParserException("解析xml文件"+filePath+"出错，"+e.getMessage(),e.getCause()); 
			}   
	}

}
