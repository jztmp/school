package cn.com.bettle.net.socket.mina;

import java.io.UnsupportedEncodingException;

import cn.com.bettle.code.utils.bytes.BytesUtils;


public class BettleProtocalPack {
    private int packageLength;
    private byte flag;
    /*包头大小*/
    public int packageHead = 4;
    private String content;
    //由于要发送的数据本身就是byte[]类型的。如果将其通过UTF-8编码转换为中间件String类型就会出现问题
    public String charsetName="ISO-8859-1";
    
    
    public BettleProtocalPack(){
        
    }
    
    public BettleProtocalPack(byte flag,String content){
        this.flag=flag;
        this.content=content;
        int len1=content==null?0:content.getBytes().length;
        this.packageLength=len1;
    }
    
    public BettleProtocalPack(byte[] bs) throws UnsupportedEncodingException{  
    	int begin = packageHead+1;
        if(bs!=null && bs.length>=begin){  
            packageLength=BytesUtils.bytes2int(BytesUtils.bytesCopy(bs, 0, packageHead));  
            flag=bs[4];  
            
            content=new String(BytesUtils.bytesCopy(bs, begin, packageLength-begin),charsetName);  
        }  
    }  
    
    public int getLength() {
        return packageLength;
    }
    public void setLength(int length) {
        this.packageLength = length;
    }
    public byte getFlag() {
        return flag;
    }
    public void setFlag(byte flag) {
        this.flag = flag;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    public String toString(){
        StringBuffer sb=new StringBuffer();
        sb.append(" Len:").append(packageLength);
        sb.append(" flag:").append(flag);
        sb.append(" content:").append(content);
        return sb.toString();
    }
}
