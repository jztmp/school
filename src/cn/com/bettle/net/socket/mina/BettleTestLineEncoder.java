package cn.com.bettle.net.socket.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

public class BettleTestLineEncoder implements ProtocolEncoder {

	Charset charset = Charset.forName("UTF-8");
	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput output)
			throws Exception {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);

        buf.putString(message.toString(), charset.newEncoder());

        buf.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
        buf.flip();

        output.write(buf);

	}

}
