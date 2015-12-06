package cn.com.bettle.net.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * WcUser entity. @author MyEclipse Persistence Tools
 */

public class WcUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String userId;
	private String userName;
	private String userPassword;
	private String userNickname;
	private Timestamp userBirthday;
	private String userDescription;
	private WcFile userHead;
	private Timestamp registerDate = new Timestamp(System.currentTimeMillis());
	private Double longitude;
	private Double latitude;
	private String city;
	private String province;
	private Integer userSex;
	private Integer userAge;
	private String userQq;
	private String userPhone;
	private String apiKey;
	private Integer userState;

	private List<WcUser> friends;

	// Constructors

	/** default constructor */
	public WcUser() {
	}

	/** minimal constructor */
	public WcUser(String userId, String userName, String userPassword) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	/** full constructor */
	public WcUser(String userId, String userName, String userPassword, String userNickname,
			Timestamp userBirthday, String userDescription, WcFile userHead, Timestamp registerDate,
			Double longitude, Double latitude, String city, String province, Integer userSex,
			Integer userAge, String userQq, String userPhone, String apiKey, Integer userState) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userNickname = userNickname;
		this.userBirthday = userBirthday;
		this.userDescription = userDescription;
		this.userHead = userHead;
		this.registerDate = registerDate;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.province = province;
		this.userSex = userSex;
		this.userAge = userAge;
		this.userQq = userQq;
		this.userPhone = userPhone;
		this.apiKey = apiKey;
		this.userState = userState;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserNickname() {
		return this.userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public Timestamp getUserBirthday() {
		return this.userBirthday;
	}

	public void setUserBirthday(Timestamp userBirthday) {
		this.userBirthday = userBirthday;
	}

	public String getUserDescription() {
		return this.userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public WcFile getUserHead() {
		return this.userHead;
	}

	public void setUserHead(WcFile userHead) {
		this.userHead = userHead;
	}

	public Timestamp getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getUserSex() {
		return this.userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	public Integer getUserAge() {
		return this.userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public String getUserQq() {
		return this.userQq;
	}

	public void setUserQq(String userQq) {
		this.userQq = userQq;
	}

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Integer getUserState() {
		return this.userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public List<WcUser> getFriends() {
		return friends;
	}

	public void setFriends(List<WcUser> friends) {
		this.friends = friends;
	}

}