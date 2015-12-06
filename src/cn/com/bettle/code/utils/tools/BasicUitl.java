package cn.com.bettle.code.utils.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class BasicUitl {

	/**
	 * 转换Obj为replaceContent如果Obj为空时
	 * @param str				给定字符串

	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static String transformNull(Object Obj ,String replaceContent){
		return Obj == null || (Obj.toString()).trim().length() == 0 ? replaceContent : (Obj.toString()).trim();

	}
	
	
	/**
	 * 转换Obj为replaceContent如果Obj为空时
	 * @param str				给定字符串

	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static String transformSql(Object Obj ,String name,String op){
		return Obj == null || (Obj.toString()).trim().length() == 0 ? "" : " and "+name+" "+op+" "+(Obj.toString()).trim();

	}
	
	/**
	 * 转换Obj为replaceContent如果Obj为空旄1�7
	 * @param str				给定字符丄1�7

	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static String dataFormat(Object Obj ,String replaceContent){
		return Obj == null || (Obj.toString()).trim().length() == 0 ? replaceContent : "'"+(Obj.toString()).trim()+"'";

	}
	

	
	public static final String nullToEmpty(Object str) {
		if (str == null || StringUtils.isBlank(String.valueOf(str)) || "null".equals(String.valueOf(str))) {
			return "";
		} else {
			return str.toString().trim();
		}
	}
	/**
	 * 将一些HTML替换掉
	 * @param strContent				给定字符串
	 * @return					替换内容
	 */
	public static String fromatHtml(String strContent){
		strContent.replaceAll("&","&amp");
		strContent.replaceAll("'","''");
		strContent.replaceAll("<","&lt");
		strContent.replaceAll(">","&gt");
		strContent.replaceAll("chr(60)","&lt");
		strContent.replaceAll("chr(37)","&gt");
		strContent.replaceAll("\"","&quot");
		strContent.replaceAll(";",";");
		strContent.replaceAll(";",";");
		strContent.replaceAll("\n","<br/>");
		strContent.replaceAll(" ","&nbsp");
		return strContent;
  
	}
		
	/**
	 * 如果字符串为空,转化为指定内容.
	 * @param str				给定字符串
	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static String NVLToStr(Object obj,String replaceContent){
		String str = obj.toString();
		return str == null || str.trim().length() == 0 ? replaceContent : str; 
	}
	
	
	/**
	 * 如果字符串为空,转化为指定内容.
	 * @param str				给定字符串
	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */	
	public static int NVLToInt(Object obj,int replaceContent){
		if(obj != null){
			String str = obj.toString().trim();
			if(str.length() > 0){
				if(isInt(str))
					return Integer.parseInt(str);
				else
					return 0;
			}
		}
		return 0;
	}
	
	public static int DoubleToInt2(Object obj, int replaceContent) {
		if (obj != null) {
			String str = obj.toString().trim();
			if (str.length() > 0) {
				if (isDouble(str))
					return (int)Double.parseDouble(str);
				else
					return 0;
			}
		}
		return 0;
	}
	
	public static String nullToZero(Object obj){
		if(obj != null){
			String str = obj.toString().trim();
			if(str.length() > 0){
				if(isNumeric2(str))
					return str;
				else
					return "0";
			}
		}
		return "0";
	}

	public static long nullToZeroLong(Object obj){
		return nullToLong(obj,0L);
	}
	public static int nullToZeroInt(Object obj){
		return nullToInt(obj,0);
	}

	/**
	 * 如果字符串为空,转化为指定内容.
	 * @param obj				给定字符串
	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */	
	public static long nullToLong(Object obj,long replaceContent){
		
		 if(obj!=null){
			 String str = obj.toString().trim();
			 if(!StringUtils.isBlank(str)){
				if(isNumeric2(str)){
					return Long.parseLong(str);
				}else{
					return replaceContent;
				}
			 }
			 return 0L;
		 }
		 return 0L;
	}
	
	public static int nullToInt(Object obj,int replaceContent){
		
		 if(obj!=null){
			 String str = obj.toString().trim();
			 if(!StringUtils.isBlank(str)){
				if(isNumeric2(str)){
					return Integer.parseInt(str);
				}else{
					return replaceContent;
				}
			 }
			 return 0;
		 }
		 return 0;
	}
	/**
	 * 如果字符串为空,转化为指定内容.
	 * @param obj				给定字符串
	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static float NVLToFloat(Object obj,float replaceContent){
		String str = obj.toString();
		if(str!=null)
			str = str.trim();
		return str == null || str.length() == 0 ? replaceContent : Float.parseFloat(str); 
	}
	
	
	/**
	 * 如果字符串为空,转化为指定内容.
	 * @param obj				给定字符串
	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static double NVLToDouble(Object obj,double replaceContent){
		String str = obj.toString();
		if(str!=null)
			str = str.trim();
		return str == null || str.length() == 0 ? replaceContent : Double.parseDouble(str); 
	}
	
	
	/**
	 * 规范sql字符串（如果为空，转为空格；否则将单引号替换为2个单引号），防止sql注入．
	 * @param str				给定字符串
	 * @param replaceContent	替换内容
	 * @return					替换内容
	 */
	public static String transDBStr(String str){
		return str == null ? " " : str.trim().replaceAll("'", "''");
	}
	
	
	/**
	 * 将字符串中"<","&"替换为html转义字符．
	 * <br>(html展示).<br>
	 * @param str	源字符串
	 * @return		转换后的字符串
	 */
	public static String formatHtmlESC(String str){
		return str == null ? "" : str.trim().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll("\r\n|\r|\n","<br>");
	}
	
	
	/**
	 * 将字符串中"<","&"的html转义字符转换成"<","&"．
	 *  <br>(js中赋值).<br>
	 * @param str	源字符串
	 * @return		转换后的字符串
	 */
	public static String conFormatHtmlESC(String str){
		return str == null ? "" : str.trim().replaceAll("<br>","\r\n|\r|\n").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
	}
	
	
	/**
	 * 将字符串中单/双引号转化为字符单/双引号．
	 * @param str	源字符串
	 * @return		转换后的字符串
	 */
	public static String formatJsESC(String str){
		return str == null ? "" : str.trim().replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
	}
	
	
	/**
	 * 加入一个元素到数组最后．
	 * @param 	addValue  		要增加的值
	 * @param 	addArray		要增加的数组
	 * @return
	 */
	public static long[] insertBottomIntoArray(long addValue, long[] addArray){
		int oldLength = addArray.length;
		long[] newArray = new long[oldLength+1];
		for(int i=0; i<oldLength; i++){
			newArray[i] = addArray[i];
		}
		newArray[oldLength] = addValue;
		return newArray;
	}
	
	
	/**
	 * 从数组中删除一个元素．
	 * @param 	addValue  		要删除的值
	 * @param 	addArray		要删除的数组
	 * @return
	 */
	public static long[] deleteValuesFromArray(long delValue, long[] delArray){
		int oldLength = delArray.length;
		int num=-1;
		for(int i=0; i<oldLength; i++){
			if(delArray[i] == delValue){
				num = i;
				break;
			}
		}
		if(num==-1)
			return delArray;
		long[] newArray = new long[oldLength-1];
		int j = 0;
		for(int i=0; i<oldLength; i++){
			if(i!=num){
				newArray[j] = delArray[i];
				j++;
			}
		}
		return newArray;
	} 
	
	
	/**
	 * 加入一个元素到数组最后．
	 * @param 	addValue  		要增加的值
	 * @param 	addArray		要增加的数组
	 * @return
	 */
	public static int[] insertBottomIntoArray(int addValue, int[] addArray){
		int oldLength = addArray.length;
		int[] newArray = new int[oldLength+1];
		for(int i=0; i<oldLength; i++){
			newArray[i] = addArray[i];
		}
		newArray[oldLength] = addValue;
		return newArray;
	}
	
	
	/**
	 * 从数组中删除一个元素．
	 * @param 	addValue  		要删除的值
	 * @param 	addArray		要删除的数组
	 * @return
	 */
	public static int[] deleteValuesFromArray(int delValue, int[] delArray){
		int oldLength = delArray.length;
		int num=-1;
		for(int i=0; i<oldLength; i++){
			if(delArray[i] == delValue){
				num = i;
				break;
			}
		}
		if(num==-1)
			return delArray;
		int[] newArray = new int[oldLength-1];
		int j = 0;
		for(int i=0; i<oldLength; i++){
			if(i!=num){
				newArray[j] = delArray[i];
				j++;
			}
		}
		return newArray;
	}
	
	
	/**
	 * 加入一个元素到数组最后．
	 * @param 	addValue  		要增加的值
	 * @param 	addArray		要增加的数组
	 * @return
	 */
	public static String[] insertBottomIntoArray(String addValue, String[] addArray){
		int oldLength = addArray.length;
		String[] newArray = new String[oldLength+1];
		for(int i=0; i<oldLength; i++){
			newArray[i] = addArray[i];
		}
		newArray[oldLength] = addValue;
		return newArray;
	}
	
	
	/**
	 * 从数组中删除一个元素．
	 * @param 	addValue  		要删除的值
	 * @param 	addArray		要删除的数组
	 * @return
	 */
	public static String[] deleteValuesFromArray(String delValue, String[] delArray){
		int oldLength = delArray.length;
		int num=-1;
		for(int i=0; i<oldLength; i++){
			if(delArray[i] == delValue){
				num = i;
				break;
			}
		}
		if(num==-1)
			return delArray;
		String[] newArray = new String[oldLength-1];
		int j = 0;
		for(int i=0; i<oldLength; i++){
			if(i!=num){
				newArray[j] = delArray[i];
				j++;
			}
		}
		return newArray;
	}
	
	
	
	/*-----------------------------------------------------*/
	 //java中判断字符串是否为数字的三种方法
	 //1用JAVA自带的函数
	 public static boolean isNumeric1(String str){
	   for (int i = str.length();--i>=0;){   
	    if (!Character.isDigit(str.charAt(i))){
	     return false;
	    }
	   }
	   return true;
	  }

	 //2用正则表达式  效率比较高
	 public static boolean isNumeric2(String str){ 
	     Pattern pattern = Pattern.compile("[0-9]*"); 
	     return pattern.matcher(str).matches();    
	  } 

	 //3用ascii码

	 public static boolean isNumeric3(String str){
	    for(int i=str.length();--i>=0;){
	       int chr=str.charAt(i);
	       if(chr<48 || chr>57)
	          return false;
	    }
	    return true;
	 }
	 
	 
	 //判断是否是整数
	 public static boolean isInt(String str){ 
		 Pattern pattern = Pattern.compile("^\\d+$|-\\d+$");
	     return pattern.matcher(str).matches();    
	 } 
	//判断是否是小数
	 public static boolean isDouble(String str){ 
		 Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");
	     return pattern.matcher(str).matches();    
	 } 
	 
	//去空格和换行
	 public static String replaceBlank(String str) {
		         String dest = "";
		 
		         if (str!=null) {
		 
		             Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
		             Matcher m = p.matcher(str);
		 
		             dest = m.replaceAll("");
		 
		         }
		 
		         return dest;
		     }
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			   System.out.println("123:"+isDouble("a123"));  
			  System.out.println("0.123:"+isDouble("0.123"));  
			  System.out.println(".123:"+isDouble(".123"));  
			  System.out.println("1.23:"+isDouble("b1.23"));  
			  System.out.println("123.:"+isDouble("123."));  
			  System.out.println("00.123:"+isDouble("00.123"));  
			  System.out.println("123.0:"+isDouble("123.0"));  
			  System.out.println("123.00:"+isDouble("123.00"));  
			  System.out.println("0123:"+isDouble("0123"));  
		}
}
