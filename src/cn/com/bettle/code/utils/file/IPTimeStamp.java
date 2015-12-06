package cn.com.bettle.code.utils.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/*
* 生成上传文件名的类
* 规则：IP地址（长度：12）+年月日时分秒毫秒+三个随机数
* */
public class IPTimeStamp{
   private StringBuffer buf;
   private String ip;
   public IPTimeStamp(String ip){    //传入参数request.getRemoteAddr();即可
       this.ip = ip;

   }
   public String getRandomFileName(){   //取得文件随即名
       buf = new StringBuffer();
       SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmssSSS" );
       String[] ipadd = ip.split( "\\." );
       for(String ipa:ipadd){
           switch (ipa.length()){ //将IP地址转化为一个长度为12的字符串
               case 1:{
                   ipa= 00 +ipa;
                   break;
               }
               case 2:{
                   ipa= 0 +ipa;
                   break;
               }
           }
           buf.append(ipa);
       }
       buf.append(sdf.format(new Date()));
       Random ran = new Random();
       for(int i=0;i<3;i++){
           buf.append(ran.nextInt(10));
       }
       return buf.toString();
   }


} 