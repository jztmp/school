package cn.com.bettle.net.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import cn.com.bettle.logic.service.AttachmentService;
import cn.com.bettle.net.constant.AttachmentConstant;
import cn.com.bettle.net.entity.Attachment;


@Controller
@RequestMapping("/api")
public class ApiUploadController {

	protected final Logger log = LoggerFactory.getLogger(ApiUploadController.class);

	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping(value = "/upload")
	public String fileUpload(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			@RequestParam("file") MultipartFile upfile) {
		String fileName = upfile.getOriginalFilename();
		return this.fileUpload(request, response, modelMap, fileName, upfile);
	}

	@RequestMapping(value = "/upload", params = "fileName")
	public String fileUpload(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile upfile) {

		String kindId = StringUtils.trimToEmpty(request.getParameter("kindId"));
		AttachmentConstant.Kind kind = AttachmentConstant.Kind.message;
		String kindStr = StringUtils.trimToEmpty(request.getParameter("kind"));
		if (StringUtils.isNotBlank(kindStr)) {
			try {
				kind = Enum.valueOf(AttachmentConstant.Kind.class, kindStr);
			} catch (Exception e) {
				log.error("转换业务类型失败");
			}
		}
		Attachment attachment = attachmentService.addUploadFile(upfile, fileName, kindId, kind,
				AttachmentConstant.Status.A);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("message", "文件上传成功");
		modelMap.addAttribute("id", attachment.getAttachmentId());
		modelMap.addAttribute("filePath", attachment.getPath());
		modelMap.addAttribute("src", request.getContextPath() + attachment.getPath());

		return "fileUpload";
	}

	protected void showParams(ServletRequest request) {
		System.out.println("------------------------------");
		@SuppressWarnings("unchecked")
		Map<String, String[]> tmp = request.getParameterMap();
		if (tmp != null) {
			for (String key : tmp.keySet()) {
				String[] values = tmp.get(key);
				System.out.println(key + ":" + (values.length == 1 ? values[0].trim() : values));
			}
		}
		System.out.println("------------------------------");
	}

}
