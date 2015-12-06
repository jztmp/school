package cn.com.map.init;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MapException extends Exception {


    private static final long serialVersionUID = 1439142487437643116L;
    
    private static final Log log = LogFactory.getLog(MapException.class);

	public MapException() {
		super();
	}

	
	    
	    
	    
	    /**
	     * @param message
	     *            the detail message. The detail message is saved for later
	     *            retrieval by the {@link #getMessage()} method.
	     */
	    public MapException(String message) {
	       super(message);
	    }
	    /**
	     * @param message
	     *            the detail message (which is saved for later retrieval by the
	     *            {@link #getMessage()} method).
	     * @param cause
	     *            the cause (which is saved for later retrieval by the
	     *            {@link #getCause()} method). (A <tt>null</tt> value is
	     *            permitted, and indicates that the cause is nonexistent or
	     *            unknown.)
	     * @since 1.4
	     */
	    public MapException(String message, Throwable cause) {
	       super(message, cause);
	    }
	    /**
	     * @param cause
	     *            the cause (which is saved for later retrieval by the
	     *            {@link #getCause()} method). (A <tt>null</tt> value is
	     *            permitted, and indicates that the cause is nonexistent or
	     *            unknown.)
	     * @since 1.4
	     */
	    public MapException(Throwable cause) {
	       super(cause);
	    }
	}
