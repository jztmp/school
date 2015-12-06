package cn.com.bettle.logic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.bettle.net.entity.AttachmentBlob;

@Repository
public interface AttachmentBlobDao {

	// ///////////////////////////////
	// ///// 增加 ////////
	// ///////////////////////////////

	/**
	 * @param att
	 * @return
	 */
	public int addAttachmentBlob(AttachmentBlob att);

	// ///////////////////////////////
	// ///// 删除 ////////
	// ///////////////////////////////

	/**
	 * @param attachmentId
	 * @return
	 */
	public int deleteAttachmentBlob(@Param("attachmentId") String attachmentId);

	// ///////////////////////////////
	// ///// 查询////////
	// ///////////////////////////////

	/**
	 * @param attachmentId
	 * @return
	 */
	public AttachmentBlob getAttachmentBlobById(@Param("attachmentId") String attachmentId);

	public List<AttachmentBlob> getAttachmentBlobList(@Param("offset") long offset,
			@Param("endRowNum") long endRowNum, @Param("attachmentId") String attachmentId);

	public long getAttachmentBlobCount();
}
