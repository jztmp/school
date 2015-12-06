package cn.com.bettle.code.database.nosql.mongodb.bugu;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.com.bettle.code.annotation.FileEntity;
import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.exception.DBException;

import com.bugull.mongo.AdvancedDao;
import com.bugull.mongo.BuguQuery;
import com.bugull.mongo.fs.BuguFS;
import com.mongodb.MongoClientOptions;



/**
 * <b>功能:</b>泛型(T extends SimpleEntity)的高级dao对应bugumongo的AdvancedDao 提供MapReduce功能，以及基于MapReduce实现的一些统计功能</br>
 * 
 * <b>查看:https://code.google.com/p/bugumongo/wiki/AdvancedDao</b>
 * @param <T>
 * */	
public  class MongoBuguBaseDAO<T> extends AdvancedDao<T>{

	public Class<T> entityClass;
	
	public BuguFS buguFS;
	

	public MongoBuguBaseDAO(Class<T> clazz) {
		super(clazz);
		this.entityClass = clazz;
		// TODO Auto-generated constructor stub
		if(entityClass.isAnnotationPresent(FileEntity.class)){
			FileEntity fileEntityAnnotation = (FileEntity)entityClass.getAnnotation(FileEntity.class);
			String bucketName = fileEntityAnnotation.bucketName();
			long chunkSize = fileEntityAnnotation.chunkSize();
			if(StringUtils.isBlank(bucketName) && chunkSize==0){
				buguFS = new BuguFS();
			}else if(StringUtils.isBlank(bucketName) && chunkSize!=0){
				buguFS = new BuguFS(chunkSize);
			}else if(!StringUtils.isBlank(bucketName) && chunkSize!=0){
				buguFS = new BuguFS(bucketName,chunkSize);
			}else if(!StringUtils.isBlank(bucketName) && chunkSize==0){
				buguFS = new BuguFS(bucketName);
			}
		}else{
			buguFS = new BuguFS();
		}
	}

	public List<T> loadList(BuguQuery<T> q) throws DBException {
		return q.results();
	}
	
	public T loadOne(BuguQuery<T> q) throws DBException {
		return q.result();
	}

	public PageInfo<T> loadPageInfo(BuguQuery<T> q,PageInfo<T> pageInfo,long count) throws DBException {
		if(count==0){
			count =  q.count();
		}
		Long total = count;
		List<T> list = q.pageNumber(pageInfo.getPageNum()).pageSize(pageInfo.getPageSize()).results();
		pageInfo.setRows(list);
		pageInfo.setTotal(total);
		return pageInfo;
	}
	

/*	public  void test() {
		BuguQuery<T> q = this.query();
		q.and(
		  q.and("name", "=", "John")
		   .and("old", ">", "10")
		   .and("", "", "")
		   .and("", "", "")
		   .and("", "", "")
		   .and("", "", "")
		).or(
          q.and("", "", "")		
				
		);
		T t = q.result();
		
	}*/
}