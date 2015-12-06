package cn.com.bettle.net.entity;

import java.util.Date;

public class Device {

	private String deviceId;
	private String userId;
	private String IMEI;
	private String IMSI;
	// 推送的通道
	private String channelId;
	// 推送的客户端ID
	private String clientUserId;
	// 类型
	private DeviceType type;
	private String status;
	private Date createTime;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getClientUserId() {
		return clientUserId;
	}

	public void setClientUserId(String clientUserId) {
		this.clientUserId = clientUserId;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static enum DeviceType {
		A("android"), I("ios");

		private String value;

		private DeviceType(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	};

}
