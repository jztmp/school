package cn.com.bettle.code.appcontext.context;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

public interface FileListener {
	
	
	
	public  void onDirectoryChange(File directory);


	public  void onDirectoryCreate(File directory);


	public  void onDirectoryDelete(File directory);


	public  void onFileChange(File file);


	public  void onFileCreate(File file);


	public  void onFileDelete(File file);

	
	public  void onStart(FileAlterationObserver observer);


	public  void onStop(FileAlterationObserver observer);
	
	public  void startListener(String filePath);
	
	public void startListener(String filePath,long second);

	public String getNameSpace();

	public void setNameSpace(String nameSpace);
}

