package cn.com.bettle.code.appcontext.context;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import cn.com.bettle.code.utils.file.FileUtils;


/**
 * 
 * spring的工厂bean用来产生ConfigurationManager
 * */
public class BettleConfigurationFactoryBean extends AbstractFactoryBean<BettleApplicationContext>{

	
	private String configFilePath;    
	
	
	@Override
	public Class<?> getObjectType() {
		return BettleApplicationContext.class;
	}

	@Override
	protected BettleApplicationContext createInstance() throws Exception {

		BettleApplicationContext configurationManager = new BettleApplicationContext();
		configurationManager.init();
		return configurationManager;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	
}
