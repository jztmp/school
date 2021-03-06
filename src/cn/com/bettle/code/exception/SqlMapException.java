package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

public class SqlMapException extends BException{

	
    private static final long serialVersionUID = 412L;
    
    private static final Log log = LogFactory.getLog(SqlMapException.class); 
    /**
     * Constructs a new exception with <code>null</code> as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */

    public SqlMapException() {
        super();
     }
    /**
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public SqlMapException(String message) {
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
    public SqlMapException(String message, Throwable cause) {
       super(message, cause);
    }

}
