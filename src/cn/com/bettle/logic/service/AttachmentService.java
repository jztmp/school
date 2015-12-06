package cn.com.bettle.logic.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.com.bettle.logic.mapper.AttachmentDao;
import cn.com.bettle.net.constant.AttachmentConstant;
import cn.com.bettle.net.constant.SystemConstant;
import cn.com.bettle.net.controller.ApiUploadController;
import cn.com.bettle.net.entity.Attachment;
import cn.com.bettle.net.entity.AttachmentBlob;
import cn.com.bettle.net.utils.UploadUtils;

@Service
public class AttachmentService {

	protected final Logger log = LoggerFactory.getLogger(ApiUploadController.class);

	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private AttachmentBlobService attachmentBlobService;

	public static String[] VIDEO_TYPE = { "flv" };

	/**
	 * 上传附件
	 * 
	 * @param multipartFile
	 * @param fileName
	 * @param kindId
	 * @param kind
	 * @param status
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws UploadException
	 */
	@Transactional
	public Attachment addUploadFile(MultipartFile multipartFile, String fileName, String kindId,
			AttachmentConstant.Kind kind, AttachmentConstant.Status status) {
		AttachmentConstant.Type type = AttachmentConstant.Type.photo;
		if (UploadUtils.isFileType(fileName, UploadUtils.VIDEO_TYPE)) {
			// 视频
			type = AttachmentConstant.Type.video;
		} else if (UploadUtils.isFileType(fileName, UploadUtils.PHOTO_TYPE)) {
			// 图片
			type = AttachmentConstant.Type.photo;
		} else if (UploadUtils.isFileType(fileName, UploadUtils.FILE_TYPE)) {
			// 文件
			type = AttachmentConstant.Type.file;
		} else {
			type = AttachmentConstant.Type.other;
		}
		Date now = new Date();
		String uploadPath = UploadUtils.getUploadPath(fileName, now.getTime());
		Attachment attachment = new Attachment();
		attachment.setAttachmentId(attachmentDao.getNextVal() + "");
		attachment.setKindId(kindId);
		attachment.setDescription("");
		attachment.setName(fileName);
		attachment.setPath(uploadPath);
		attachment.setType(type);
		attachment.setFileSize(multipartFile.getSize());
		attachment.setKind(kind);
		attachment.setStatus(status);
		attachment.setCreateTime(now);
		attachmentDao.addAttachment(attachment);
		// 异步调用方法
		this.saveBlob(attachment.getAttachmentId(), multipartFile, uploadPath);
		return attachment;
	}

	/**
	 * 保存文件到数据库和磁盘目录
	 * 
	 * @param attachmentId
	 * @param multipartFile
	 * @param uploadPath
	 */
	@Async
	public void saveBlob(String attachmentId, MultipartFile multipartFile, String uploadPath) {
		log.debug("开始保存文件");
		try {
			// 将附件文件保存到数据库中
			attachmentBlobService.addUploadFile(attachmentId, multipartFile);
			// 将文件保存到文件系统
			String filePath = System.getProperty(SystemConstant.WEBAPP_ROOT) + uploadPath;
			multipartFile.transferTo(new File(filePath));
		} catch (IllegalStateException e) {
			log.error("保存文件出错", e);
		} catch (IOException e) {
			log.error("保存文件出错", e);
		}
		log.debug("保存文件结束");
	}

	public boolean updateKindId(Attachment attachment) {
		return attachmentDao.updateAttachment(attachment) > 0;
	}

	public List<Attachment> getAttachmentsByKindId(String kindId, AttachmentConstant.Kind kind,
			AttachmentConstant.Status status) {

		return attachmentDao.getAttachmentsByKindId(kindId, kind, status);
	}

	public Attachment getAttachmentsById(String attachmentId) {
		return attachmentDao.getAttachmentById(attachmentId);
	}

	public File transferToFile(Attachment attachment) {
		String attachmentId = attachment.getAttachmentId();
		AttachmentBlob blob = attachmentBlobService.getAttachmentBlobById(attachmentId);
		if(blob == null){
			return null;
		}
		byte[] blobByte = blob.getAttachment();
		String path = attachment.getPath();
		// 获得文件路径
		String downLoadPath = System.getProperty(SystemConstant.WEBAPP_ROOT) + path;
		File file = new File(downLoadPath);
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(blobByte);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(fos);
		}
		return file;
	}
}
