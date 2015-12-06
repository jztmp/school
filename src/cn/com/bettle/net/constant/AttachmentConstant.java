package cn.com.bettle.net.constant;

public class AttachmentConstant {

	/**
	 * 类型<br>
	 * photo：照片<br>
	 * file：文件<br>
	 * 
	 * @author 
	 * 
	 */
	public static enum Type {
		/**
		 * 相册
		 */
		photo, /**
		 * 文件
		 */
		file,
		/**
		 * 视频
		 */
		video,
		/**
		 * 其他类型
		 */
		other
	}

	/**
	 * 种类
	 * 
	 * @author 
	 * 
	 */
	public static enum Kind {
		message
	}

	public static enum Status {
		/**
		 * 可用状态
		 */
		A, /**
		 * 不可用状态
		 */
		X
	}
}
