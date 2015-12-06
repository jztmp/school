package cn.com.bettle.code.database.nosql.redis.jedis;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;


public class RedisJedisPoolFactoryBean extends AbstractFactoryBean<RedisJedisPool> {
	  
	private ShardedJedisPool  shardedPool;
	
	private JedisPool pool;
	
	     @Override
	     public Class<?> getObjectType() {
	         return RedisJedisPool.class;
	     }
	 
	     @Override
	     protected RedisJedisPool createInstance() throws Exception {
	    	 RedisJedisPool jpool = new RedisJedisPool();
	    	 jpool.setPool(pool);
	    	 jpool.setShardedPool(shardedPool);
	         return jpool;
	     }
	     
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
