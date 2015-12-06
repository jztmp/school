package cn.com.bettle.code.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface NeedRedisCached {
	
	long expire() default 0;
	int cachType() default 0;
	String namespace() default "";
	
	public static int COMMON = 0;
	public static int MAP = 1;
	public static int LIST = 2;
}
