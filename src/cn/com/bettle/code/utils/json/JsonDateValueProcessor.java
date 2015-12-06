package cn.com.bettle.code.utils.json;   
  
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
  
/**  
 *   
 * @author John  
 * @date 2008-11-22 上午10:54:19  
 * @version 1.0  
 */  
public class JsonDateValueProcessor implements JsonValueProcessor {      
         
    private String format = "yyyy-MM-dd HH:mm:ss";  //默认为这个模式    
     
    public JsonDateValueProcessor() {      
     
    }      
     
    public JsonDateValueProcessor(String format) {      
        this.format = format;      
    }      
     
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {      
        String[] obj = {};      
        if (value instanceof java.sql.Date[]) {      
            SimpleDateFormat sf = new SimpleDateFormat(format);      
            java.sql.Date[] dates = (java.sql.Date[]) value;      
            obj = new String[dates.length];      
            for (int i = 0; i < dates.length; i++) {  
            	java.util.Date myDate = new Date( ((java.sql.Date) dates[i]).getTime()); 
                obj[i] = sf.format(myDate);      
            }      
        }
        if (value instanceof java.sql.Timestamp[]) {      
            SimpleDateFormat sf = new SimpleDateFormat(format);      
            java.sql.Timestamp[] dates = (java.sql.Timestamp[]) value;      
            obj = new String[dates.length];      
            for (int i = 0; i < dates.length; i++) { 
            	java.util.Date myDate = new Date( ((java.sql.Timestamp) dates[i]).getTime()); 
                obj[i] = sf.format(myDate);      
            }      
        }  
        if (value instanceof java.util.Date[]) {      
            SimpleDateFormat sf = new SimpleDateFormat(format);      
            java.util.Date[] dates = (java.util.Date[]) value;      
            obj = new String[dates.length];      
            for (int i = 0; i < dates.length; i++) {      
                obj[i] = sf.format(dates[i]);      
            }      
        }  
        return obj;      
    }      
     
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {  
    	java.util.Date myDate = null;
    	////System.out.println("________________________"+key);
    	////System.out.println("________________________"+value.getClass().toString());
        if (value instanceof java.sql.Date) {  
        	myDate = new Date( ((java.sql.Date) value).getTime()); 
            String str = new SimpleDateFormat(format).format(myDate);      
            return str;      
        } 
        if( value instanceof java.sql.Timestamp ){   
        	myDate = new Date( ((java.sql.Timestamp) value).getTime());   
            String str = new SimpleDateFormat(format).format(myDate);      
            return str;      
        }
        if( value instanceof java.util.Date  ){   
            String str = new SimpleDateFormat(format).format((java.util.Date) value);      
            return str;      
        } 
        return value == null ? null : value.toString();      
    }      
     
    public String getFormat() {      
        return format;      
    }      
     
    public void setFormat(String format) {      
        this.format = format;      
    }      
     
}    