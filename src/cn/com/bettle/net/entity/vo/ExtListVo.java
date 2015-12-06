package cn.com.bettle.net.entity.vo;

import java.util.List;

/**
 * 分页器
 * 
 * @author
 * 
 * @param <T>
 */
public class ExtListVo<T> {
	/**
	 * 总数
	 */
	private int total;
	/**
	 * 数据
	 */
	private List<T> items;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
