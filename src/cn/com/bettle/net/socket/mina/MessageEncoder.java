package cn.com.bettle.net.socket.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

/**
 * 译码器，不做任何事情
 */
public class MessageEncoder extends ProtocolEncoderAdapter {

	Charset charset = Charset.forName("UTF-8");
    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession, java.lang.Object, org.apache.mina.filter.codec.ProtocolEncoderOutput)
     */
    @Override
    public void encode(IoSession session, Object message,
            ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);

        buf.putString(message.toString(), charset.newEncoder());

        buf.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
        buf.flip();

        out.write(buf);
    }

}