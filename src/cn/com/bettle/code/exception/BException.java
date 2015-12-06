package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class BException extends Exception{

	private static final long serialVersionUID = 400L;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
    /**
     * Constructs a new exception with <code>null</code> as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BException() {
       super();
    }
    /**
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public BException(String message) {
       super(message);
       logger.error(message);
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
    public BException(String message, Throwable cause) {
       super(message, cause);
       logger.error(cause.getClass().toString()+":::"+message+"{"+cause.getStackTrace().toString()+"}");
    }
    /**
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     * @since 1.4
     */
    public BException(Throwable cause) {
       super(cause);
       logger.error(cause.getClass().toString()+":::"+cause.getLocalizedMessage()+"{"+cause.getStackTrace().toString()+"}");
    }
    

    
}
