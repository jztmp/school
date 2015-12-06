package cn.com.bettle.code.utils.file;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 


import cn.com.bettle.code.utils.image.ImageUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zongqi
 * Date: 13-1-14
 * Time: zongqi 10:16
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadTool {
	
	private static final Log log = LogFactory.getLog(FileUploadTool.class);
    private HttpServletRequest request = null; //用于存放request请求，
    private List<FileItem>  itemList = null; //从表单中获取的文件tiem
    private Map<String, List<String>>   params = new HashMap<String, List<String>>(); //存放表单中传来的非文件参数（文本形式的参数）
    private Map<String, FileItem>  files = new HashMap<String, FileItem>(); //从表单中获取的文件tiem
    private String  fileDir=null; //用户存放保存文件的路径
    private String  tempDir=null; //用户存放保存文件的路径
    private String  pressImage=null; //水印图片文件的路径
    private long fileSize= 5 * 1024 * 1024;;
    private String  url=null; //水印图片文件的路径
    /*
    * 构造函数，初始化request中表单信息
    * @param  request 请求
    * @param  fileSize 文件大小的限制
    * @param  tempDir 文件上传的临时目录
    * @param  fileDir 文件上传到的真实目录
    *
    *
    * */
    public FileUploadTool(HttpServletRequest request, long fileSize, String tempDir, String fileDir,String pressImage,String url) throws Exception {
        request.setCharacterEncoding("utf-8");
        this.request = request;
        this.fileDir=fileDir;
        this.tempDir=tempDir;
        this.pressImage = pressImage;
        this.url = url;
        File filetemp  = new File(tempDir);
        File filepath  = new File(fileDir);
        //this.request.setCharacterEncoding( utf-8 );
        DiskFileItemFactory factory = new DiskFileItemFactory();
        if (factory != null) {
        	
        	if (!filetemp.exists()) {
        		filetemp.mkdirs();
			}
        	
        	if (!filepath.exists()) {
        		filepath.mkdirs();
			}
            factory.setRepository(filepath);//一个临时目录
        }
        ServletFileUpload upload = new ServletFileUpload(factory);
        if (fileSize > 0) {
            upload.setFileSizeMax(fileSize);
            upload.setHeaderEncoding("utf-8");
        }
        itemList = upload.parseRequest(request);
        if(fileSize!=0){
        this.fileSize = fileSize;
        }
        init();
    }
    /*
    *  分离普通参数（文本参数）和文件参数的方法
    *
    * */
    private void init()throws Exception {
        IPTimeStamp IPts = new IPTimeStamp(this.request.getRemoteAddr());
        for (FileItem item : itemList) {

            if (item.isFormField()) {  //是基本数据
                String name = item.getFieldName();
                String value = item.getString("utf-8");
                List<String>  temp = null;
                if (this.params.containsKey(name)) {
                    temp = this.params.get(name);
                } else {
                    temp = new ArrayList<String>();
                }
                temp.add(value);
                this.params.put(name, temp);
            } else { //是文件数据
            	
            	// 得到文件的大小
				long size = item.getSize();
				if(size>=this.fileSize){
					throw new Exception("您上传的文件"+ item.getName()+"太大，请裁剪后再上传！");
				}
            	
                int len = item.getName().split("\\.").length;
                String fileSysName = IPts.getRandomFileName();
                //String fileName =  "D:\\download\\files\\"  + fileSysName + "."  + item.getName().split("\\.")[len - 1];
                this.files.put(fileSysName +  "."  + item.getName().split("\\.")[len - 1], item);
            }


        }
    }
    /*
    * 获取单个的参数值
    *  @param 参数的名字
    * */
    public String getParameter(String name) {
        String result = null;
        List <String>  list = this.params.get(name);
        if (list != null) {
            result = list.get(0);
        }
        return result;
    }
    /*
   * 获取多个的参数值 例如 复选框
   * @param 参数的名字
   * */
    public String[] getParameterValues(String name) {
        String result[] = null;
        List<String>  list = this.params.get(name);
        if (list != null) {
            result = list.toArray(new String[]{});
        }
        return result;
    }

   /*
   * 保存所有文件的方法
   * */

    public Map<String,String>  saveAll() throws Exception {
        FileItem item = null;
        Map<String,String>  names = new HashMap<String,String> ();
        if (this.files.size() > 0) {
            Set<String>  set = this.files.keySet(); //获取所有文件名称、、自动命名之后的
            File saveFile=null;
            InputStream input=null;
            OutputStream output=null;
            Iterator<String>  iter = set.iterator();
            while (iter.hasNext()) {
                String fileName=iter.next();
                item=this.files.get(fileName);
                names.put(item.getName().split("\\.")[0],this.fileDir+ "\\" +fileName);
                //new FileOutputStream(fileName);
                try {
                    input = item.getInputStream();
                    output = new FileOutputStream(new File(this.fileDir+ "\\" +fileName));
                    int temp = 0;
                    byte[] b = new byte[512];
                    while ((temp = input.read(b)) != -1) {
                        output.write(b, 0, temp);
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    try {
                        input.close();
                        output.close();

                    } catch (Exception e1) {
                        throw e1;
                    }
                }
            }
        }
        deleteTemp();
        return names;
    }
    
    
    public Map<String,String>  saveAll(int medium_height, int medium_width, boolean medium_bb,int small_height, int small_width, boolean small_bb) throws Exception {
        FileItem item = null;
        Map<String,String>  names = new HashMap<String,String> ();
        if (this.files.size() > 0) {
            Set<String>  set = this.files.keySet(); //获取所有文件名称、、自动命名之后的
            File saveFile=null;
            InputStream input=null;
            OutputStream output=null;
            Iterator<String>  iter = set.iterator();
            while (iter.hasNext()) {
                String fileName=iter.next();
                item=this.files.get(fileName);
                names.put(item.getName(),this.url+ "/" +fileName);
                //new FileOutputStream(fileName);
                try {
                    input = item.getInputStream();
                    output = new FileOutputStream(new File(this.fileDir+ "\\" +fileName));
                    int temp = 0;
                    byte[] b = new byte[512];
                    while ((temp = input.read(b)) != -1) {
                        output.write(b, 0, temp);
                    }
                    ImageUtils.scale2(this.fileDir+ "\\" +fileName, this.fileDir+ "\\small_" +fileName, small_height, small_width, small_bb);
                    names.put("small_"+item.getName(),this.url+ "/small_" +fileName);
                    ImageUtils.scale2(this.fileDir+ "\\" +fileName, this.fileDir+ "\\medium_" +fileName, medium_height, medium_width, medium_bb);
                    names.put("medium_"+item.getName(),this.url+ "/medium_" +fileName);
                    ImageUtils.pressImage(this.pressImage,this.fileDir+ "\\" +fileName, this.fileDir+ "\\" +fileName, 1f);
                } catch (Exception e) {
                    throw e;
                } finally {
                    try {
                        input.close();
                        output.close();

                    } catch (Exception e1) {
                        throw e1;
                    }
                }
            }
        }
        deleteTemp();
        return names;
    }
    /*
    * 删除临时文件的方法，如果不删除，临时文件夹会很大
    * */
    private void deleteTemp(){
        File tempDir = new File(this.tempDir);
        File tempFiles[]=tempDir.listFiles();
        for(File file:tempFiles){
            file.delete();
        }
    }

}
 
  
 
  

