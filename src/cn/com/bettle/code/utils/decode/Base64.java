package cn.com.bettle.code.utils.decode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
* @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
* @category
* <b>功能：</b>Base64工具类
 * 
 * */ 
public class Base64 {
	public static String encode(String text) {
		String s = new BASE64Encoder().encode(text.getBytes());
		return s;
	}

	public static String decode(String text) {
		byte[] buffer = new byte[0];
		try {
			buffer = new BASE64Decoder().decodeBuffer(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = "";
		try {
			s = new String(buffer, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {
		String test = "111";
		String encode = encode(test);
		System.out.println(encode);
		System.out.println(decode("ZjEzMjY5OWU3ODI2NGM4N2ZmZjU5YmIyNmI3ZjYwZTk="));
	}
}
