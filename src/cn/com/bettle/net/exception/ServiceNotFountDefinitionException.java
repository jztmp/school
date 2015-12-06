package cn.com.bettle.net.exception;

public class ServiceNotFountDefinitionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serviceName;

	/**
	 * Create a new {@code NoSuchServiceDefinitionException}.
	 * 
	 * @param name
	 *            the name of the missing service
	 */
	public ServiceNotFountDefinitionException(String name) {
		super("服务名为‘" + name + "’的服务不存在");
		this.serviceName = name;
	}

	public String getServiceName() {
		return serviceName;
	}

}
