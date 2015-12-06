package cn.com.bettle.net.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bettle.logic.service.AttachmentService;
import cn.com.bettle.net.constant.SystemConstant;
import cn.com.bettle.net.entity.Attachment;


/**
 * 附件下载
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api")
public class ApiDownloadController {

	protected final Logger log = LoggerFactory.getLogger(ApiDownloadController.class);

	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping("/download")
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = StringUtils.trimToEmpty(request.getParameter("fileId"));
		return this.download(fileId, request, response);
	}

	@RequestMapping("/download/{fileId}")
	public ModelAndView download(@PathVariable("fileId") String fileId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		// 获得文件路径
		Attachment attachment = attachmentService.getAttachmentsById(fileId);
		if(attachment == null){
			ModelAndView mv = new ModelAndView("fileNotFound");
			mv.addObject("message", "您请求的文件不存在或已被删除");
			mv.addObject("success", false);
			return mv;
		}
		String path = attachment.getPath();
		// 获得文件路径
		String downLoadPath = System.getProperty(SystemConstant.WEBAPP_ROOT) + path;
		log.debug(downLoadPath);
		String fileName = attachment.getName();
		try {
			File file = new File(downLoadPath);
			if (!file.exists()) {// 判断文件目录是否存在
				file.getParentFile().mkdirs();
				file = attachmentService.transferToFile(attachment);
			}
			if(file == null || !file.exists()){
				ModelAndView mv = new ModelAndView("fileNotFound");
				mv.addObject("message", "您请求的文件“"+fileName+"”不存在或已被删除");
				mv.addObject("success", false);
				return mv;
			}else{
				// 文件大小
				long fileLength = file.length();
				// 设置相应类型
				 response.setContentType("application/octet-stream");
				// 设置文件名
				response.setHeader("Content-disposition", "attachment; filename="
						+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
				response.setHeader("Content-Length", String.valueOf(fileLength));
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			}
		} catch (Exception e) {
			log.error("下载文件时出错",e);
			ModelAndView mv = new ModelAndView("fileNotFound");
			mv.addObject("message", "下载文件时出错");
			mv.addObject("success", false);
			return mv;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return null;
	}

}
