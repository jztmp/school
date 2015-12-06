package cn.com.bettle.code.utils.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.com.bettle.code.exception.SerializeException;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;

public class SerializeUtil {
	public static byte[] serialize(Object object)throws SerializeException {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
		// 序列化
		baos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		byte[] bytes = baos.toByteArray();
		return bytes;
		} catch (Exception e) {
		  throw new SerializeException(e.getMessage());
		}
	}
	 
	public static Object unserialize(byte[] bytes) throws SerializeException{
		ByteArrayInputStream bais = null;
		try {
		// 反序列化
		bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
		} catch (Exception e) {
			throw new SerializeException(e.getMessage());
		}
	}
	
	public static <T> T map2Bean(Map<String,Object> map,Class<T> _class){
		String json = JsonSerializeUtil.map2Json(map);
		return  JsonSerializeUtil.json2Bean(json, _class);
	}
	
	public static <T> Map<String,Object> bean2Map(T t){
		String json = JsonSerializeUtil.bean2Json(t);
		return  JsonSerializeUtil.json2Map(json);
	}
	
	

}
