package cn.com.bettle.net.socket.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringDecoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringEncoder;

public class ServerProtocolCodec implements ProtocolCodecFactory {

	private final PrefixedStringEncoder encoder;
	private final PrefixedStringDecoder decoder;

	public ServerProtocolCodec() {
		this(Charset.forName("utf-8"));
	}

	public ServerProtocolCodec(Charset charset) {
		encoder = new PrefixedStringEncoder(charset); //new TextLineEncoder(charset, LineDelimiter.UNIX);
		decoder = new PrefixedStringDecoder(charset);
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

}
