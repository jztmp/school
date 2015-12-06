package cn.com.bettle.net.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.bettle.net.constant.SystemConstant;

public class UploadUtils {

	/**
	 * 文件允许格式
	 */
	public static String[] FILE_TYPE = { ".rar", ".zip", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf",
			".txt" };

	/**
	 * 图片允许格式
	 */
	public static String[] PHOTO_TYPE = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };

	/**
	 * 视频允许格式
	 */
	public static String[] VIDEO_TYPE = { ".flv", ".swf", ".wmv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg", ".ogg",
			".mov", ".mp4" };

	/**
	 * 流程定义格式
	 */
	public static String[] WORKFLOW_TYPE = { ".bpmn20.xml", ".bpmn" };

	public static boolean isFileType(String fileName, String[] typeArray) {
		for (String type : typeArray) {
			if (fileName.toLowerCase().endsWith(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 递归获得目录的所有地址
	 * 
	 * @param realpath
	 * @param files
	 * @param fileType
	 * @return
	 */
	public static List<java.io.File> getFiles(String realpath, List<File> files, String[] fileType) {
		File realFile = new File(realpath);
		if (realFile.isDirectory()) {
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles) {
				if (file.isDirectory()) {
					getFiles(file.getAbsolutePath(), files, fileType);
				} else {
					if (isFileType(file.getName(), fileType)) {
						files.add(file);
					}
				}
			}
		}
		return files;
	}

	/**
	 * 得到文件上传的相对路径
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getUploadPath(String fileName, long time) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		String uploadPath = new StringBuffer(SystemConstant.UPLOAD_FOLDER).append("/")
				.append(formater.format(new Date())).append("/").append(time).append(getFileExt(fileName)).toString();
		// 判断父文件夹是否存在，不存在则创建
		File dir = new File(System.getProperty(SystemConstant.WEBAPP_ROOT) + uploadPath).getParentFile();
		if (!dir.exists()) {
			try {
				// 文件不存在，创建所在的文件夹
				dir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return uploadPath;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 删除物理文件
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		File file = new File(System.getProperty(SystemConstant.WEBAPP_ROOT) + path);
		file.delete();
	}

}
