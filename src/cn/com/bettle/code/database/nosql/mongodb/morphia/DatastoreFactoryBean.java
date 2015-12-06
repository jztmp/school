package cn.com.bettle.code.database.nosql.mongodb.morphia;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.Datastore;  
import com.mongodb.Mongo;
import org.springframework.beans.factory.config.AbstractFactoryBean;  
import org.springframework.util.StringUtils;  


public class DatastoreFactoryBean extends AbstractFactoryBean<Datastore> {
	      
	      private Morphia morphia;    //morphia实例，最好是单例
	      private Mongo mongo;    //mongo实例，最好是单例
	      private String dbName;    //数据库名
	      private String username;    //用户名，可为空
	      private String password;    //密码，可为空
	      private boolean toEnsureIndexes=false;    //是否确认索引存在，默认false
	      private boolean toEnsureCaps=false;    //是否确认caps存在，默认false
	     
	 
	     @Override
	     protected Datastore createInstance() throws Exception {
	         //这里的username和password可以为null，morphia对象会去处理
	         Datastore ds = morphia.createDatastore(mongo, dbName, username,
	                 password==null?null:password.toCharArray());
	         if(toEnsureIndexes){
	             ds.ensureIndexes();
	         }
	         if(toEnsureCaps){
	             ds.ensureCaps();
	         }
	         return ds;
	     }
	 
	     @Override
	     public Class<?> getObjectType() {
	         return Datastore.class;
	     }
	 
	     @Override
	     public void afterPropertiesSet() throws Exception {
	         super.afterPropertiesSet();
	         if (mongo == null) {
	             throw new IllegalStateException("mongo is not set");
	         }
	         if (morphia == null) {
	             throw new IllegalStateException("morphia is not set");
	         }
	     }
	     
	     public void setMorphia(Morphia morphia) {  
	         this.morphia = morphia;  
	     }  
	   
	     public void setMongo(Mongo mongo) {  
	         this.mongo = mongo;  
	     }  
	   
	     public void setDbName(String dbName) {  
	         this.dbName = dbName;  
	     }  
	   
	     public void setUsername(String username) {  
	         this.username = username;  
	     }  
	   
	     public void setPassword(String password) {  
	         this.password = password;  
	     }

		public boolean isToEnsureIndexes() {
			return toEnsureIndexes;
		}

		public void setToEnsureIndexes(boolean toEnsureIndexes) {
			this.toEnsureIndexes = toEnsureIndexes;
		}

		public boolean isToEnsureCaps() {
			return toEnsureCaps;
		}

		public void setToEnsureCaps(boolean toEnsureCaps) {
			this.toEnsureCaps = toEnsureCaps;
		}

		public Morphia getMorphia() {
			return morphia;
		}

		public Mongo getMongo() {
			return mongo;
		}

		public String getDbName() {
			return dbName;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}  
	     
	     
	 }