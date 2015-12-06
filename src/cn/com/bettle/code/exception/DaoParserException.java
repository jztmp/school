package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DaoParserException  extends BException{

	
    private static final long serialVersionUID = 403L;
    
    private static final Log log = LogFactory.getLog(DaoParserException.class); 
    
    /**
     * Constructs a new exception with <code>null</code> as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
	  public DaoParserException() {
	    super();
	  }

	  public DaoParserException(String message) {
	    super(message);
	  }

	  public DaoParserException(String message, Throwable cause) {
	    super(message, cause);
	  }

	  public DaoParserException(Throwable cause) {
	    super(cause);
	  }
}
