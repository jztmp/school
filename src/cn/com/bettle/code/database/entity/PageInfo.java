package cn.com.bettle.code.database.entity;

import java.util.List;

/** 
 *
 * <p>分页工具类</p>
 * @author		John  <br/>
 * <b>E-mail</b> : zxmapple@gmail.com
 * @version		0.1
 */
public class PageInfo<T> {
	private long total;
	private List<T> rows;
	private int pageNum;
	private int pageSize;
	
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
