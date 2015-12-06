package cn.com.bettle.code.appcontext.context;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import cn.com.bettle.code.exception.ConfigParserException;



public class PropertieListener extends FileListenerAbstract{



	@Override
	public void onFileChange(File file) {
		logger.info("文件变更了:"+file.getAbsolutePath());
		try {
			PropertieContext propertieContext = new PropertieContext();
			propertieContext.parser(this.nameSpace,file.getAbsolutePath());
		} catch (ConfigParserException e) {
			logger.error(e.getMessage());
		}
	}

	
}
