package cn.com.bettle.code.database.nosql.redis.cache;

import java.util.List;

import cn.com.bettle.code.database.nosql.redis.springdata.RedisSpringBaseDAO;



public class RedisSpringCacheManger<T> extends RedisSpringBaseDAO<T> implements RedisCache<T> {
	

	@Override
	public List<T> getRedisCacheInfo(String key) {
		return this.get(key,true);
	}

	@Override
	public boolean setRedisCacheInfo(String key, T value) {
		return this.insert(key, value, true);
	}
	
	@Override
	public boolean setRedisCacheInfo(String key, String json) {
		return this.insert(key, json, true);
	}
	
	

}