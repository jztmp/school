package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServicePluginException extends BException{
	
	
		private static final long serialVersionUID = 411L;
	
	    private static final Log log = LogFactory.getLog(ServicePluginException.class); 
	
	  	  public ServicePluginException() {
		    super();
		  }

		  public ServicePluginException(String message) {
		    super(message);
		  }

		  public ServicePluginException(String message, Throwable cause) {
		    super(message, cause);
		  }

		  public ServicePluginException(Throwable cause) {
		    super(cause);
		  }
}
