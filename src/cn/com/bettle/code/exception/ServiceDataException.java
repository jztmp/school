package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceDataException extends BException{
	
	
		private static final long serialVersionUID = 409L;
	
	    private static final Log log = LogFactory.getLog(ServiceDataException.class); 
	
	  	  public ServiceDataException() {
		    super();
		  }

		  public ServiceDataException(String message) {
		    super(message);
		  }

		  public ServiceDataException(String message, Throwable cause) {
		    super(message, cause);
		  }

		  public ServiceDataException(Throwable cause) {
		    super(cause);
		  }
}
