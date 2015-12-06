package cn.com.bettle.code.appcontext.context;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;

import cn.com.bettle.code.exception.ConfigParserException;



public class AppListener extends FileListenerAbstract{
	



	private String nameSpace = "application_context";



	@Override
	public void onFileChange(File file) {
		logger.info("文件变更了:"+file.getAbsolutePath());
		try {
			AppContext appContext = AppContext.getInstance();
			appContext.parser(this.nameSpace,file.getAbsolutePath());
		} catch (ConfigParserException e) {
			logger.error(e.getMessage());
		} catch (BeansException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		
	}

}

