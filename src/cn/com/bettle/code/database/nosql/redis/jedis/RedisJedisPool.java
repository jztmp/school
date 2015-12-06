package cn.com.bettle.code.database.nosql.redis.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

public class RedisJedisPool {

	private ShardedJedisPool  shardedPool;
	
	private JedisPool pool;

	public ShardedJedisPool getShardedPool() {
		return shardedPool;
	}

	public void setShardedPool(ShardedJedisPool shardedPool) {
		this.shardedPool = shardedPool;
	}

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}
	
	
}
