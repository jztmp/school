package cn.com.bettle.code.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




/**
 * 在static方法中不能使用this，那么在static方法中该怎样获得类加载器呢??,如下已经解决
 * 功能：操作文件的工具类
 * @author zxm
 *
 */
public class FileUtils {
	private static final Log log = LogFactory.getLog(FileUtils.class);
	/**
	 * 获得类路径下的文件的路径
	 * @param _fileName:类路径下的文件名
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getClassPathFilePath(String _fileName) throws UnsupportedEncodingException{
		URL url = getClassPathFileUrl(_fileName);
		String filePath	= URLDecoder.decode(url.getPath(),"UTF-8");
		return filePath;
	}
	
	/**
	 * 获得类路径下的文件的URL
	 * @param _fileName:类路径下的文件名
	 * @return ：对url类的一点说明-url.getPath()返回的path是经过编码的
	 */
	public static URL getClassPathFileUrl(String _fileName){
		ClassLoader cl = FileUtils.class.getClassLoader();
		URL url = cl.getResource(_fileName);
		return url;
	}
	
	/**
	 * 以流的形式加载类路径下的文件
	 * @param _fileName:类路径下的文件名
	 * @return
	 */
	public static InputStream getResourceAsStream(String _fileName){
		ClassLoader cl = FileUtils.class.getClassLoader();
		InputStream is = cl.getResourceAsStream(_fileName);
		return is;
	}
	
	

	/**
	 * 获得应用中制定目录的文件
	 * @param servletContStriext
	 * @param _dir：文件存放的目录
	 * @param _fileName：制定的文件名
	 * @return
	 */
	public static String getServerFilePath(ServletContext servletContStriext ,String _dir,String _fileName){
//		String filePath = servletContStriext.getRealPath("/");
		String path =null;
	    if(ServerDetector.isTomcat()){//tomcat server
	    	path = servletContStriext.getRealPath("/")+_dir+"\\"+_fileName;
	    }else if(ServerDetector.isWebLogic()){//weblogic server
	    	path = servletContStriext.getRealPath("/")+"\\"+_dir+"\\"+_fileName;
	    }
		return path;
	}
	
	/**
	 * 获得应用中制定目录中文件的路径
	 * @param servletContStriext
	 * @param _dir：文件存放的目录
	 * @return
	 */
	public static String getServerFilePath(ServletContext servletContext ,String _dir){
//		String filePath = servletContStriext.getRealPath("/");
		String path =null;
	    if(ServerDetector.isTomcat()){//tomcat server
	    	path = servletContext.getRealPath("/")+_dir+"/";
	    }else if(ServerDetector.isWebLogic()){//weblogic server
	    	path = servletContext.getRealPath("/")+"\\"+_dir+"\\";
	    }
		return path;
	}
	
	
    /**
     * 读取文本文件内容
     * @param filePathAndName 带有完整绝对路径的文件名
     * @param encoding 文本文件打开的编码方式
     * @return 返回文本文件的内容
     */
    public static String readTxt(String filePathAndName,String encoding) throws IOException,Exception{
     encoding = encoding.trim();
     StringBuffer str = new StringBuffer("");
     String st = "";
      FileInputStream fs = new FileInputStream(filePathAndName);
      InputStreamReader isr;
      if(encoding.equals("")){
       isr = new InputStreamReader(fs);
      }else{
       isr = new InputStreamReader(fs,encoding);
      }
      BufferedReader br = new BufferedReader(isr);
       String data = "";
       while((data = br.readLine())!=null){
         str.append(data+" "); 
       }
      
      st = str.toString();
      return st;     
    }

    /**
     * 新建目录
     * @param folderPath 目录
     * @return 返回目录创建后的路径
     */
    public static String createFolder(String folderPath)throws Exception{
        String txt = folderPath;
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        return txt;
    }
    
    /**
     * 多级目录创建
     * @param folderPath 准备要在本级目录下创建新目录的目录路径 例如 c:myf
     * @param paths 无限级目录参数，各级目录以单数线区分 例如 a|b|c
     * @return 返回创建文件后的路径 例如 c:myfac
     */
    public static String createFolders(String folderPath, String paths)throws Exception{
        String txts = folderPath;
            String txt;
            txts = folderPath;
            StringTokenizer st = new StringTokenizer(paths,"|");
            for(int i=0; st.hasMoreTokens(); i++){
                    txt = st.nextToken().trim();
                    if(txts.lastIndexOf("/")!=-1){ 
                        txts = createFolder(txts+txt);
                    }else{
                        txts = createFolder(txts+txt+"/");    
                    }
            }
        return txts;
    }

    
    /**
     * 新建文件
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent 文本文件内容
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent)throws Exception{
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
            resultFile.close();
    }

    /**
     * 有编码方式的文件创建
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent 文本文件内容
     * @param encoding 编码方式 例如 GBK 或者 UTF-8
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent, String encoding)throws Exception{
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            PrintWriter myFile = new PrintWriter(myFilePath,encoding);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
    } 


    /**
     * 删除文件
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @return Boolean 成功删除返回true遭遇异常返回false
     */
    public static boolean delFile(String filePathAndName)throws Exception{
     boolean bea = false;
            String filePath = filePathAndName;
            File myDelFile = new File(filePath);
            if(myDelFile.exists()){
             myDelFile.delete();
             bea = true;
            }else{
             bea = false;
             log.info("FileOperate.delFile()\r\n The delected file does not exist" );
            }
        return bea;
    }
    


    /**
     * 删除文件夹
     * @param folderPath 文件夹完整绝对路径
     * @return
     */
    public static void delFolder(String folderPath)throws Exception{
            delAllFile(folderPath); //ɾ����������������
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //ɾ����ļ���
    }
    
    
    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return
     * @return
     */
    public static boolean delAllFile(String path)throws Exception{
     boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            }else{
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path+"/"+ tempList[i]);//��ɾ�����ļ���������ļ�
                delFolder(path+"/"+ tempList[i]);//��ɾ������ļ���
                bea = true;
            }
        }
        return bea;
    }


    /**
     * 复制单个文件
     * @param oldPathFile 准备复制的文件源
     * @param newPathFile 拷贝到新绝对路径带文件名
     * @return
     */
    public static void copyFile(String oldPathFile, String newPathFile)throws Exception{
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) { //�ļ�����ʱ
                InputStream inStream = new FileInputStream(oldPathFile); //����ԭ�ļ�
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while((byteread = inStream.read(buffer)) != -1){
                    bytesum += byteread; //�ֽ��� �ļ���С
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
    }
    

    /**
     * 复制整个文件夹的内容
     * @param oldPath 准备拷贝的目录
     * @param newPath 指定绝对路径的新目录
     * @return
     */
    public static void copyFolder(String oldPath, String newPath)throws Exception{
            new File(newPath).mkdirs(); //����ļ��в����� ��b���ļ���
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }else{
                    temp=new File(oldPath+File.separator+file[i]);
                }
                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                    (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//��������ļ���
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
    }


    /**
     * 移动文件
     * @param oldPath
     * @param newPath
     * @return
     */
    public static void moveFile(String oldPath, String newPath)throws Exception{
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }
    

    /**
     * 移动目录
     * @param oldPath
     * @param newPath
     * @return
     */
    public static void moveFolder(String oldPath, String newPath)throws Exception{

        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }
    /**
     * 写入文件
     * @param filePathAndName 带有完整绝对路径的文件名
     * @param encoding 文本文件打开的编码方式
     * @param text  writer text
     * @return true/false
     */
public static boolean writeFile( String filePathAndName,String encoding, String text )throws RuntimeException{
	boolean a=false;
    	encoding = encoding.trim();
    	OutputStreamWriter writer = null;
    	try{
    	if(encoding.equals("")){
    		writer= new OutputStreamWriter( new FileOutputStream( filePathAndName ));
    	}
    	else
    	{
    	 writer= new OutputStreamWriter( new FileOutputStream( filePathAndName ), encoding);
    	}
        writer.write( text );
        writer.close();
        a=true;
    	}catch(Exception e){
    		throw new RuntimeException(e.getMessage(),e.getCause());
    	} finally{
    		if(writer!=null){
    			try{
    				writer.close();
    			}catch (IOException e) {
    				throw new RuntimeException(e.getMessage(),e.getCause());
    			}
    		}
    		
    	}
      return a;
}
/**
 * 计算文件夹大小
  * @param Path  
   * @return size
 */
public static long getDirectorySize( String Path ) {
    long retSize = 0;
    File dir = new File(Path);
    if ( ( dir == null ) || !dir.isDirectory() ) {
        return retSize;
    }
    File[] entries = dir.listFiles();
    int count = entries.length;
    for ( int i = 0; i < count; i++ ) {
        if ( entries[ i ].isDirectory() ) {
        	
            retSize += getDirectorySize( entries[ i ].getAbsolutePath());
        } else {
            retSize += entries[ i ].length();
        }
    }
    return retSize;
}   
/**
 * 删除整个文件夹
  * @param f  
 */
public static void del(File f) throws RuntimeException {
	if(f.isDirectory()){//�ж����ļ�����Ŀ¼
		if(f.listFiles().length==0){//��Ŀ¼��û���ļ���ֱ��ɾ��
			if(!f.delete()) throw new RuntimeException("delete frustrated!");
		}else{//��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼
			File[] delFile=f.listFiles();
			int i =delFile.length;
			for(int j=0;j<i;j++){
				del(delFile[j]); //�ݹ����del����
			}
			if(!f.delete()) throw new RuntimeException("delete frustrated!");
		}
	}else{
		if(!f.delete()) throw new RuntimeException("delete frustrated!");

	}
}


public static String bolbToFile(InputStream ins) throws RuntimeException {
	
	OutputStream fout = null;
	try{
	String path = "";
    long now = System.currentTimeMillis();   
    String prefix = String.valueOf(now); // 根据系统时间生成上传后保存的文件名   时间戳
	String fileName = "MyPIC"+prefix+".jpg";
	String ShowPath = System.getProperty ("ShowPath");
	String fileUrl = ShowPath+fileName;
	path = "Showdata/"+fileName;
		
			fout = new FileOutputStream(fileUrl, true);
		
		// 下面将BLOB数据写入文件
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = ins.read(b)) != -1) {
			fout.write(b, 0, len);
		}
		// 依次关闭
		fout.flush();
		fout.close();
		return path;
	} catch (FileNotFoundException e) {
		throw new RuntimeException(e.getMessage(),e.getCause());
	} catch (IOException e) {
		throw new RuntimeException(e.getMessage(),e.getCause());
	} finally{
		if(fout!=null){
			try{
			fout.flush();
			}catch (IOException e) {
				throw new RuntimeException(e.getMessage(),e.getCause());
			}
		}
		
	}
}
}
