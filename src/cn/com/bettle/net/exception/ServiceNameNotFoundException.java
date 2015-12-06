package cn.com.bettle.net.exception;

/**
 * 用户名已存在异常
 * 
 * @author zhangshuai
 * 
 */
public class ServiceNameNotFoundException extends NullPointerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceNameNotFoundException() {
		super("The service name not found");
	}

	public ServiceNameNotFoundException(String message) {
		super(message);
	}

}
