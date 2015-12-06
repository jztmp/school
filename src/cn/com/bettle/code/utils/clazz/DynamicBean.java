package cn.com.bettle.code.utils.clazz;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;


import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

/**
* @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
* @category
* <b>功能：</b>动态bean 工具类 提供对某个bean的反射开发
 * 
 * */ 
public class DynamicBean {

	/**
	  * 实体Object
	  */
	private  Object object = null;

	/**
	  * 属性map
	  */
	private  BeanMap beanMap = null;

	public DynamicBean() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public DynamicBean(Map propertyMap) {
	  this.object = generateBean(propertyMap);
	  this.beanMap = BeanMap.create(this.object);
	}


	/**
	  * 给bean属性赋值
	  * @param property 属性名
	  * @param value 值
	  */
	public void setValue(String property, Object value) {
	  beanMap.put(property, value);
	}

	/**
	  * 通过属性名得到属性值
	  * @param property 属性名
	  * @return 值
	  */
	public Object getValue(String property) {
	  return beanMap.get(property);
	}

	/**
	  * 得到该实体bean对象
	  * @return
	  */
	public Object getObject() {
	  return this.object;
	}

	/**
	 * 动态创建 新的javabean   BeanGenerator
	 * @param propertyMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object generateBean(Map propertyMap) {
	  BeanGenerator generator = new BeanGenerator();
	  Set keySet = propertyMap.keySet();
	  for (Iterator i = keySet.iterator(); i.hasNext();) {
	   String key = (String) i.next();
	   generator.addProperty(key, (Class) propertyMap.get(key));
	  }
	  return generator.create();
	}
	
	
	/**
	 * 在原来的bean中增加新的属性 javassit
	 * @param <T>
	 * */
	public static <T> Class<T> addClassField(Class<T>entityClass, Map<String,String> propertyMap)throws InstantiationException, IllegalAccessException, IntrospectionException, CannotCompileException, RuntimeException, NotFoundException, NoSuchMethodException, InvocationTargetException{
	      String className = entityClass.getName();
		  ClassPool pool = ClassPool.getDefault();
		  CtClass pt = pool.makeClass("JavaBeanTemplate",pool.get(className));
		  for(String key :  propertyMap.keySet()){
			  CtField fld = new CtField(pool.get(propertyMap.get(key)),
					  key, pt);
			  fld.setModifiers(Modifier.PUBLIC );
			  pt.addField(fld);
			  String methodName = StringUtils.capitalize(key.toLowerCase());
			  CtMethod methodGet = new CtMethod(pool.get(propertyMap.get(key)), "get"+methodName,null, pt);
			  methodGet.setBody("{return "+key+";}");
			  pt.addMethod(methodGet);
			  CtMethod methodSet = new CtMethod(CtClass.voidType, "set"+methodName,new CtClass[]{pool.get(propertyMap.get(key))}, pt);
			  methodSet.setBody("{this."+key+"=$1;}");
			  pt.addMethod(methodSet);
		  }
		  Class c=pt.toClass();
		return c;
	}
	
	public static <T> T addBeanField(T entity, Map<String,String> propertyMap)throws InstantiationException, IllegalAccessException, IntrospectionException, CannotCompileException, RuntimeException, NotFoundException, NoSuchMethodException, InvocationTargetException{
	      String className = entity.getClass().getName();
		  ClassPool pool = ClassPool.getDefault();
		  CtClass pt = pool.makeClass("JavaBeanTemplate",pool.get(className));
		  for(String key :  propertyMap.keySet()){
			  CtField fld = new CtField(pool.get(propertyMap.get(key)),
					  key, pt);
			  fld.setModifiers(Modifier.PUBLIC );
			  pt.addField(fld);
			  String methodName = StringUtils.capitalize(key.toLowerCase());
			  CtMethod methodGet = new CtMethod(pool.get(propertyMap.get(key)), "get"+methodName,null, pt);
			  methodGet.setBody("{return "+key+";}");
			  pt.addMethod(methodGet);
			  CtMethod methodSet = new CtMethod(CtClass.voidType, "set"+methodName,new CtClass[]{pool.get(propertyMap.get(key))}, pt);
			  methodSet.setBody("{this."+key+"=$1;}");
			  pt.addMethod(methodSet);
		  }
		  Class c=pt.toClass();
		  T bean = (T) c.newInstance();
		  BeanUtils.copyProperties(bean, entity);
		return bean;
	}
	
	/**
	 * 通过Introspector读取类中的属性
	 * 
	 * */	
	
	public static PropertyDescriptor[] getPropertyDescriptors(Class clazz) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
	    return beanInfo.getPropertyDescriptors();
	} 

	/**
	 * 反射读取类中的属性
	 * 
	 * */
	public static Field[] readProperty(Object object) throws IllegalArgumentException, IllegalAccessException{
		Class clazz = object.getClass();
        Field   fields[]   =   clazz.getDeclaredFields(); //使用java反射机制获取javabean中的私有属性
        Field.setAccessible(fields,   true); //是最为关键的一点
        return fields;
	}

	//-----------------------------------------------------------------------------------------//
    public static void main(String[] args){  
    try {   
		    	DynamicBean student = new DynamicBean(); 	  
		    	Field[]  fields1 =readProperty(student);	  
				  String[]   name1   =   new   String[fields1.length]; 
			      Object[]   value1   =   new   Object[fields1.length]; 
			                  for   (int   i   =   0;   i   <   name1.length;   i++)   { 
			                         name1[i]   =   fields1[i].getName(); 
			                         System.out.println(name1[i]   +   "-> "); 
			                         value1[i]   =   fields1[i].get(student); 
			                         System.out.println(value1[i]); 
			                  } 
			    System.out.println("================================================");              
		        // 设置类成员属性  
		        HashMap propertyMap = new HashMap();  
		        propertyMap.put("id", Class.forName("java.lang.Integer"));
		        propertyMap.put("name", Class.forName("java.lang.String"));  
		        propertyMap.put("address", Class.forName("java.lang.String"));  
		  
		        // 生成动态 Bean  
		        DynamicBean bean = new DynamicBean(propertyMap);  
		        //bean.addField(propertyMap);
		        // 给 Bean 设置值  
		        bean.setValue("id", new Integer(123));  
		  
		        bean.setValue("name", "454");  
		  
		        bean.setValue("address", "789");  
		  
		        // 从 Bean 中获取值，当然了获得值的类型是 Object  
		  
		        System.out.println("  >> id      = " + bean.getValue("id"));  
		  
		        System.out.println("  >> name    = " + bean.getValue("name"));  
		  
		        System.out.println("  >> address = " + bean.getValue("address"));  
		        System.out.println("================================================");
		        // 获得bean的实体  
		        Object object = bean.getObject();  
		        // 通过反射查看所有方法名  
		        Class clazz = object.getClass();  
		        Method[] methods = clazz.getDeclaredMethods();  
		        for (int i = 0; i < methods.length; i++) {  
		            System.out.println(methods[i].getName());   
		        }  
		        
		        
		        System.out.println("================================================");
				
				PropertyDescriptor[] pr;
		
					pr = getPropertyDescriptors(clazz);
					for(int i=0;i<pr.length;i++){
						System.out.println(pr[i].getName()+"="+pr[i].getPropertyType());
					}
		
					System.out.println("================================================");
					Field[]  fields = readProperty(object);
					  String[]   name   =   new   String[fields.length]; 
				      Object[]   value   =   new   Object[fields.length]; 
				                  for   (int   i   =   0;   i   <   name.length;   i++)   { 
				                         name[i]   =   fields[i].getName(); 
				                         System.out.println(name[i]   +   "-> "); 
				                         value[i]   =   fields[i].get(object); 
				                         System.out.println(value[i]); 
				                  } 

    	} catch (ClassNotFoundException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
}
