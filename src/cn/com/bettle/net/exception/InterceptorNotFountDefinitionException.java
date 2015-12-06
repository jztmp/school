package cn.com.bettle.net.exception;

public class InterceptorNotFountDefinitionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new {@code NoSuchServiceDefinitionException}.
	 * 
	 * @param name
	 *            the name of the missing service
	 */
	public InterceptorNotFountDefinitionException() {
		super("拦截器没有被找到");
	}

}
