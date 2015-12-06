package cn.com.bettle.code.appcontext.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import cn.com.bettle.code.exception.ConfigParserException;



public class PropertieContext implements Parser {

	@Override
	public void parser(String namespace,String filePath)throws ConfigParserException {
		// TODO Auto-generated method stub
	 try {
        // 生成文件对象  
        File pf = new File(filePath);  
        // 生成文件输入流  
        FileInputStream inpf = new FileInputStream(pf);  
        // 生成properties对象  
        Properties p = new Properties(); 

		p.load(inpf);
		
		BettleApplicationContext.removePropertieContext(namespace);
		BettleApplicationContext.addPropertieContext(namespace, p);
		} catch (IOException e) {
			throw new ConfigParserException("解析Propertie文件"+filePath+"出现输入输出异常,"+e.getMessage(),e.getCause());
		}  
		
	}

}
