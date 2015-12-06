package cn.com.bettle.net.exception;

public class BettleExceptionBean {

	private boolean success = false;

	private String name;

	private String simpleName;

	private String message;

	private String detail;

	public BettleExceptionBean(Exception ex) {
		this.initInfo(ex, false);
	}

	public BettleExceptionBean(Exception ex, boolean debug) {
		this.initInfo(ex, debug);
	}

	private void initInfo(Exception ex, boolean debug) {
		Class<? extends Exception> clazz = ex.getClass();
		this.name = clazz.getName();
		this.simpleName = clazz.getSimpleName();
		this.message = ex.getMessage();
		if (debug)
			this.detail = this.getExceptionDetail(ex);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * 获取exception详情信息
	 * 
	 * @param e
	 *            Excetipn type
	 * @return String type
	 */
	protected String getExceptionDetail(Exception e) {
		StringBuffer msg = new StringBuffer("null");
		if (e != null) {
			msg = new StringBuffer("");
			String message = e.toString();
			int length = e.getStackTrace().length;
			if (length > 0) {
				msg.append(message + "\n");
				for (int i = 0; i < length; i++) {
					msg.append("\t" + e.getStackTrace()[i] + "\n");
				}
			} else {
				msg.append(message);
			}
		}
		return msg.toString();
	}

}
