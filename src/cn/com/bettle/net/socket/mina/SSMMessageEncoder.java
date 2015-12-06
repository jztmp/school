package cn.com.bettle.net.socket.mina;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

public class SSMMessageEncoder implements ProtocolEncoder {

	Charset charset = Charset.forName("UTF-8");
	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput output)
			throws Exception {
/*		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);

        buf.putString(message.toString(), charset.newEncoder());

        buf.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
        buf.flip();

        output.write(buf);*/
        
        String str = message.toString();
        byte[] data = str.getBytes("GBK");
        IoBuffer buff =IoBuffer.allocate(2+data.length).setAutoExpand(true);
        buff.putShort((short)data.length);
        buff.put(data);
        buff.flip();
        output.write(buff);
        

        

	}

}
