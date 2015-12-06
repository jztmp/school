package cn.com.bettle.code.utils.clazz;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.apache.commons.lang.ClassUtils;

/**
* @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
* @category
* <b>功能：</b>Class处理工具类
 * 
 * */ 

public class ClassUtils2 extends  ClassUtils{
   
    /***************************************************************************
     * 判断对象c实现的所有接口中是否有szInterface 
     * @param c 
     * @param szInterface
     * @return boolean
     */
    public static boolean isInterface(Class c, String szInterface)
    {
            Class[] face = c.getInterfaces();

            for (int i = 0, j = face.length; i < j; i++) 
            {
                    if(face[i].getName().equals(szInterface.trim()))
                    {
                            return true;
                    }
                    else
                    { 
                            Class[] face1 = face[i].getInterfaces();
                            for(int x = 0; x < face1.length; x++)
                            {
                                    if(face1[x].getName().equals(szInterface))
                                    {
                                            return true;
                                    }
                                    else if(isInterface(face1[x], szInterface))
                                    {
                                            return true;
                                    }
                            }
                    }
            }
            if (null != c.getSuperclass())
            {
                    return isInterface(c.getSuperclass(), szInterface);
            }
            return false;
    }  
    
    /***************************************************************************
     * 获取泛型的实际类对象
     * @param Type 泛型类型
     * @param int  类的层级 一般为0一层
     * @return Class 
     */
    public static Class getClass(Type type, int i) {     
        if (type instanceof ParameterizedType) { // 处理泛型类型     
            return getGenericClass((ParameterizedType) type, i);     
        } else if (type instanceof TypeVariable) {     
            return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象     
        } else {// class本身也是type，强制转型     
            return (Class) type;     
        }     
    }  
    
    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {     
        Object genericClass = parameterizedType.getActualTypeArguments()[i];     
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型     
            return (Class) ((ParameterizedType) genericClass).getRawType();     
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型     
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();     
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象     
            return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);     
        } else {     
            return (Class) genericClass;     
        }     
    }   
}
