package cn.com.bettle.net.socket.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BettleTestLineDecoder implements ProtocolDecoder {

 /*
	char beginchar = (char)02;
	String messagestr = "";
	*/
	protected final Logger log = LoggerFactory
			.getLogger(BettleTestLineDecoder.class);
	Charset charset = Charset.forName("UTF-8");
	IoBuffer buf = IoBuffer.allocate(1024*1000).setAutoExpand(true);
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput output)
			throws Exception {
		/*
		int indexbegin;
		String s = in.getHexDump();
		*/
		while(in.hasRemaining()){
			byte b = in.get();	
			buf.put(b);
		}
		buf.flip();
		byte[] bytes = new byte[buf.limit()];
		buf.get(bytes);
		String message = new String(bytes,charset);		
		log.info(message);
		
		
		
		output.write(message);
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
