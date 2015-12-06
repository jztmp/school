package cn.com.bettle.code.database.nosql.mongodb.bugu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.bugull.mongo.BuguConnection;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class BuguFactoryBean extends AbstractFactoryBean<BuguConnection> {

    // 表示服务器列表(主从复制或者分片)的字符串数组
    private String[] serverStrings;
    // mongoDB配置对象
    private MongoOptions mongoOptions;
    // 是否主从分离(读取从库)，默认读写都在主库
    private boolean readSecondary = false;
    // 设定写策略(出错时是否抛异常)，默认采用SAFE模式(需要抛异常)
    private WriteConcern writeConcern = WriteConcern.SAFE;
    
    private String dbName;
    
    private String username;
    
    private String password;
	
     @Override
     protected BuguConnection createInstance() throws Exception {
    	 BuguConnection conn = BuguConnection.getInstance();
    	 
    	 MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
         builder.connectionsPerHost(mongoOptions.getConnectionsPerHost());
         builder.autoConnectRetry(mongoOptions.isAutoConnectRetry());
         builder.connectTimeout(mongoOptions.getConnectTimeout());
         builder.threadsAllowedToBlockForConnectionMultiplier(mongoOptions.getThreadsAllowedToBlockForConnectionMultiplier());
         builder.maxAutoConnectRetryTime(mongoOptions.getMaxAutoConnectRetryTime());
         builder.cursorFinalizerEnabled(mongoOptions.isCursorFinalizerEnabled());
         builder.maxWaitTime(mongoOptions.getMaxWaitTime());
         builder.socketTimeout(mongoOptions.getSocketTimeout());
         builder.socketKeepAlive(mongoOptions.isSocketKeepAlive());
        
         //writeConcern = mongoOptions.getWriteConcern();
         
         
         MongoClientOptions options = builder.build();
         
         List<ServerAddress> serverList = getServerList();

         if(!StringUtils.isBlank(username) && !StringUtils.isBlank(password))
         {
        	 conn.setReplicaSet(serverList).setDatabase(dbName).setUsername(username).setPassword(password).setOptions(options).connect();
         }
         else{
        	 conn.setReplicaSet(serverList).setDatabase(dbName).setOptions(options).connect();
         }
         // 设定主从分离
         if (readSecondary) {
        	 conn.setReadPreference(ReadPreference.secondaryPreferred());
         }
         /*conn.getDB().setWriteConcern(writeConcern);*/
         
         return conn;
     }
 
     @Override
     protected void destroyInstance(BuguConnection instance) throws Exception {
    	//关闭数据库连接
         BuguConnection.getInstance().close();  
    	 super.destroyInstance(instance);
     }
     
     /**
      * 根据服务器字符串列表，解析出服务器对象列表
      * <p>
      * 
      * @Title: getServerList
      *         </p>
      * 
      * @return
      * @throws Exception
      */
     private List<ServerAddress> getServerList() throws Exception {
         List<ServerAddress> serverList = new ArrayList<ServerAddress>();
         try {
             for (String serverString : serverStrings) {
                 String[] temp = serverString.split(":");
                 String host = temp[0];
                 if (temp.length > 2) {
                     throw new IllegalArgumentException(
                             "Invalid server address string: " + serverString);
                 }
                 if (temp.length == 2) {
                     serverList.add(new ServerAddress(host, Integer
                             .parseInt(temp[1])));
                 } else {
                     serverList.add(new ServerAddress(host));
                 }
             }
             return serverList;
         } catch (Exception e) {
             throw new Exception(
                     "Error while converting serverString to ServerAddressList",
                     e);
         }
     }
     
     @Override
     public Class<?> getObjectType() {
         return BuguConnection.class;
     }


	public String[] getServerStrings() {
		return serverStrings;
	}


	public void setServerStrings(String[] serverStrings) {
		this.serverStrings = serverStrings;
	}


	public MongoOptions getMongoOptions() {
		return mongoOptions;
	}


	public void setMongoOptions(MongoOptions mongoOptions) {
		this.mongoOptions = mongoOptions;
	}


	public boolean isReadSecondary() {
		return readSecondary;
	}


	public void setReadSecondary(boolean readSecondary) {
		this.readSecondary = readSecondary;
	}


	public WriteConcern getWriteConcern() {
		return writeConcern;
	}


	public void setWriteConcern(WriteConcern writeConcern) {
		this.writeConcern = writeConcern;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

     
     
 }