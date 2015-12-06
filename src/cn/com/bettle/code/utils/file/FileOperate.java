package cn.com.bettle.code.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FileOperate {
    private String message;
    private static final Log log = LogFactory.getLog(FileOperate.class);
    public FileOperate() {
    } 
    /**
     * 读取文本
     * @param filePathAndName  文件完整路径
     * @param encoding  打开时使用的编码
     * @return 
     */
    public static String readTxt(String filePathAndName,String encoding) throws IOException,Exception{
     encoding = encoding.trim();
     StringBuffer str = new StringBuffer("");
     String st = "";
     try{
      FileInputStream fs = new FileInputStream(filePathAndName);
      InputStreamReader isr;
      if(encoding.equals("")){
       isr = new InputStreamReader(fs);
      }else{
       isr = new InputStreamReader(fs,encoding);
      }
      BufferedReader br = new BufferedReader(isr);
      try{
       String data = "";
       while((data = br.readLine())!=null){
         str.append(data+" "); 
       }
      }catch(Exception e){
    	  log.error("FileOperate.readTxt()\r\n" + e);
       throw e;
      }
      st = str.toString();
     }catch(IOException es){
    	 log.error("FileOperate.readTxt()\r\n" + es);
    	 throw es;
     }
     return st;     
    }

    /**
     * 创建文件夹
     * @param folderPath 文件夹路径
     * @return 
     */
    public static String createFolder(String folderPath)throws Exception{
        String txt = folderPath;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        }
        catch (Exception e){
        	log.error("FileOperate.createFolder()\r\n" + e);
        	throw e;
        }
        return txt;
    }
    
    /**
     * 创建多个文件夹
     * @param folderPath  文件夹路径
     * @param paths  子文件夹路径以|隔开个路径
     * @return 
     */
    public static String createFolders(String folderPath, String paths)throws Exception{
        String txts = folderPath;
        try{
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
       }catch(Exception e){
       	log.error("FileOperate.createFolders()\r\n" + e);
    	throw e;
       }
        return txts;
    }

    
    /**
     * �½��ļ�
     * @param filePathAndName �ı��ļ�������·�����ļ���
     * @param fileContent �ı��ļ�����
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent)throws Exception{
     
        try {
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
        catch (Exception e) {
       log.error("FileOperate.createFile()\r\n" + e);
    	 throw e;
        }
    }


    /**
     * �б��뷽ʽ���ļ�����
     * @param filePathAndName �ı��ļ�������·�����ļ���
     * @param fileContent �ı��ļ�����
     * @param encoding ���뷽ʽ ���� GBK ���� UTF-8
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent, String encoding)throws Exception{
     
        try {
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
        catch (Exception e) {
               log.error("FileOperate.createFile()\r\n" + e);
    	 throw e;
        }
    } 


    /**
     * ɾ���ļ�
     * @param filePathAndName �ı��ļ�������·�����ļ���
     * @return Boolean �ɹ�ɾ���true�����쳣����false
     */
    public static boolean delFile(String filePathAndName)throws Exception{
     boolean bea = false;
        try {
            String filePath = filePathAndName;
            File myDelFile = new File(filePath);
            if(myDelFile.exists()){
             myDelFile.delete();
             bea = true;
            }else{
             bea = false;
           log.info("FileOperate.delFile()\r\n The delected file does not exist" );
            }
        }
        catch (Exception e) {
       log.error("FileOperate.delFile()\r\n" + e);
    	 throw e;
        }
        return bea;
    }
    


    /**
     * ɾ���ļ���
     * @param folderPath �ļ���������·��
     * @return
     */
    public static void delFolder(String folderPath)throws Exception{
        try {
            delAllFile(folderPath); //ɾ����������������
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //ɾ����ļ���
        }
        catch (Exception e) {
               log.error("FileOperate.delFolder()\r\n" + e);
    	 throw e;
        }
    }
    
    
    /**
     * ɾ��ָ���ļ����������ļ�
     * @param path �ļ���������·��
     * @return
     * @return
     */
    public static boolean delAllFile(String path)throws Exception{
     boolean bea = false;
     try
     {
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
      }catch (Exception e) {
                  log.error("FileOperate.delAllFile()\r\n" + e);
    	 throw e;
        }
    }


    /**
     * ���Ƶ����ļ�
     * @param oldPathFile ׼�����Ƶ��ļ�Դ
     * @param newPathFile �������¾��·�����ļ���
     * @return
     */
    public static void copyFile(String oldPathFile, String newPathFile)throws Exception{
        try {
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
        }catch (Exception e) {
                  log.error("FileOperate.copyFile()\r\n" + e);
    	 throw e;
        }
    }
    

    /**
     * ��������ļ��е�����
     * @param oldPath ׼��������Ŀ¼
     * @param newPath ָ�����·������Ŀ¼
     * @return
     */
    public static void copyFolder(String oldPath, String newPath)throws Exception{
        try {
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
        }catch (Exception e) {
                         log.error("FileOperate.copyFolder()\r\n" + e);
    	 throw e;
        }
    }


    /**
     * �ƶ��ļ�
     * @param oldPath
     * @param newPath
     * @return
     */
    public static void moveFile(String oldPath, String newPath)throws Exception{
    	try
    	{
        copyFile(oldPath, newPath);
        delFile(oldPath);
      }
      catch (Exception e) {
                         log.error("FileOperate.moveFile()\r\n" + e);
    	 throw e;
        }
    }
    

    /**
     * �ƶ�Ŀ¼
     * @param oldPath
     * @param newPath
     * @return
     */
    public static void moveFolder(String oldPath, String newPath)throws Exception{
    	    	try
    	{
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
      }
       catch (Exception e) {
       log.error("FileOperate.moveFolder()\r\n" + e);
    	 throw e;
        }
    }
/**
 * д���ļ�
 * @param filePathAndName ����������·�����ļ���
 * @param encoding �ı��ļ��򿪵ı��뷽ʽ
 * @param text  writer text
 * @return true/false
 */
public static boolean writeFile( String filePathAndName,String encoding, String text )throws Exception{
	boolean a=false;
    try {
    	encoding = encoding.trim();
    	OutputStreamWriter writer;
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
    } catch ( IOException e ) {
    	 log.error("FileOperate.moveFolder()\r\n" + e);
    	 throw e;
      
    }
      return a;
}
/**
 * �����ļ��д�С
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
 * ɾ������ļ���
  * @param f  
 */
public static void del(File f) throws Exception {
	if(f.isDirectory()){//�ж����ļ�����Ŀ¼
		if(f.listFiles().length==0){//��Ŀ¼��û���ļ���ֱ��ɾ��
			if(!f.delete()) throw new Exception("delete frustrated!");
		}else{//��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼
			File[] delFile=f.listFiles();
			int i =delFile.length;
			for(int j=0;j<i;j++){
				del(delFile[j]); //�ݹ����del����
			}
			if(!f.delete()) throw new Exception("delete frustrated!");
		}
	}else{
		if(!f.delete()) throw new Exception("delete frustrated!");

	}
}
}

