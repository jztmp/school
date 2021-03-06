package cn.com.bettle.code.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BettleHandleBeforException extends BException{

	
    private static final long serialVersionUID = 4052L;
    
    /**
     * Constructs a new exception with <code>null</code> as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BettleHandleBeforException() {
       super();
    }
    /**
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public BettleHandleBeforException(String message) {
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
    public BettleHandleBeforException(String message, Throwable cause) {
       super(message, cause );
    }
    /**
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     * @since 1.4
     */
    public BettleHandleBeforException(Throwable cause) {
       super(cause);
    }
}