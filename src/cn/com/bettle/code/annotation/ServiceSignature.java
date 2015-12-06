package cn.com.bettle.code.annotation;

public @interface ServiceSignature {
	String method();
	Class<?> type(); 
	Class<?>[] args(); 
}
