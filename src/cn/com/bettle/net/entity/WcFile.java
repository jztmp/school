package cn.com.bettle.net.entity;

// default package

import java.sql.Timestamp;

/**
 * WcFile entity. @author MyEclipse Persistence Tools
 */

public class WcFile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields

	private String fileId;
	private String shortPath;
	private String fullPath;
	private Timestamp createDate;
	private WcUser uploadUser;
	private Integer fileType;

	// Constructors

	/** default constructor */
	public WcFile() {
	}

	/** minimal constructor */
	public WcFile(String fileId) {
		this.fileId = fileId;
	}

	/** full constructor */
	public WcFile(String fileId, String shortPath, String fullPath, Timestamp createDate, WcUser uploadUser,
			Integer fileType) {
		this.fileId = fileId;
		this.shortPath = shortPath;
		this.fullPath = fullPath;
		this.createDate = createDate;
		this.uploadUser = uploadUser;
		this.fileType = fileType;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getShortPath() {
		return this.shortPath;
	}

	public void setShortPath(String shortPath) {
		this.shortPath = shortPath;
	}

	public String getFullPath() {
		return this.fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public WcUser getUploadUser() {
		return this.uploadUser;
	}

	public void setUploadUser(WcUser uploadUser) {
		this.uploadUser = uploadUser;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

}