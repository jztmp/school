/**
 * 
 */
package cn.com.bettle.code.utils.upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.id.DefaultIdentifierGeneratorFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.bettle.code.utils.date.DateUtils;



/**
 * 设想一：设计一个基类继承HttpServlet，此类可以自动处理包含附件的请求，并将文件保存，同时提供一个execute方法给用户作为接口，但灵活性比较差
 * 设想二：提供一个工具类可以自动以自定的文件名保存文件，同时将非文件表单域自动封装到HashMap中。
 * 
 * @author John
 * 
 */
public class BasicFileUpload {
	
	private static final Log log = LogFactory.getLog(BasicFileUpload.class);
	public  String fileExtensionName = "png";
	public static final long MAX_SIZE = 10 * 1024 * 1024;// 默认设置上传文件最大值:10M
	public static final int TEMP_SIZE = 4 * 1024;// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘

	private String[] allowedExt = null;// 允许上传的文件的扩展名--使用正则表达式实现

	private int tempSize;
	private long maxSize;
	private String tmpdir;// 当内存中超过了允许的大小时，文件临时保存的目录，默认为web服务器的临时目录中
	private String savePath;
	private String savedir;// 文件保存目录
	private String fileNameFormat;// 保存在web服务器上的文件名生成模式？？？暂时不提供，默认使用文件名+System.currentTimeMillis()

	// 默认构造函数，将对所用属性启用默认值,不对上传文件的格式进行验证

	public BasicFileUpload(String savePath) {
		log.debug("saveing.......");
		log.debug("saveDir:" + savePath);
		this.savePath = savePath;
		int year = DateUtils.getYear();
		int moth = DateUtils.getMonth();
		int day = DateUtils.getDay();
		this.maxSize = BasicFileUpload.MAX_SIZE;
		this.tempSize = BasicFileUpload.TEMP_SIZE;
		this.tmpdir =  System.getProperty("java.io.tmpdir");
		this.savedir = "" + year;
		this.savedir +=  "/" + String.format("%02d", moth);
		this.savedir +=  "/" + String.format("%02d", day);
		log.debug("savedir:" + this.savedir);

	}

	/**
	 * @param _maxSize
	 * @param _tempSize
	 * @param _allowExt
	 *            ：允许上传的文件类型
	 */
	public BasicFileUpload(long _maxSize, int _tempSize, String[] _allowExt,
			String _savedir) {
		this.maxSize = _maxSize;
		this.tempSize = _tempSize;
		this.allowedExt = _allowExt;
		this.tmpdir = System.getProperty("java.io.tmpdir");
		this.savedir = _savedir;
	}
	
	public BasicFileUpload(long _maxSize,String savePath){
		this(savePath);
		this.maxSize = _maxSize;//重新赋值最大上传数据量
	}
	
	
	

	public String generateUUID() {
		org.apache.commons.id.DefaultIdentifierGeneratorFactory factory = new DefaultIdentifierGeneratorFactory();
		org.apache.commons.id.uuid.UUID uuid = (org.apache.commons.id.uuid.UUID) factory
				.uuidVersionFourGenerator().nextIdentifier();
		return uuid.toString();
	}

	/**
	 * 文件扩展名的验证使用js验证。
	 * 
	 * @return :封装了所用的表单元素：但还不太清楚当表单中有复选框时的情况是怎样的？？？
	 */
	public HashMap disposeRequest(HttpServletRequest request)throws Exception  {

		log.debug("disposeRequest invoked");

		HashMap hm = new HashMap();
		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(this.tempSize);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		dfif.setRepository(new File(this.tmpdir));// 设置存放临时文件的目录,web根目录下的临时目录
		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		// 设置最大上传大小
		sfu.setSizeMax(MAX_SIZE);
		// 从request得到所有上传域的列表
		List fileList = null;
		try {
			fileList = sfu.parseRequest(request);
		} catch (FileUploadException e) {// 处理文件尺寸过大异常
			hm.put("size", "false");
			log.error("文件尺寸过大" + e.getMessage(), e);
			return hm;
		}
		// 是否要对fileList中的值进行判断

		// 得到所有上传的文件
		Iterator fileItr = fileList.iterator();
		// 循环处理所有文件
		log.debug("循环处理所有文件   ");
		while (fileItr.hasNext()) {
			FileItem fileItem = null;
			String path = null;
			// 下面收集表单中的所有信息，并保存在hashMap中

			// 得到当前文件
			fileItem = (FileItem) fileItr.next();
			// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
			if (fileItem.isFormField()) {
				log.debug("fileItem.isFormField = true");
				String name = fileItem.getFieldName();
				String value;
				try {
					value = fileItem.getString("utf-8");
					hm.put(name, value);
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.debug("fileItem.isFormField = false");

				// 到文件的完整路径
				path = fileItem.getName();
				if (path != null && !path.equals("")) {
					log.debug("path is not null");
					// 得到文件的大小
					long size = fileItem.getSize();
					log.debug("file size :: "+size);
					if (size > this.maxSize) {
						hm.put("size", "false");
						log.debug("size :: "+size+" this size > maxSize");
					}
					// 得到去除路径的文件名
					// 得到文件的扩展名(无扩展名时将得到全名)
					String saveFileExtension = FilenameUtils.getExtension(path);
					String fileNameWithExt = FilenameUtils.getName(path);//包含扩展名的文件名
					hm.put("fileNameWithExt", fileNameWithExt);
					File saveFilePath = new File(this.savePath, this.savedir);
					if (!saveFilePath.exists()) {
						saveFilePath.mkdirs();
					}
					if(saveFileExtension==null || saveFileExtension.equals(""))
					{
						saveFileExtension = this.fileExtensionName;
					}
					String saveFileName = this.generateUUID() + "."+ saveFileExtension;
					File saveFile = new File(saveFilePath, saveFileName);
					hm.put("fileName", saveFileName);// 保存去除路径的文件名
					hm.put("filePath", this.savedir + "/" + saveFileName);
					log.debug("saveFilePath:" + saveFile.getAbsolutePath());
					// 保存文件到指定目录下，如：C:\\upload
					try {
						fileItem.write(saveFile);

					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				} else {
					log.debug("path is null");
				}
			}

		}
		return hm;
	}

	/**
	 * @return the fileNameFormat
	 */
	public String getFileNameFormat() {
		return fileNameFormat;
	}

	/**
	 * @param fileNameFormat
	 *            the fileNameFormat to set
	 */
	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}

	/**
	 * @return the tmpdir
	 */
	public String getTmpdir() {
		return tmpdir;
	}

	/**
	 * @param tmpdir
	 *            the tmpdir to set
	 */
	public void setTmpdir(String tmpdir) {
		this.tmpdir = tmpdir;
	}

	/**
	 * @return the allowedExt
	 */
	public String[] getAllowedExt() {
		return allowedExt;
	}

	/**
	 * @param allowedExt
	 *            the allowedExt to set
	 */
	public void setAllowedExt(String[] allowedExt) {
		this.allowedExt = allowedExt;
	}

	/**
	 * @return the maxSize
	 */
	public long getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize
	 *            the maxSize to set
	 */
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * @return the tempSize
	 */
	public long getTempSize() {
		return tempSize;
	}

	/**
	 * @param tempSize
	 *            the tempSize to set
	 */
	public void setTempSize(int tempSize) {
		this.tempSize = tempSize;
	}

	public String getFileExtensionName() {
		return fileExtensionName;
	}

	public void setFileExtensionName(String fixFileExtensionName) {
		this.fileExtensionName = fixFileExtensionName;
	}
	
	

}
