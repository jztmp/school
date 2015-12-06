package cn.com.bettle.net.exception;

/**
 * 用户名已存在异常
 * 
 * @author zhangshuai
 * 
 */
public class UserNameAlreadyExitsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNameAlreadyExitsException() {
		super("the user name already exists");
	}

	public UserNameAlreadyExitsException(String message) {
		super(message);
	}

}
