package cn.com.bettle.net.socket.mina;

import java.nio.charset.Charset;  
import org.apache.mina.core.session.IoSession;  
import org.apache.mina.filter.codec.ProtocolCodecFactory;  
import org.apache.mina.filter.codec.ProtocolDecoder;  
import org.apache.mina.filter.codec.ProtocolEncoder;  
public class BettleProtocalCodecFactory   implements ProtocolCodecFactory {  
        private final BettleProtocalEncoder encoder;  
        private final BettleProtocalDecoder decoder;  
          
        public BettleProtocalCodecFactory(Charset charset) {  
            encoder=new BettleProtocalEncoder(charset);  
            decoder=new BettleProtocalDecoder(charset);  
        }  
           
        public ProtocolEncoder getEncoder(IoSession session) {  
            return encoder;  
        }  
        public ProtocolDecoder getDecoder(IoSession session) {  
            return decoder;  
        }  
          
}  
