package cn.com.bettle.logic.entity;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.bettle.code.utils.date.DateUtils;
import cn.com.bettle.code.utils.json.JsonGsonSerializeUtil;

/**

 * User: 80071491
 
 * Date: 2008-10-31
 
 * Time: 13:46:39
 
 */
 
public class GPRMC {
 
    /*
 
    <1> UTC时间，hhmmss（时分秒）格式
 
    <2> 定位状态，A=有效定位，V=无效定位
 
    <3> 纬度ddmm.mmmm（度分）格式（前面的0也将被传输）
 
    <4> 纬度半球N（北半球）或S（南半球）
 
    <5> 经度dddmm.mmmm（度分）格式（前面的0也将被传输）
 
    <6> 经度半球E（东经）或W（西经）
 
    <7> 地面速率（000.0~999.9节，前面的0也将被传输）
 
    <8> 地面航向（000.0~359.9度，以真北为参考基准，前面的0也将被传输）
 
    <9> UTC日期，ddmmyy（日月年）格式
 
    <10> 磁偏角（000.0~180.0度，前面的0也将被传输）
 
    <11> 磁偏角方向，E（东）或W（西）
 
    <12> 模式指示（仅NMEA0183 3.00版本输出，A=自主定位，D=差分，E=估算，N=数据无效）
 
     */
 
    private Date gpsDate;
 
    //private boolean valid;
 
    private double latitude;    // n + s -
 
    private double longitude;   // e + w -
 
    private double altitude;
 
    private double speed;             // km/h
    
    private double direct;
    
    private String studentId;
    
    private Date studentData;
 

 

 

 
   
 

 

 

 
    public Date getGpsDate() {
		return gpsDate;
	}



	public void setGpsDate(Date gpsDate) {
		this.gpsDate = gpsDate;
	}



	public double getLatitude() {
		return latitude;
	}



	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}



	public double getLongitude() {
		return longitude;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}



	public double getAltitude() {
		return altitude;
	}



	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}



	public double getSpeed() {
		return speed;
	}



	public void setSpeed(double speed) {
		this.speed = speed;
	}



	public double getDirect() {
		return direct;
	}



	public void setDirect(double direct) {
		this.direct = direct;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public Date getStudentData() {
		return studentData;
	}



	public void setStudentData(Date studentData) {
		this.studentData = studentData;
	}


	class MyStringTokenizer{
		String[] str;
		int size;
		int index = 0;
		
		
		public MyStringTokenizer(String a, String b){
			 str = a.split(b);
			 size = str.length;
		}
		
		public String nextToken(){
			String returnStr="";
			if(index<size){
				returnStr = str[index];
				index++;
			}
			return returnStr;
		}
		
		
	}

	/*
	 * 解析串口接收到的字节串（RMC）推荐定位信息
	 * $GPRMC,013946,A,3202.1855,N,11849.0769,E,0.05,218.30,111105,4.5,W,A*20..
	 * $GPRMC,<1> ,2,<3> ,4,<5> ,6,<7> ,<8> ,<9> ,10,11,12*hh<CR><LF>
	 * <1>UTC时间，hhmmss（时分秒）格式 <2> 定位状态，A=有效定位，V=无效定位
	 * <3>纬度ddmm.mmmm（度分）格式（前面的0也将被传输） <4> 纬度半球N（北半球）或S（南半球）
	 * <5>经度dddmm.mmmm（度分）格式（前面的0也将被传输） <6> 经度半球E（东经）或W（西经）
	 * <7>地面速率（000.0~999.9节，前面的0也将被传输） <8> 地面航向（000.0~359.9度，以真北为参考基准，前面的0也将被传输）
	 * <9> UTC日期，ddmmyy（日月年）格式 <10> 磁偏角（000.0~180.0度，前面的0也将被传输）
	 * <11>磁偏角方向，E（东）或W（西） <12> 模式指示（仅NMEA0183 3.00版本输出，A=自主定位，D=差分，E=估算，N=数据无效）
	 * 
	 * 
	 * 返回值 0 正确 1校验失败 2非GPRMC信息 3无效定位 4格式错误 5校验错误
	 */
	public int parseGPRMC(String by) throws ParseException
	{
		String time = "";
		String cyear = DateUtils.getYearP();
		if(by==null||by.isEmpty())//判断非空
			return 4;
/*		if(checksum(by.getBytes())==false)//计算校验和并与语句中的校验和比较
			return 5;*/
		MyStringTokenizer str=new MyStringTokenizer(by,",");
		String temp=null;
		temp=str.nextToken();//取第一个子串即标记 0
		if(!temp.equals("$GPRMC"))// 确定是$GPRMC
			return 2;

		temp=str.nextToken();// 时间 1
		if(temp.length()>0){
		int hour=(short)Integer.parseInt(temp.substring(0,2));
		int minute=(short)Integer.parseInt(temp.substring(2,4));
		int second=(short)Float.parseFloat(temp.substring(4));
		time = hour+":"+minute+":"+second;
		}
		temp=str.nextToken();// 定位状态 2
		if(temp.length()!=1)//无
			return 4;
		else if(temp.charAt(0)=='V')//为A则有效 为V则无效
			return 3;
		temp=str.nextToken();// 纬度 3
		if(temp.length()>0){
			latitude=Double.parseDouble(temp.substring(0,2));// 纬度-度
			latitude+=Double.parseDouble(temp.substring(2))/60;// 纬度-分
		}
		temp=str.nextToken();// 纬度半球 4
		if(temp.length()!=1)
			return 4;
		else if(temp.charAt(0)=='N')
			latitude = latitude;
		else if(temp.charAt(0)=='S')
			latitude = -latitude;
		else   //错误信息
			return 4;
		temp=str.nextToken();// 经度 5
		if(temp.length()>0){
		longitude=Double.parseDouble(temp.substring(0,3));// 经度-度
		longitude+=Double.parseDouble(temp.substring(3))/60;// 经度-分
		}
		temp=str.nextToken();// 经度半球 6
		if(temp.length()!=1)
			return 4;
		else if(temp.charAt(0)=='E')
			longitude = longitude;
		else if(temp.charAt(0)=='W')
			longitude = -longitude;
		else
			return 4;
		temp=str.nextToken();// 地面速率 7
		if(!temp.isEmpty())
		{
			speed=Double.parseDouble(temp)*1.852;//速度单位转换为千米每小时
		}
		temp=str.nextToken();// 方位角度 8
		if(!temp.isEmpty()){
			direct = Double.parseDouble(temp);
		}
		temp=str.nextToken();// UTC日期 9
		if(temp.length()>0){
			int day=(short)Integer.parseInt(temp.substring(0,2));
			int month=(short)Integer.parseInt(temp.substring(2,4));
			int year=(short)Float.parseFloat(cyear+temp.substring(4));
			time = day+"-"+month+"-"+year+" "+time;
		}
		gpsDate = DateUtils.stringToDate(time,"dd-MM-yyyy HH:mm:ss");
		
		int whe = by.indexOf("@");
		if(whe>-1){
			String time2 = "";
			String aa = by.substring(whe,by.length());
			MyStringTokenizer str2=new MyStringTokenizer(aa,",");
			temp=str2.nextToken();// @SMS
			if(!temp.equals("@SMS"))
				return 4;
			temp=str2.nextToken();// student id
			if(!temp.isEmpty()){
				studentId = temp;
			}
			temp=str2.nextToken();// student time
			if(temp.length()>0){
				int hour=(short)Integer.parseInt(temp.substring(0,2));
				int minute=(short)Integer.parseInt(temp.substring(2,4));
				int second=(short)Float.parseFloat(temp.substring(4));
				time2 = hour+":"+minute+":"+second;
			}
			temp=str2.nextToken();// student date
			if(temp.length()>0){
				int day=(short)Integer.parseInt(temp.substring(0,2));
				int month=(short)Integer.parseInt(temp.substring(2,4));
				int year=(short)Float.parseFloat(cyear+temp.substring(4));
				time2 = day+"-"+month+"-"+year+" "+time2;
			}
			studentData = DateUtils.stringToDate(time2,"MM-dd-yyyy HH:mm:ss");
		}
		
		
		//MyStringTokenizer str2=new MyStringTokenizer(,",");
		return 0;
	}

	

private boolean checksum(byte[] b)
	{
		byte chk=0;// 校验和
		byte cb=b[1];// 当前字节
		int i=0;
		if(b[0]!='$')
			return false;
		for(i=2;i<b.length;i++)//计算校验和
		{
			if(b[i]=='*')
				break;
			cb=(byte)(cb^b[i]);
		}
		if(i!=b.length-3)//校验位不正常
			return false;
		i++;
		byte[] bb=new byte[2];//用于存放语句后两位
		bb[0]=b[i++];bb[1]=b[i];
		try
		{
			chk=(byte)Integer.parseInt(new String(bb),16);//后两位转换为一个字节
		}
		catch(Exception e)//后两位无法转换为一个字节，格式错误
		{
			return false;
		}
		System.out.println("校验信息");
		System.out.println("    原文："+chk);
		System.out.println("    计算："+cb);

		return chk==cb;//计算出的校验和与语句的数据是否一致
	}
    
	public static void main(String[] args) throws ParseException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		//System.out.println(longToString(220226,"HH:mm:ss"));
        String by = "$GPRMC,205326.000,A,3441.8319,N,08635.1028,W,0.00,87.17,230913,,*20,@SMS,0002,042040,092413";
        GPRMC gprmc = new GPRMC();
        int i = gprmc.parseGPRMC(by);
        if(i==0){
        	System.out.println(JsonGsonSerializeUtil.bean2Json(gprmc));
        }else{
        	System.out.println(i);
        }
        
	}
} 
