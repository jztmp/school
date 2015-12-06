package cn.com.bettle.code.utils.bytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import cn.com.bettle.code.exception.BytesOperationException;

/**
 * @author <b>Name:<b> John <br>
 *         <b>Mail:<b><br>
 *         曾徐明0027005113/Partner/zte_ltd<br>
 *         zxmapple@gmail.com <br>
 *         343816974@qq.com
 * @category b>功能：</b>字节处理工具类
 * 
 * 
 * 
 * 
 * */

/*
 * 
 * 1个字节是8位 只有8种基本类型可以算.其他引用类型都是由java虚拟机决定的自己不能操作 byte 1字节 short 2字节 int 4字节 long
 * 8字节 float 4字节 double 8字节 char 2字节 boolean 1字节
 */

public class BytesUtils {

	public static byte[] string2byte(String st, String charsetName)
			throws UnsupportedEncodingException {
		return st.getBytes(charsetName);
	}

	public static String byte2string(byte[] src, String charsetName)
			throws UnsupportedEncodingException {
		return new String(src, charsetName);
	}

	public static byte[] bytesCopy(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	public static byte[] int2bytes(int x) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(x);
		return buffer.array();
	}

	public static int bytes2int(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(bytes);
		buffer.flip();// need flip
		return buffer.getInt();
	}

	public static int bytes2int(byte[] bytes,int size)throws BytesOperationException {
		if(size>4){
			throw new BytesOperationException("BytesUtils::bytes2int More than 4 of size");
		}
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.put(bytes);
		buffer.flip();// need flip
		return buffer.getInt();
	}
	
	public static byte[] long2bytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(x);
		return buffer.array();
	}

	public static long bytes2long(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes);
		buffer.flip();// need flip
		return buffer.getLong();
	}

	public static final byte[] int2byteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}

	public static byte[] concatByteArray(byte[] a, byte[] b) {

		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static byte[] getBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(
				ByteOrder.nativeOrder());
		buffer.putInt(value);
		return buffer.array();
	}

	public static long getUnsignedInt(int x) {
		return x & 0x00000000ffffffffL;
	}

	/**
	 * 将流另存为文件
	 * 
	 * @param is
	 * @param outfile
	 */
	public static void streamSaveAsFile(InputStream is, File outfile) {
		FileOutputStream fos = null;
		try {
			File file = outfile;
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
				fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new RuntimeException(e2);
			}
		}
	}

	/**
	 * Read an input stream into a string
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String streamToString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static byte[] stream2Byte(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = is.read(b, 0, b.length)) != -1) {
			baos.write(b, 0, len);
		}
		byte[] buffer = baos.toByteArray();
		return buffer;
	}

	/**
	 * @方法功能 InputStream 转为 byte
	 * @param InputStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] inputStream2Byte(InputStream inStream)
			throws Exception {
		// ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		// byte[] buffer = new byte[1024];
		// int len = -1;
		// while ((len = inStream.read(buffer)) != -1) {
		// outSteam.write(buffer, 0, len);
		// }
		// outSteam.close();
		// inStream.close();
		// return outSteam.toByteArray();
		int count = 0;
		while (count == 0) {
			count = inStream.available();
		}
		byte[] b = new byte[count];
		inStream.read(b);
		return b;
	}

	/**
	 * @方法功能 byte 转为 InputStream
	 * @param 字节数组
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream byte2InputStream(byte[] b) throws Exception {
		InputStream is = new ByteArrayInputStream(b);
		return is;
	}

	/**
	 * @功能 短整型与字节的转换
	 * @param 短整型
	 * @return 两位的字节数组
	 */
	public static byte[] shortToByte(short number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/**
	 * @功能 字节的转换与短整型
	 * @param 两位的字节数组
	 * @return 短整型
	 */
	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);// 最低位
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * @方法功能 整型与字节数组的转换
	 * @param 整型
	 * @return 四位的字节数组
	 */
	public static byte[] intToByte(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}

	/**
	 * @方法功能 字节数组和整型的转换
	 * @param 字节数组
	 * @return 整型
	 */
	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	/**
	 * @方法功能 字节数组和长整型的转换
	 * @param 字节数组
	 * @return 长整型
	 */
	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();
			// 将最低位保存在最低位
			temp = temp >> 8;
			// 向右移8位
		}
		return b;
	}

	/**
	 * @方法功能 字节数组和长整型的转换
	 * @param 字节数组
	 * @return 长整型
	 */
	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff; // s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// TODO Auto-generated method stub
			ByteBuffer buffer = ByteBuffer.allocate(40);
			byte[] a = int2bytes(40);
			buffer.put(a);
			byte[] a2 = int2bytes(1);
			buffer.put(a2);
			String content = "Hello world!";
			byte[] cn = content.getBytes("ISO-8859-1");
			buffer.put(cn);
			buffer.flip();
			byte[] a1 = buffer.array();
			byte[] b1 = bytesCopy(a1, 0, 4);
			byte[] b2 = bytesCopy(a1, 4, 4);
			byte[] b3 = bytesCopy(a1, 8, cn.length);
			//byte[] b2 = bytesCopy(a, 0, 4);
			System.out.println(bytes2int(b1));
			System.out.println(bytes2int(b2));
			System.out.println(byte2string(b3, "ISO-8859-1"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
