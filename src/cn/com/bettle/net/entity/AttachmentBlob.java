package cn.com.bettle.net.entity;

public class AttachmentBlob {

	private String attachmentId;
	private byte[] attachment;

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setContent(byte[] attachment) {
		this.attachment = attachment;
	}

}
