package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceException extends BException{
	
	
		private static final long serialVersionUID = 410L;
	
	    private static final Log log = LogFactory.getLog(ServiceException.class); 
	
	  	  public ServiceException() {
		    super();
		  }

		  public ServiceException(String message) {
		    super(message);
		  }

		  public ServiceException(String message, Throwable cause) {
		    super(message, cause);
		  }

		  public ServiceException(Throwable cause) {
		    super(cause);
		  }
}
