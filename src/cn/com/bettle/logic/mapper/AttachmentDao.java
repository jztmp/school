package cn.com.bettle.logic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.bettle.net.constant.AttachmentConstant;
import cn.com.bettle.net.constant.AttachmentConstant.Status;
import cn.com.bettle.net.entity.Attachment;

@Repository
public interface AttachmentDao {

	// ///////////////////////////////
	// ///// 增加 ////////
	// ///////////////////////////////

	/**
	 * 下一个值 获得
	 * 
	 * @return
	 */
	public long getNextVal();

	/**
	 * @param att
	 * @return
	 */
	public int addAttachment(Attachment att);

	// ///////////////////////////////
	// ///// 删除 ////////
	// ///////////////////////////////

	/**
	 * @param attachmentId
	 * @return
	 */
	public int deleteAttachment(@Param("attachmentId") String attachmentId);

	// ///////////////////////////////
	// ///// 修改////////
	// ///////////////////////////////

	/**
	 * @param attachmentId
	 * @param status
	 */
	public void updateStatusByAttachmentId(@Param("attachmentId") String attachmentId, @Param("status") Status status);

	public int updateDescriptionByAttachmentId(@Param("attachmentId") String attachmentId,
			@Param("description") String description, @Param("name") String name);

	public int updateAttachment(Attachment attachment);

	// ///////////////////////////////
	// ///// 查询////////
	// ///////////////////////////////

	/**
	 * @param attachmentId
	 * @return
	 */
	public Attachment getAttachmentById(@Param("attachmentId") String attachmentId);

	/**
	 * 
	 * @param kindId
	 * @param kind
	 * @param status
	 * @return
	 */
	public List<Attachment> getAttachmentsByKindId(@Param("kindId") String kindId,
			@Param("kind") AttachmentConstant.Kind kind, @Param("status") AttachmentConstant.Status status);

	/**
	 * @param folderId
	 * @return
	 */
	public int getAttachmentCountByKindId(@Param("kindId") String kindId, @Param("kind") AttachmentConstant.Kind kind,
			@Param("status") AttachmentConstant.Status status);

	public int getAttachmentCountByKind(@Param("kind") AttachmentConstant.Kind kind,
			@Param("status") AttachmentConstant.Status status);

}
