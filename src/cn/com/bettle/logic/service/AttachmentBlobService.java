package cn.com.bettle.logic.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.com.bettle.logic.mapper.AttachmentBlobDao;
import cn.com.bettle.net.entity.Attachment;
import cn.com.bettle.net.entity.AttachmentBlob;

@Service
public class AttachmentBlobService {

	protected final Logger log = LoggerFactory.getLogger(AttachmentBlobService.class);

	@Autowired
	private AttachmentBlobDao attachmentBlobDao;

	/**
	 * 上传附件
	 * 
	 * @param attachmentId
	 * @param multipartFile
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws UploadException
	 */
	public void addUploadFile(String attachmentId, MultipartFile multipartFile) throws IOException,
			IllegalStateException {
		addUploadFile(attachmentId, multipartFile.getBytes());
	}

	/**
	 * 上传附件
	 * 
	 * @param attachment
	 * @param fileBytes
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws UploadException
	 */
	public void addUploadFile(String attachmentId, byte[] fileBytes) throws IllegalStateException, IOException {
		AttachmentBlob attachmentBlob = new AttachmentBlob();
		attachmentBlob.setAttachmentId(attachmentId);
		attachmentBlob.setContent(fileBytes);
		addUploadFile(attachmentBlob);
	}

	/**
	 * 上传附件
	 * 
	 * @param attachmentBlob
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws UploadException
	 */
	public void addUploadFile(AttachmentBlob attachmentBlob) throws IllegalStateException, IOException {
		int number = attachmentBlobDao.addAttachmentBlob(attachmentBlob);
		if (log.isDebugEnabled())
			log.debug("保存完成，共保存数据：" + number + "条");
	}

	/**
	 * 
	 * 删除附件通过附件ID
	 * 
	 * @param attachmentId
	 * @param path
	 */
	public void deleteAttachment(String attachmentId, String path) {
		attachmentBlobDao.deleteAttachmentBlob(attachmentId);
	}

	public List<AttachmentBlob> getAttachmentBlobList(int pageNum, int rows, String attachmentId) {
		int offset = (pageNum - 1) * rows;
		int endRowNum = offset + rows;
		List<AttachmentBlob> list = attachmentBlobDao.getAttachmentBlobList(offset, endRowNum, attachmentId);
		return list;
	}

	/**
	 * 
	 * 根据id获得附件内容
	 * 
	 * @param attachmentId
	 * @return
	 */
	public AttachmentBlob getAttachmentBlobById(String attachmentId) {
		return attachmentBlobDao.getAttachmentBlobById(attachmentId);
	}

}
