package cn.com.bettle.net.servlet.view.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;

/**
 * 定义rss视图
 * 
 * @author Administrator
 * 
 */
public class RssFeedView extends AbstractRssFeedView {

	public static String RSS_FEED_CONTENT = "feedContent";

	@SuppressWarnings("unchecked")
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object obj = model.get(RssFeedView.RSS_FEED_CONTENT);
		if (obj == null) {
			throw new RssFeedNotFoundException();
		}
		// 这里使用强制数据类型转换，这个视图就是需要Rssable的list对象
		List<Rssable> contentList = (List<Rssable>) obj;
		List<Item> items = new ArrayList<Item>(contentList.size());
		for (Rssable tempContent : contentList) {
			Item item = new Item();
			Content content = new Content();
			content.setValue(tempContent.getSummary());
			item.setContent(content);
			item.setTitle(tempContent.getTitle());
			item.setLink(tempContent.getUrl());
			item.setPubDate(tempContent.getCreateDate());
			items.add(item);
		}
		return items;
	}

}
