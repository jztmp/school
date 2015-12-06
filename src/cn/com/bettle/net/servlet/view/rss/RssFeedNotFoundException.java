package cn.com.bettle.net.servlet.view.rss;

/**
 * RSS数据供给对象没有被发现
 * 
 * @author Administrator
 * 
 */
public class RssFeedNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RssFeedNotFoundException() {
		super("RSS数据供给对象没有被发现");
	}

	public RssFeedNotFoundException(String message) {
		super(message);
	}

}
