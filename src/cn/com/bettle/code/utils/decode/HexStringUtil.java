package cn.com.bettle.code.utils.decode;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;


/**
* @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
* @category
* <b>功能：</b>16进制和字符串互转工具类
 * 
 * */ 
public class HexStringUtil {
	
	
	//转化字符串为十六进制编码
	public static String toHexString(String s) {
	   String str = "";
	   for (int i = 0; i < s.length(); i++) {
	    int ch = (int) s.charAt(i);
	    String s4 = Integer.toHexString(ch);
	    str = str + s4;
	   }
	   return str;
	}
	// 转化十六进制编码为字符串
	public static String toStringHex1(String s) {
	   byte[] baKeyword = new byte[s.length() / 2];
	   for (int i = 0; i < baKeyword.length; i++) {
	    try {
	     baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
	       i * 2, i * 2 + 2), 16));
	    } catch (Exception e) {
	     e.printStackTrace();
	    }
	   }
	   try {
	    s = new String(baKeyword, "utf-8");// UTF-16le:Not
	   } catch (Exception e1) {
	    e1.printStackTrace();
	   }
	   return s;
	}


	/*
	* 16进制数字字符集
	*/
	private static String hexString = "0123456789ABCDEF";
	/*
	* 将字符串编码成16进制数字,适用于所有字符（包括中文）
	*/
	public static String encode(String str) {
	   // 根据默认编码获取字节数组
	   byte[] bytes = str.getBytes();
	   StringBuilder sb = new StringBuilder(bytes.length * 2);
	   // 将字节数组中每个字节拆解成2位16进制整数
	   for (int i = 0; i < bytes.length; i++) {
	    sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
	    sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
	   }
	   return sb.toString();
	}
	/*
	* 将16进制数字解码成字符串,适用于所有字符（包括中文）
	*/
	public static String decode(String bytes) {
	   ByteArrayOutputStream baos = new ByteArrayOutputStream(
	     bytes.length() / 2);
	   // 将每2位16进制整数组装成一个字节
	   for (int i = 0; i < bytes.length(); i += 2)
	    baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
	      .indexOf(bytes.charAt(i + 1))));
	   return new String(baos.toByteArray());
	}


	   /**
	    * 字节转换为16进制
	   * @param b byte[]
	   * @return String
	   */
	   public static String Bytes2HexString(byte[] b) {
		    String ret = "";
		    for (int i = 0; i < b.length; i++) {
		     String hex = Integer.toHexString(b[i] & 0xFF);
		     if (hex.length() == 1) {
		      hex = '0' + hex;
		     }
		     ret += hex.toUpperCase();
		    }
		    return ret;
	   }
	   
	   /**
	    * 字节转换为10进制
	    * */
		public static String Bytes2TenString(byte[] b) {
			String hex = Bytes2HexString(b);
			hex = "" + Integer.parseInt(hex, 16);//// 十六进制转化为十进制
			return hex;
		}
	   
		
		
	   public static byte[] getHexByteArray(String hexString) {
		     return new BigInteger(hexString,16).toByteArray();
	   }

	   /**
	   * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	   * @param src0 byte
	   * @param src1 byte
	   * @return byte
	   */
	   public static byte uniteBytes(byte src0, byte src1) {
	    byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
	      .byteValue();
	    _b0 = (byte) (_b0 << 4);
	    byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
	      .byteValue();
	    byte ret = (byte) (_b0 ^ _b1);
	    return ret;
	   }
	   /**
	   * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	   * @param src String
	   * @return byte[]
	   */
	   public static byte[] HexString2Bytes(String src) {
	    byte[] ret = new byte[8];
	    byte[] tmp = src.getBytes();
	    for (int i = 0; i < 8; i++) {
	     ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
	    }
	    return ret;
	   }
	   
	   
	   
	   //JAVA 16进制字符串与2进制字符串相互转换
	   public static String hexString2binaryString(String hexString) {
		    if (hexString == null || hexString.length() % 2 != 0)
		      return null;
		    String bString = "", tmp;
		    for (int i = 0; i < hexString.length(); i++) {
		      tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
		      bString += tmp.substring(tmp.length() - 4);
		    }
		    return bString;
		  }
		//JAVA 2进制字符串与16进制字符串相互转换
		  public static String binaryString2hexString(String bString) {
		    if (bString == null || bString.equals("") || bString.length() % 8 != 0)
		      return null;
		    StringBuffer tmp=new StringBuffer();
		    int iTmp = 0;
		    for (int i = 0; i < bString.length(); i += 4) {
		      iTmp = 0;
		      for (int j = 0; j < 4; j++) {
		        iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
		      }
		      tmp.append(Integer.toHexString(iTmp));
		    }
		    return tmp.toString();
		  }
	  
		  
			public static byte[] hexStringToBytes(String hexString) {
				if (hexString == null || hexString.equals("")) {
					return null;
				}
				hexString = hexString.replace(" ", "");
				hexString = hexString.toUpperCase();
				int length = hexString.length() / 2;
				char[] hexChars = hexString.toCharArray();
				byte[] d = new byte[length];
				for (int i = 0; i < length; i++) {
					int pos = i * 2;
					d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
				}
				return d;
			}

			private static byte charToByte(char c) {
				return (byte) "0123456789ABCDEF".indexOf(c);
			}
		  
			
			
			
			private static byte[] intToByteArray(final int integer) {
				int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
				byte[] byteArray = new byte[4];

				for (int n = 0; n < byteNum; n++)
				byteArray[3 - n] = (byte) (integer>>> (n * 8));

				return (byteArray);
				}
			
			public static int byteArrayToInt(byte[] b, int offset) {
			       int value= 0;
			       for (int i = 0; i < 4; i++) {
			           int shift= (4 - 1 - i) * 8;
			           value +=(b[i + offset] & 0x000000FF) << shift;
			       }
			       return value;
			 }
			private static byte[] getBytes (char[] chars) {
				   Charset cs = Charset.forName ("UTF-8");
				   CharBuffer cb = CharBuffer.allocate (chars.length);
				   cb.put (chars);
				                 cb.flip ();
				   ByteBuffer bb = cs.encode (cb);
				  
				   return bb.array();

				 }
			
			private static char[] getChars (byte[] bytes) {
			      Charset cs = Charset.forName ("UTF-8");
			      ByteBuffer bb = ByteBuffer.allocate (bytes.length);
			      bb.put (bytes);
			                 bb.flip ();
			       CharBuffer cb = cs.decode (bb);
			  
			   return cb.array();
			}
			
			public static String byteToString(byte b) {   
				byte high, low;   
				byte maskHigh = (byte)0xf0;   
				byte maskLow = 0x0f;   
				  
				high = (byte)((b & maskHigh) >> 4);   
				low = (byte)(b & maskLow);   
				  
				StringBuffer buf = new StringBuffer();   
				buf.append(findHex(high));   
				buf.append(findHex(low));   
				  
				return buf.toString();   
				} 
			
			public static String byteToString(byte[] b) {   
				byte maskHigh = (byte)0xf0;   
				byte maskLow = 0x0f;   
				StringBuffer buf = new StringBuffer();   
				for (int i = 0; i < b.length; i++) { 
					byte high, low;  
					high = (byte)((b[i] & maskHigh) >> 4);   
					low = (byte)(b[i] & maskLow);   
					buf.append(findHex(high));   
					buf.append(findHex(low));   
					buf.append(" "); 
				}
				return buf.toString();   
			} 
			
			private static char findHex(byte b) {   
				int t = new Byte(b).intValue();   
				t = t < 0 ? t + 16 : t;   
				  
				if ((0 <= t) &&(t <= 9)) {   
				return (char)(t + '0');   
				}   
				  
				return (char)(t-10+'A');   
				}  
			
		  public static void main(String[] args) {  
	
			  byte[] a = {(byte) 0XF0,(byte) 0XC0};
			  System.out.println(Bytes2HexString(a));
			  System.out.println(Bytes2TenString(a));
			  System.out.println(byteToString(a));

		  }  
}
