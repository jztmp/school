package cn.com.bettle.code.appcontext.context;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import cn.com.bettle.code.exception.ConfigParserException;

public abstract class FileListenerAbstract extends FileAlterationListenerAdaptor implements FileListener{

	protected static Logger logger = Logger.getLogger(FileListenerAbstract.class);
	
	protected String nameSpace = "";

	@Override
	public void onDirectoryChange(File directory) {
		logger.info("文件目录变更了:"+directory.getAbsolutePath());
	}

	@Override
	public void onDirectoryCreate(File directory) {
		logger.info("文件目录创建了:"+directory.getAbsolutePath());
	}

	@Override
	public void onDirectoryDelete(File directory) {
		logger.info("文件目录删除了:"+directory.getAbsolutePath());
	}

	@Override
	public abstract void onFileChange(File file);

	@Override
	public void onFileCreate(File file) {
		logger.info("文件创建了:"+file.getAbsolutePath());
	}

	@Override
	public void onFileDelete(File file) {
		logger.info("文件删除了:"+file.getAbsolutePath());
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
		logger.info("开始监听:"+observer.getDirectory());
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		logger.info("停止监听:"+observer.getDirectory());
	}

	

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return this.nameSpace;
	}

	@Override
	public void setNameSpace(String nameSpace) {
		// TODO Auto-generated method stub
		this.nameSpace = nameSpace;
		
	}
	
	@Override
	public void startListener(String filePath) {
		FileObserver ob = new FileObserver(filePath);
		ob.addListener(this);
		FileMonitor monitor = new FileMonitor(ob);
		monitor.start();
	}
	
	@Override
	public void startListener(String filePath,long second) {
		FileObserver ob = new FileObserver(filePath);
		ob.addListener(this);
		FileMonitor monitor = new FileMonitor(second,ob);
		monitor.start();
	}
	
}
