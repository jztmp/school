package cn.com.bettle.net.exception;

public class CreateServiceException extends RuntimeException {

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
	public CreateServiceException(String name) {
		super("构建" + name + "服务时出错");
	}

}
