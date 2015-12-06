package cn.com.bettle.net.servlet.view.rss;

import java.util.Date;

/**
 * Rssable接口的简单实现程序中可直接该类创建一个RSS订阅对象
 * 
 * @author zhangshuai
 * 
 */
public class SampleRssFeed implements Rssable {
	private String title;
	private String url;
	/**
	 * 摘要
	 */
	private String summary;
	private Date createDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
