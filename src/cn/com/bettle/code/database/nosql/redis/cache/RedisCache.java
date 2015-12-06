package cn.com.bettle.code.database.nosql.redis.cache;

import java.util.List;

public interface RedisCache<T> {
	
	public  List<T> getRedisCacheInfo(String key);

	public  boolean setRedisCacheInfo(String key, T value);
	
	public boolean setRedisCacheInfo(String key, String json);
}
