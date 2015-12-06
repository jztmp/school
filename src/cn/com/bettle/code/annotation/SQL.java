package cn.com.bettle.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>功能：</b>修饰mongoDB中的文件（GridFS）,进行对象-文档映射
 * <b>注意：</b>该注解只能用来修饰类。
 * */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SQL {
	
	String namespace() default "";
    String loadList() default ""; 
    String loadOne() default ""; 
    String loadPageInfo() default ""; 
    String insert() default ""; 
    String delete() default ""; 
    String update() default ""; 
    
    public static final int LOADLIST = 0;
    public static final int LOADONE = 1;
    public static final int LOADPAGEINFO = 2;
    public static final int INSERT = 3;
    public static final int DELETE = 4;
    public static final int UPDATE = 5;
}
