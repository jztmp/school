package cn.com.bettle.net.entity.vo;

import cn.com.bettle.net.entity.User;

/**
 * 
 * 系统用户实体类
 * 
 * @author zhangshuai
 * 
 */
public class UserVo extends User {

	/**
	 * 邮箱地址
	 */
	private String email;
	/**
	 * 手机号码
	 */
	private String tel;
	/**
	 * 固定电话
	 */
	private String fixTel;
	/**
	 * 短号码
	 */
	private String shortTel;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFixTel() {
		return fixTel;
	}

	public void setFixTel(String fixTel) {
		this.fixTel = fixTel;
	}

	public String getShortTel() {
		return shortTel;
	}

	public void setShortTel(String shortTel) {
		this.shortTel = shortTel;
	}

}
