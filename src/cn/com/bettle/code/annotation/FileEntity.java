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
public @interface FileEntity {
    String bucketName() default "";
    long chunkSize() default 0;
}
