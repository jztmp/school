package cn.com.bettle.code.appcontext.context;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bettle.code.exception.ConfigParserException;




/**
 * @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
 * @category
 * <b>功能：</b>解析service-interceptor.xml文件，将ServiceInterceptor信息保存到内存中
 * */
public class BettleListenerContext implements Parser{

	private Logger logger = LoggerFactory.getLogger(BettleListenerContext.class);
	
	@Override
	public void parser(String namespace, String filePath)
			throws ConfigParserException {
		
		File inputXml=new File(filePath);   
		SAXReader saxReader = new SAXReader();  
		try {   
				Document document = saxReader.read(inputXml);  
				List<Node> interceptorlist = document.selectNodes("//bettle/listen");
				Iterator it = interceptorlist.iterator();  
				while (it.hasNext()) { 

					 Element itemNode = (Element) it.next();  
					 String event =  itemNode.attributeValue("event");
					 String listener =  itemNode.attributeValue("listener");
					 List<Node> propertylist = itemNode.selectNodes("property");
					 Iterator it2 = propertylist.iterator(); 
					 
					 Properties  properties = new Properties();
					 while (it2.hasNext()) { 
						 Element propertyNode = (Element) it2.next();  
						 String name = propertyNode.attributeValue("name");
						 String value = propertyNode.attributeValue("value");
						 properties.put(name, value);
					 }
					 BettleApplicationContext.addBettleListen(event,listener, properties);
				 }
			} catch (DocumentException e) {  
				 throw new ConfigParserException("解析xml文件"+filePath+"出错，"+e.getMessage(),e.getCause()); 
			}   
	}

}
