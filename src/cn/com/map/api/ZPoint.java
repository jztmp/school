package cn.com.map.api;

import cn.com.bettle.code.utils.json.JsonGsonSerializeUtil;

public class ZPoint {
	private double  x;
	private double  y;
	private double length;
	private double lat;
	private double lon;
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	 public String toString() {
		    return JsonGsonSerializeUtil.bean2Json(this);
	 }
} 
