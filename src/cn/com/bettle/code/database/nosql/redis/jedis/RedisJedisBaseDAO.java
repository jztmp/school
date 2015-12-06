/*
 *    Copyright 2010 The myBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.com.bettle.code.database.nosql.redis.jedis;

import cn.com.bettle.code.exception.NDBException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;


/**
 * <p>redis数据库Dao基于Jedis的实现</p>
 * <b>功能描述</b> 提供Redis数据库连接池，数据库连接池自动关闭，异常处理，	管道处理，分布式处理的封装	<br/>
 * <b>设计模式   ： 回调，动态代理</b>
 * @author John <br/>
 * <b>E-mail</b> : zxmapple@gmail.com
 * @version 0.1
 */
public  class RedisJedisBaseDAO {
	
	private ShardedJedisPool  shardedPool;
	
	private JedisPool pool;
	
	
	public void RedisJedisBaseDAO(){
		
	}
	
	/**
	 * <b>功能：</b> 分布式批处理使用分布式连接池获取ShardedJedisPipeline分布式管道流进行批处理<br/>
	 * <b>适用范围：</b> 适用于master/slave的分布式数据库结构
	 * @param ShardedJedisBatch  分布式批处理器
	 * @throws NDBException 
	 * */
	public  void  shardedBatchProcess(ShardedJedisBatch jedisBatch) throws NDBException{
		ShardedJedis jedis = shardedPool.getResource();  
		try
		{
			ShardedJedisPipeline pipelined = jedis.pipelined();
			Response[] response = jedisBatch.batch(pipelined);		
			pipelined.sync();
			jedisBatch.afterBatch(response);
		}catch(Exception e){
			if(jedis != null){
			shardedPool.returnBrokenResource(jedis);
			}
			throw new NDBException(e.getMessage(),e.getCause());
		}
		finally{
			if(jedis != null){
			shardedPool.returnResource(jedis);  
			}
		}	
	}

	
	/**
	 * <b>功能：</b> 单库批处理使用单库连接池获取pipelined管道流进行批处理<br/>
	 * <b>适用范围：</b> 适用于单个数据库
	 * @param JedisBatch  单库批处理器
	 * @throws NDBException 
	 * */
	public  void batchProcess(JedisBatch jedisBatch) throws NDBException{
		Jedis jedis = pool.getResource();   
		try
		{
		Pipeline pipelined = jedis.pipelined();
		Response[] response = jedisBatch.batch(pipelined);		
		pipelined.sync();
		jedisBatch.afterBatch(response);
		}catch(Exception e){
			if(jedis != null){
			pool.returnBrokenResource(jedis);
			}
			throw new NDBException(e.getMessage(),e.getCause());
		}
		finally{
			if(jedis != null){
			pool.returnResource(jedis);   
			}
		}	
	}

	
	/**
	 * <b>功能：</b> 分布式处理使用单库连接池获取ShardedJedis进行处理<br/>
	 * <b>适用范围：</b> 适用于单个数据库，数据操作不是批处理
	 * @param ShardedJedisProcess  单库处理器
	 * @throws NDBException 
	 * */	
	public  void shardedProcess(ShardedJedisProcess process) throws NDBException{
		ShardedJedis jedis = shardedPool.getResource();  
		try
		{
			process.process(jedis);
		}catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
		finally{
			if(jedis != null){
			shardedPool.returnResource(jedis);  	
			}
		}
	}
	
	/**
	 * <b>功能：</b> 单库处理使用单库连接池获取Jedis进行处理<br/>
	 * <b>适用范围：</b> 适用于单个数据库，数据操作不是批处理
	 * @param JedisProcess  单库处理器
	 * @throws NDBException 
	 * */	
	public  void process(JedisProcess process) throws NDBException{
		Jedis jedis = pool.getResource();  
		try
		{
			process.process(jedis);
		}catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
		finally{
			if(jedis != null){
			pool.returnResource(jedis);  
			}
		}
	}
	
	
	/**
	 * <b>功能：</b> 事务锁处理,当两个以上线程或进程同时访问库中的某个键时将该键锁住，避免出现脏写脏读的情况<br/>
	 * <b>适用范围：</b> 适用于单个数据库
	 * @param JedisTransactions  单库事务处理器
	 * @param String... 一个以上的键的名称（被锁住的键）
	 * @throws NDBException 
	 * */
	public  void transactionsProcess(JedisTransactions jedisTransactions,String... keys) throws NDBException{
		Jedis jedis = pool.getResource();  
		try
		{
			if(keys.length==0){
				throw new NDBException("No parameter name 'keys' method named 'transactionsProcess' ");
			}
			jedis.watch(keys);
			Transaction transaction = jedis.multi();
			Response[] response = jedisTransactions.transactions(jedis,transaction);
			transaction.exec();
			jedis.unwatch();
			jedisTransactions.afterTransactions(response);
		}catch(RuntimeException e){
			throw new NDBException(e.getMessage(),e.getCause());
		}catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
		finally{
			if(jedis != null){
			pool.returnResource(jedis);   
			}
		}
				
	}	 
			
	/**
	 * <b>功能：</b> 设置监听器<br/>
	 * <b>适用范围：</b> 单个数据库环境
	 * @param <T extends JedisPubSub>  发布/订阅 处理器
	 * @throws NDBException 
	 * */
	public <T extends JedisPubSub> void setListener(T listener,String... keys)throws NDBException{
		Jedis jedis = pool.getResource();  
		try
		{
			jedis.subscribe(listener, keys);
		}catch(Exception e){
			throw new NDBException(e.getMessage(),e.getCause());
		}
		finally{
			if(jedis != null){
			pool.returnResource(jedis);  
			}
		}
	} 
	





	/**
	 * <b>功能：</b> 分布式批处理器<br/>
	 * <b>适用范围：</b> 分布式数据库环境
	 * @author John 
	 * @version 0.1
	 * */
	public static abstract class ShardedJedisBatch {
		public abstract Response[] batch(ShardedJedisPipeline pipelined)throws Exception;
		
		public abstract void afterBatch(Response[] response)throws Exception;
	}
	
	
	/**
	 * <b>功能：</b> 单库批处理器<br/>
	 * <b>适用范围：</b> 单个数据库环境
	 * @author John 
	 * @version 0.1
	 * */
	public static abstract class JedisBatch {
		public abstract Response[] batch(Pipeline pipelined)throws Exception;
		
		public abstract void afterBatch(Response[] response)throws Exception;
	}

	
	/**
	 * <b>功能：</b> 单库处理器
	 * <b>适用范围：</b> 单个数据库环境
	 * @author John 
	 * @version 0.1
	 * */
	public static abstract class JedisProcess {
		public abstract void process(Jedis jedis)throws Exception;
	}

	
	/**
	 * <b>功能：</b> 分布式处理器</b>
	 * <b>适用范围：</b> 分布式数据库环境
	 * @author John 
	 * @version 0.1
	 * */
	public static abstract class ShardedJedisProcess {
		public abstract void process(ShardedJedis jedis)throws Exception;
	}

	
	/**
	 * <b>功能：</b> 事务锁处理器，当两个以上线程或进程同时访问库中的某个键时将该键锁住，避免出现脏写脏读的情况<br/>
	 * <b>适用范围：</b> 单个数据库环境
	 * @author John 
	 * @version 0.1
	 * */
	public static abstract class JedisTransactions {
		
		 public  abstract  Response[] transactions(Jedis jedis,Transaction transaction)throws Exception;
		 
		 public abstract void afterTransactions(Response[] response)throws Exception;
		
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
