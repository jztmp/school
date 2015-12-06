package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

public class NDBException extends DataAccessException{

	
    private static final long serialVersionUID = 407L;
    
    private static final Log log = LogFactory.getLog(NDBException.class); 

    /**
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public NDBException(String message) {
       super(message);
       log.error(message);
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
    public NDBException(String message, Throwable cause) {
       super(message, cause);
       log.error(cause.getClass().toString()+":::"+message+"{"+cause.getStackTrace().toString()+"}");
    }

}
