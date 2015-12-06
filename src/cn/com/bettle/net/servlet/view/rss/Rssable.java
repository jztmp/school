package cn.com.bettle.net.servlet.view.rss;

import java.util.Date;

/**
 * 定义一个可订阅的接口供RssFeedView使用
 * 
 * @author zhangshuai
 * 
 */
public interface Rssable {

	public String getTitle();

	public String getUrl();

	public String getSummary();

	public Date getCreateDate();

}
