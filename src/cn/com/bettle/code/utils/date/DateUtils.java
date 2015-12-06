package cn.com.bettle.code.utils.date;

/**
* @author <b>Name:<b> John  <br> <b>Mail:<b> 曾徐明0027005113/Partner/zte_ltd, zxmapple@gmail.com
* @category
* <b>功能：</b>日期工具类，对日期进行格式化
 * 
 * */ 
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils {
	
	public static int getYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	
	public static String getYearP() {
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		return String.valueOf(year).substring(0,2);
	}
	
	public static int getMonth() {
		GregorianCalendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH) + 1;
		return month;
	}

	public static int getDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		int date = calendar.get(Calendar.DATE);
		return date;
	}

	public static int getDay() {
		GregorianCalendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static int getHours() {
		GregorianCalendar calendar = new GregorianCalendar();
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		return hours;
	}

	public static int getMinutes() {
		GregorianCalendar calendar = new GregorianCalendar();
		int min = calendar.get(Calendar.MINUTE);
		return min;
	}

	public static int getSeconds() {
		GregorianCalendar calendar = new GregorianCalendar();
		int sec = calendar.get(Calendar.SECOND);
		return sec;
	}

	public static int getMilliSeconds() {
		GregorianCalendar calendar = new GregorianCalendar();
		int millisec = calendar.get(Calendar.MILLISECOND);
		return millisec;
	}
	
	// dateformat:yyyy-MM-dd,yyyy-M-d,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd H:m:s
	public static String getFormatDate(Date date,String dateformat) { 
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		String time = format.format(date);
		return time;
	}
	
	public static String getFormatDate(Date date) {
		return getFormatDate(date,"yyyy-MM-dd");
	}
	
	// dateformat:yyyy-MM-dd,yyyy-M-d,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd H:m:s
	public static String getFormatNowDate(String dateformat) {
		Date now = new Date();
		return getFormatDate(now,dateformat);
	}
	
	public static String getFormatNowDate() {
		return getFormatNowDate("yyyy-MM-dd");
	}
	
	// dateformat:yyyy-MM-dd,yyyy-M-d,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd H:m:s
	public static Date getParseDateString(String dateStr,String dateformat){
		try{
			SimpleDateFormat format = new SimpleDateFormat(dateformat);
			Date date = format.parse(dateStr);
			return date;
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}
	
	public static Date getParseDateString(String dateStr){
		return getParseDateString(dateStr,"yyyy-MM-dd");
	}
	
	
  // date类型转换为String类型
  // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
  // data Date类型的时间
  public static String dateToString(Date data, String formatType) {
	  return new SimpleDateFormat(formatType).format(data);
  }
 
  // long类型转换为String类型
  // currentTime要转换的long类型的时间
  // formatType要转换的string类型的时间格式
  public static String longToString(long currentTime, String formatType)
  throws ParseException {
	  Date date = longToDate(currentTime, formatType); // long类型转成Date类型
	  String strTime = dateToString(date, formatType); // date类型转成String
	  return strTime;
  }
 
  // string类型转换为date类型
  // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
  // HH时mm分ss秒，
  // strTime的时间格式必须要与formatType的时间格式相同
  public static Date stringToDate(String strTime, String formatType)
  throws ParseException {
	  SimpleDateFormat formatter = new SimpleDateFormat(formatType);
	  Date date = null;
	  date = formatter.parse(strTime);
	  return date;
  }
 
  // long转换为Date类型
  // currentTime要转换的long类型的时间
  // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
  public static Date longToDate(long currentTime, String formatType)
  throws ParseException {
	  Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
	  String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
	  Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
	  return date;
  }
 
  // string类型转换为long类型
  // strTime要转换的String类型的时间
  // formatType时间格式
  // strTime的时间格式和formatType的时间格式必须相同
  public static long stringToLong(String strTime, String formatType)
  throws ParseException {
	  Date date = stringToDate(strTime, formatType); // String类型转成date类型
	  if (date == null) {
	  return 0;
	  } else {
	  long currentTime = dateToLong(date); // date类型转成long类型
	  return currentTime;
	  }
  }
 
  // date类型转换为long类型
  // date要转换的date类型的时间
  public static long dateToLong(Date date) {
	  return date.getTime();
  }
  
  
  public static Date add(String timeStirng,int filed, int number) throws ParseException{
	  	  DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      Date d = format.parse(timeStirng);  
	      Calendar c = Calendar.getInstance();  
	      c.setTime(d);  
	      c.add(filed, number);//属性很多也有月等等，可以操作各种时间日期   
	      return c.getTime();
  }
  
	public static void main(String[] args) throws ParseException {
/*		System.out.println(longToString(220226,"HH:mm:ss"));
		System.out.println(longToString(230913,"yyyy-MM-dd"));
		System.out.println(longToString(230913+220226,"yyyy-MM-dd HH:mm:ss"));*/
		System.out.println(dateToString(add("2014-11-30 3:59:06",Calendar.HOUR,-6),"yyyy-MM-dd HH:mm:ss"));
	}
}
