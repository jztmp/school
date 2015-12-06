package cn.com.bettle.net.socket.mina;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 断包和粘包处理，处理后的消息为一个或多个完整的数据消息
 * @author blc
 */
public class MessageDecoder extends CumulativeProtocolDecoder {
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache
     * .mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer,
     * org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        
        in.order(ByteOrder.LITTLE_ENDIAN);    //字节序, ServerConfig.ByteEndian = ByteOrder.LITTLE_ENDIAN
        
        //消息buf
        IoBuffer buf = IoBuffer.allocate(1024);   //ServerConfig.MessageMaxByte 最大消息字节数
        buf.order(ByteOrder.LITTLE_ENDIAN);
        
        //考虑以下几种情况：
        //    1. 一个ip包中只包含一个完整消息
        //    2. 一个ip包中包含一个完整消息和另一个消息的一部分
        //    3. 一个ip包中包含一个消息的一部分
        //    4. 一个ip包中包含两个完整的数据消息或更多（循环处理在父类的decode中）
        if (in.remaining() > 1) {
            int length = in.getShort(in.position());
            if (length < 4) {
                throw new RuntimeException("Error net message. (Message Length="+length+")");
            }
            if (length > 10240) {
                throw new RuntimeException("Error net message. Message Length("+length+") > MessageMaxByte(10240)");
            }
            if (length > in.remaining()) return false;
            //复制一个完整消息
            byte[] bytes = new byte[length];
            in.get(bytes);
            buf.put(bytes);
            
            buf.flip();
            out.write(buf);
            return true;
        } else {
            return false;
        }
    }
}