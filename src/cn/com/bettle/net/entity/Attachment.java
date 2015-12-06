package cn.com.bettle.net.entity;

import java.util.Date;

import cn.com.bettle.net.constant.AttachmentConstant;

public class Attachment {

	private String attachmentId;
	private String kindId;
	private String name;
	private String path;
	private String description;
	private long fileSize;
	private AttachmentConstant.Type type;
	private Date createTime;
	private AttachmentConstant.Kind kind;
	private AttachmentConstant.Status status;

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getKindId() {
		return kindId;
	}

	public void setKindId(String kindId) {
		this.kindId = kindId;
	}

	public AttachmentConstant.Kind getKind() {
		return kind;
	}

	public void setKind(AttachmentConstant.Kind kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public AttachmentConstant.Type getType() {
		return type;
	}

	public void setType(AttachmentConstant.Type type) {
		this.type = type;
	}

	public AttachmentConstant.Status getStatus() {
		return status;
	}

	public void setStatus(AttachmentConstant.Status status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
