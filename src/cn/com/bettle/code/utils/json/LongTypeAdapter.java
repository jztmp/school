package cn.com.bettle.code.utils.json;

import java.lang.reflect.Type;  
import java.sql.Timestamp;  
import java.text.DateFormat;  
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
  
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;  
import com.google.gson.JsonDeserializer;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonParseException;  
import com.google.gson.JsonPrimitive;  
import com.google.gson.JsonSerializationContext;  
import com.google.gson.JsonSerializer;  
  
public class LongTypeAdapter implements JsonSerializer<Long>, JsonDeserializer<Long>{  
 
    public JsonElement serialize(Long src, Type arg1, JsonSerializationContext arg2) {  
        long date = src.longValue();  
        return new JsonPrimitive(date);  
    }  
  
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {  
        if (!(json instanceof JsonPrimitive)) {  
            throw new JsonParseException("The date should be a string value");  
        }  
  
        try {  
            long date = Long.parseLong(json.getAsString());  
            return date;  
        } catch (Exception e) {  
            throw new JsonParseException(e);  
        }  
    }  
    

  
}  

