package cn.com.bettle.logic.service.thread;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.utils.date.DateUtils;
import cn.com.bettle.code.utils.serial.ConsumerRead;
import cn.com.bettle.code.utils.serial.DSerialPort;
import cn.com.bettle.code.utils.tools.BasicUitl;


public class SchoolConsumerRead extends ConsumerRead{
	
	    private final Logger log = LoggerFactory.getLogger(SchoolConsumerRead.class);
	    
	    
		@Override
		public void run() {
			 try{
				  String st = this.message;
				  if(st!=null){
					  st = st.trim();
					  System.out.println("*******************"+st+"*******************");
					  if(("OK".endsWith(st.toUpperCase()) || st.toUpperCase().indexOf("OK")>-1) && st.toUpperCase().indexOf("WHERE")<0){
						  try {
							  
							mSerialPort.setOk();
							System.out.println("*******************setOk()*******************");
							//删除上次短信猫里的短信
							String lastMessage = mSerialPort.consumeMsgList();
							if(StringUtils.isNotBlank(lastMessage)){
								if(lastMessage.toUpperCase().equals("AT")){
									//清空短信猫的缓存
									String sms = "AT+CMGD=1,4 "+StringEscapeUtils.unescapeJava("\r");
									mSerialPort.produceWriteMsg(sms);
								}else{
									Thread.currentThread().sleep(5000);
									String sms = "AT+CMGD="+lastMessage+" "+StringEscapeUtils.unescapeJava("\r");
									mSerialPort.produceWriteMsg(sms);
									System.out.println("*******************删除短信： "+sms+"*******************");	
								}

							}
							
						} catch (InterruptedException e) {
							throw new ServiceException(e);
						}
						  
					  }else if(st.startsWith("+CMTI: \"SM\"") || st.indexOf("\"SM\"")>-1){
						  String[] sta = st.split(",");
						  String smsNumber =sta[sta.length-1];
						  String sms = "AT+CMGR="+smsNumber+" "+StringEscapeUtils.unescapeJava("\r");
						  try {
							mSerialPort.produceMsgList(smsNumber);
						} catch (InterruptedException e1) {
							log.error("保存短信猫短信号失败",e1);
						}
						  try {
								mSerialPort.produceWriteMsg(sms);
						} catch (InterruptedException e) {
							throw new ServiceException(e);
						}
					  }else if(st.startsWith("+CMGR") || st.indexOf("+CMGR")>-1){
						  PostMethod method = null;
						  try
						  {
						    String[] smsNumber = st.split(",\"");
						    if(smsNumber.length==3){
						    String TEL = smsNumber[1].substring(0,smsNumber[1].length()-2);
							String BUS_NUMBER = smsNumber[2].split(":")[3];
							if(BUS_NUMBER.indexOf("OK")>-1){
								mSerialPort.setOk();
								//BUS_NUMBER = BUS_NUMBER.substring(0,BUS_NUMBER.indexOf("OK"));
								BUS_NUMBER = BasicUitl.replaceBlank(BUS_NUMBER.substring(0,BUS_NUMBER.indexOf("OK")));
							}
							  //获取配置变量SERVICE_IP
							  String ip  = BettleApplicationContext.getPropertieContext("variables", "SERVICE_IP");
							  HttpClient client = new HttpClient();  
						      method = new PostMethod( "http://"+ip+"/mapguide/api/execute" );  
						      NameValuePair SERVICENAME_PARAM = new NameValuePair( "SERVICENAME_PARAM" , "SCHOOL_SERVICE" );
						      NameValuePair ACTIV_PARAM = new NameValuePair( "ACTIV_PARAM" , "getLocation" );  
						      NameValuePair PARAMES_PARAM = new NameValuePair( "PARAMES_PARAM" , "{'BUS_NUMBER':"+BUS_NUMBER+"}" );  
						      method.setRequestBody( new NameValuePair[] { SERVICENAME_PARAM,ACTIV_PARAM,PARAMES_PARAM});  
						      try {
								client.executeMethod(method);
							} catch (HttpException e) {
								throw new ServiceException(e);
							} catch (IOException e) {
								throw new ServiceException(e);
							}   //打印服务器返回的状态   
						      System.out.println(method.getStatusLine());   //打印结果页面  
						      String response = "";
							try {
								response = new String(method.getResponseBodyAsString().getBytes("utf-8"));
							} catch (UnsupportedEncodingException e) {
								throw new ServiceException(e);
							} catch (IOException e) {
								throw new ServiceException(e);
							}  
						      if(StringUtils.isNotBlank(response)){
						          //打印返回的信息  
						    	  System.out.println(response); 
						    	  JSONObject jsonObject = JSONObject.fromObject(response); 
						    	  boolean success = jsonObject.getBoolean("success");
						    	  if(success){
						    		  JSONObject result = jsonObject.getJSONObject("result");
						    		//获取配置变量TIME_CORRECT
									  String timeCorrect  = BettleApplicationContext.getPropertieContext("variables", "TIME_CORRECT");
						    		  String utime = BasicUitl.nullToEmpty(result.getString("UTIME"));
						    		  if(!"".endsWith(utime)){
						    			  utime = DateUtils.dateToString(DateUtils.add(utime,Calendar.HOUR,Integer.parseInt(timeCorrect)),"yyyy-MM-dd HH:mm:ss");
						    		  }
						    		  String message = "Time : "+utime+"  Location: http://maps.google.com/maps?f=q&q="+BasicUitl.nullToEmpty(result.getString("LATITUDE"))+","+BasicUitl.nullToEmpty(result.getString("LONGITUDE"))+"&z=16";
						    		  //String sms = "AT+CMGS=\""+TEL+"\" "+StringEscapeUtils.unescapeJava("\\x0d".replace("\\x", "\\u00"))+" "+message+" "+StringEscapeUtils.unescapeJava("\\x1A".replace("\\x", "\\u00"));
						    		  String sms = "AT+CMGS="+TEL+StringEscapeUtils.unescapeJava("\r");  
						    		  try {
						    				  mSerialPort.produceWriteMsg(sms);
									} catch (InterruptedException e) {
										throw new ServiceException(e);
									}
					
						    		  String sms2 = message+StringEscapeUtils.unescapeJava("\\x1A".replace("\\x", "\\u00"))+StringEscapeUtils.unescapeJava("\r");
						    		  try {
						    				  mSerialPort.produceWriteMsg(sms2);
									} catch (InterruptedException e) {
										throw new ServiceException(e);
									}	
						    	  }
						      }
						  }
						  }catch(Exception e2){
							  throw new ServiceException(e2);
						  }finally{
							  if(method!=null){
								  method.releaseConnection(); 
							  }
						  }
						  
						      
					  }/*else if(st.startsWith("+CMGS")){
						  String[] smsArray = st.split(":");
						  String messageNumber = smsArray[1].trim();
						  if(StringUtils.isNotBlank(messageNumber)){
							  String sms = "AT+CMGD="+messageNumber;
							  try {
									mSerialPort.produceWriteMsg(sms);
								} catch (InterruptedException e) {
									throw new ServiceException(e);
								}
						  }
						  
						  
					  }*/
					  
					  
					
				    
				 }
			 }catch(ServiceException e){
				 log.error("SchoolConsumerRead : run  ::",e);
			 }
		}
		
		 public static void main(String args[]){
			 	String st = "+CMGR: \"REC UNREAD\",\"+12566569092\",,\"14/10/30,11:40:18-20\"Whereis:09-14\r\n\r\n\r\nOK";
			    String[] smsNumber = st.split(",\"");
			    String TEL = smsNumber[1].substring(0,smsNumber[1].length()-2);
				String BUS_NUMBER = smsNumber[2].split(":")[3];
				if(BUS_NUMBER.indexOf("OK")>-1){
					BUS_NUMBER = BasicUitl.replaceBlank(BUS_NUMBER.substring(0,BUS_NUMBER.indexOf("OK")));
				}
				System.out.println(BUS_NUMBER);
		 }
		
	}
