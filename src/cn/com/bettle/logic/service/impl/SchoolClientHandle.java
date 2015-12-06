package cn.com.bettle.logic.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchoolClientHandle implements IoHandler {

	protected final  Logger logger =  LoggerFactory.getLogger(SchoolClientHandle.class);


   
    /**  
    * 接收到消息时调用的方法，也就是用于接收消息的方法，一般情况下，message 是一个  
    * IoBuffer 类，如果你使用了协议编解码器，那么可以强制转换为你需要的类型。  
    */   
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
        String messageStr = message.toString();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        String dateStr = format.format(new Date());

        logger.info(messageStr + "\t" + dateStr);

        Collection<IoSession> sessions = session.getService().getManagedSessions().values();
        for(IoSession tempSession : sessions){
            tempSession.write(messageStr + "\t" + dateStr);
        }
    }
    
    /**  
    * 当发送消息成功时调用这个方法，注意这里的措辞，发送成功之后，  
    * 也就是说发送消息是不能用这个方法的。  
    */   
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
        logger.info("服务器成功发送信息: " + message.toString());
    }
    
    /**  
    * 对于TCP 来说，连接被关闭时，调用这个方法。  
    * 对于UDP 来说，IoSession 的close()方法被调用时才会毁掉这个方法。  
    */   
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        logger.info("链接将关闭:"+session.getId());
        CloseFuture future = session.close(true);
        future.addListener(new IoFutureListener(){
            public void operationComplete(IoFuture future){
                if(future instanceof CloseFuture){
                    ((CloseFuture)future).setClosed();
                    logger.info("have do the future set to closed");
                }
            }
        });
    }

    /**  
    * 这个方法当一个Session 对象被创建的时候被调用。对于TCP 连接来说，连接被接受的时候  
    * 调用，但要注意此时TCP 连接并未建立，此方法仅代表字面含义，也就是连接的对象  
    * IoSession 被创建完毕的时候，回调这个方法。  
    * 对于UDP 来说，当有数据包收到的时候回调这个方法，因为UDP 是无连接的。  
    */   
    @Override 
    public void sessionCreated(IoSession session) throws Exception {  
        logger.info("服务端与客户端创建连接...");  
    }  
    /**  
    * 这个方法在连接被打开时调用，它总是在sessionCreated()方法之后被调用。对于TCP 来  
    * 说，它是在连接被建立之后调用，你可以在这里执行一些认证操作、发送数据等。  
    */   
    @Override 
    public void sessionOpened(IoSession session) throws Exception {  
        logger.info("服务端与客户端连接打开...");  
    }  

   
      
    /**  
    * 这个方法在IoSession 的通道进入空闲状态时调用，对于UDP 协议来说，这个方法始终不会  
    * 被调用。  
    */   
    @Override 
    public void sessionIdle(IoSession session, IdleStatus status)  
            throws Exception {  
        logger.info("服务端进入空闲状态...");  
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
	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}  

}
