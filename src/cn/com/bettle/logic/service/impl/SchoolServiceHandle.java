package cn.com.bettle.logic.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.appcontext.factory.ServiceFactory;
import cn.com.bettle.code.service.CService;
import cn.com.bettle.code.service.IService;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;
import cn.com.bettle.code.utils.tools.BasicUitl;
import cn.com.bettle.logic.service.ISchoolService;
import cn.com.bettle.net.controller.ApiController;

public class SchoolServiceHandle extends CService implements IoHandler {

	protected final Logger logger = LoggerFactory
			.getLogger(SchoolServiceHandle.class);

	ISqlMapDAO mySqlMapDao;
	ISqlMapDAO memorySqlMapDao;

	public SchoolServiceHandle(ISqlMapDAO _mySqlMapDao,
			ISqlMapDAO _memorySqlMapDao) {
		mySqlMapDao = _mySqlMapDao;
		memorySqlMapDao = _memorySqlMapDao;
	}

	/**
	 * 接收到消息时调用的方法，也就是用于接收消息的方法，一般情况下，message 是一个 IoBuffer
	 * 类，如果你使用了协议编解码器，那么可以强制转换为你需要的类型。
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void messageReceived(IoSession session, Object message)
			throws Exception {
//%%Honda,A,150210170205,N3441.8265W08635.1276,000,270,F100,47000000,108..
		String messageStr = message.toString();
		Collection<IoSession> sessions = session.getService()
				.getManagedSessions().values();

		int messagelength = messageStr.length();
		// 判断格式 最后有回车换行符 
		int indexbegin = messageStr.indexOf("%%");
		int indexend = messageStr.indexOf("\r\n");		
		if (indexbegin == 0 && indexend == (messagelength - 2)) {
			messageStr = messageStr.substring(2, messagelength - 2);
			
			

			ISchoolService service = ServiceFactory.getService("SCHOOL_SERVICE", this.bettleApplicationContext,false);
			service.initSqlMapDAO();
			service.saveGPS2(messageStr);
			
			
		}
	

		
		 
		 

	}

	/**
	 * 当发送消息成功时调用这个方法，注意这里的措辞，发送成功之后， 也就是说发送消息是不能用这个方法的。
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("服务器成功发送信息: " + message.toString());
	}

	/**
	 * 对于TCP 来说，连接被关闭时，调用这个方法。 对于UDP 来说，IoSession 的close()方法被调用时才会毁掉这个方法。
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("链接将关闭:" + session.getId());
		CloseFuture future = session.close(true);
		future.addListener(new IoFutureListener() {
			public void operationComplete(IoFuture future) {
				if (future instanceof CloseFuture) {
					((CloseFuture) future).setClosed();
					logger.debug("have do the future set to closed");
				}
			}
		});
	}

	/**
	 * 这个方法当一个Session 对象被创建的时候被调用。对于TCP 连接来说，连接被接受的时候 调用，但要注意此时TCP
	 * 连接并未建立，此方法仅代表字面含义，也就是连接的对象 IoSession 被创建完毕的时候，回调这个方法。 对于UDP
	 * 来说，当有数据包收到的时候回调这个方法，因为UDP 是无连接的。
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.debug("服务端与客户端创建连接...");
		
	}

	/**
	 * 这个方法在连接被打开时调用，它总是在sessionCreated()方法之后被调用。对于TCP 来
	 * 说，它是在连接被建立之后调用，你可以在这里执行一些认证操作、发送数据等。
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("服务端与客户端连接打开...");
	}

	/**
	 * 这个方法在IoSession 的通道进入空闲状态时调用，对于UDP 协议来说，这个方法始终不会 被调用。
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.debug("服务端进入空闲状态...");
	}

	/**
	 * 这个方法在你的程序、Mina 自身出现异常时回调，一般这里是关闭IoSession。
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("服务端发送异常...", cause);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		logger.debug("数据流关闭...");
		CloseFuture future = session.close(true);
		future.addListener(new IoFutureListener() {
			public void operationComplete(IoFuture future) {
				if (future instanceof CloseFuture) {
					((CloseFuture) future).setClosed();
					logger.debug("have do the future set to closed");
				}
			}
		});
	}

}
