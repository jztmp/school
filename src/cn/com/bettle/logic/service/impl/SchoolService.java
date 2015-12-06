package cn.com.bettle.logic.service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.exception.SerialPortException;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.service.CService;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;
import cn.com.bettle.code.utils.decode.Base64;
import cn.com.bettle.code.utils.decode.MD5Util;
import cn.com.bettle.code.utils.json.JsonGsonSerializeUtil;
import cn.com.bettle.code.utils.serial.DSerialPort;
import cn.com.bettle.code.utils.tools.BasicUitl;
import cn.com.bettle.code.utils.uuid.UUIDHexGenerator;
import cn.com.bettle.logic.service.ISchoolService;
import cn.com.bettle.logic.service.thread.SchoolConsumerRead;
import cn.com.bettle.net.socket.mina.BettleTextLineCodecFactory;


@Service("SCHOOL_SERVICE")
public class SchoolService extends CService implements ISchoolService{

	protected final Logger log = LoggerFactory.getLogger(SchoolService.class);


	public void SchoolService(){
		
		String isLiunx = BettleApplicationContext.getPropertieContext("variables", "LINUX");
		if("false".equals(isLiunx)){
			// -- xp service run
			try {
				activeSerialPort();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// == xp service run
		}
	}
	
	public void init(){
		
		String isLiunx = BettleApplicationContext.getPropertieContext("variables", "LINUX");
		if("false".equals(isLiunx)){
			// -- xp service run
			try {
				activeSerialPort();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// == xp service run
		}
	}

	@Override
	public List getUser(HashMap params) throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			sqlMapDAO.insert("school.getUser", params);
			return null;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveParents(LinkedHashMap params) throws RuntimeException{
		try{
			encodePassword(params);
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			//Map<String,Object> map = sqlMapDAO.loadOne("school.getUserMaxId", params);
			String firstName = BasicUitl.nullToEmpty(params.get("FIRST_NAME"));
			String lastName = BasicUitl.nullToEmpty(params.get("LAST_NAME"));
			String userRealName = firstName +" "+ lastName;
			params.put("USER_REAL_NAME",userRealName);
			
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("USER_ID", mUUIDHexGenerator.generate());
			
			sqlMapDAO.insert("school.saveUser", params);
			return sqlMapDAO.insert("school.saveParents", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveStudent(LinkedHashMap params) throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("STUDENT_ID", mUUIDHexGenerator.generate());
			sqlMapDAO.insert("school.saveStudent", params);
			sqlMapDAO.insert("school.saveParentsStudent", params);
			return sqlMapDAO.insert("school.saveStudentSchool", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveDefaultCar(LinkedHashMap params) throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			String userId = (String)params.get("USER_ID");
			List cars = (List)params.get("cars");
			int ret = sqlMapDAO.delete("school.deleteUserDefaultCar", params);
			for (Object obj : cars){
				params.put("BUS_ID", obj);
				sqlMapDAO.insert("school.saveDefaultBus", params);
			}
			return ret;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveTeacher(LinkedHashMap params) throws RuntimeException{
		try{
			encodePassword(params);
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			//Map<String,Object> map = sqlMapDAO.loadOne("school.getUserMaxId", params);
			String firstName = BasicUitl.nullToEmpty(params.get("FIRST_NAME"));
			String lastName = BasicUitl.nullToEmpty(params.get("LAST_NAME"));
			String userRealName = firstName +" "+ lastName;
			params.put("USER_REAL_NAME",userRealName);
			
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("USER_ID", mUUIDHexGenerator.generate());
			sqlMapDAO.insert("school.saveUser", params);
			sqlMapDAO.insert("school.saveTeacher", params);
			return sqlMapDAO.insert("school.saveTeacherSchool", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveUser(LinkedHashMap params) throws RuntimeException {
		try{
			encodePassword(params);
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			//Map<String,Object> map = sqlMapDAO.loadOne("school.getUserMaxId", params);
			String firstName = BasicUitl.nullToEmpty(params.get("FIRST_NAME"));
			String lastName = BasicUitl.nullToEmpty(params.get("LAST_NAME"));
			String userRealName = firstName +" "+ lastName;
			params.put("USER_REAL_NAME",userRealName);
			
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("USER_ID", mUUIDHexGenerator.generate());
			return sqlMapDAO.insert("school.saveUser", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveSchool(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("SCHOOL_ID", mUUIDHexGenerator.generate());
			return sqlMapDAO.insert("school.saveSchool", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}	
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveBus(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			//Map map = sqlMapDAO.loadOne("school.getNewBusId", params);
			//params.putAll(map);
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("BUS_ID", mUUIDHexGenerator.generate());
			sqlMapDAO2.insert("school.saveBusMemory", params);
			return sqlMapDAO.insert("school.saveBus", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveBusRelation(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			return sqlMapDAO.insert("school.saveBusRelation", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteBusRelation(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			return sqlMapDAO.insert("school.deleteBusRelation", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteUser(LinkedHashMap params)throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
				sqlMapDAO.update("school.deleteTeacher", params);
				sqlMapDAO.update("school.deleteSchoolTeacher", params);
				sqlMapDAO.delete("school.deleteParents", params);
			return sqlMapDAO.delete("school.deleteUser", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteStudnets(LinkedHashMap params)throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
				sqlMapDAO.update("school.deleteStudnets", params);
				sqlMapDAO.update("school.deleteSchoolStudnets", params);
			return sqlMapDAO.update("school.deleteParentsStudnets", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteBus(LinkedHashMap params)throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			sqlMapDAO2.delete("school.deleteBus2", params);
			sqlMapDAO2.delete("school.deleteBus3", params);
			sqlMapDAO2.delete("school.deleteBus4", params);
			sqlMapDAO.update("school.deleteBusDriver", params);
			return sqlMapDAO.delete("school.deleteBus", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String,String> getAllDistance(LinkedHashMap params)throws RuntimeException{
		Map<String,String> map = new HashMap<String,String>();
		try{
			double dit = 0;
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			int subtime = Integer.parseInt(BettleApplicationContext.getPropertieContext("variables", "TIME_CORRECT"));
			params.put("SUBTIME", subtime);
			List<Map<String,Object>>list = sqlMapDAO.loadList("school.getBusHistoryForDistance", params);
			  for(int i=1;i<list.size();i++){
				  	Map<String,Object> map2 = list.get(i-1);
				  	Map<String,Object> map3 = list.get(i);
				  	double lon1 = Double.parseDouble(BasicUitl.transformNull(map2.get("LONGITUDE"),"0.0"));
				  	double lat1 = Double.parseDouble(BasicUitl.transformNull(map2.get("LATITUDE"),"0.0"));
				  	double lon2 = Double.parseDouble(BasicUitl.transformNull(map3.get("LONGITUDE"),"0.0"));
				  	double lat2 = Double.parseDouble(BasicUitl.transformNull(map3.get("LATITUDE"),"0.0"));
		            dit = dit +  getDistance( lon1, lat1, lon2,  lat2); 
		        }
			dit = Math.round(dit)*3.280839895*1000;
			map.put("DICT", String.valueOf(dit));
			return map;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveGPS(HashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			Map bus = sqlMapDAO2.loadOne("school.getBusMemory", params);
			
			params.putAll(bus);
			
			String orgString = BasicUitl.nullToEmpty(params.get("ORG_STRING"));
			//更新内存数据 BAUTO是历史数据客户端没来得及发送的数据。
			if(!orgString.equals("") && orgString.indexOf("BAUTO")<0 && orgString.indexOf("BVIBRATION")<0){
				sqlMapDAO2.delete("school.deletedGpsMemory", params);
				sqlMapDAO2.insert("school.saveGpsMemory", params);
			}
			if(params.containsKey("studentId") && params.get("studentId")!=null){
				
				Map params2 = sqlMapDAO.loadOne("school.getStudent", params);
				Map parents = sqlMapDAO.loadOne("school.getParents", params2);
				
				params.putAll(params2);
				
				UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
				params.put("FING_ID", mUUIDHexGenerator.generate());
				
				log.debug("params====="+params.toString());
				sqlMapDAO.insert("school.saveStudentFingerprint", params);
				if(parents!=null){
					log.debug("parents====="+parents.toString());
					String tel = BasicUitl.nullToEmpty(parents.get("MOBILE")) ;
					String message = "Student ID:"+BasicUitl.nullToEmpty(params2.get("STUDENT_ID"))+" --- Scan Time:" +BasicUitl.nullToEmpty(params.get("studentData"))+"---Location: http://maps.google.com/maps?f=q&q="+BasicUitl.nullToEmpty(params.get("latitude"))+","+BasicUitl.nullToEmpty(params.get("longitude"))+"&z=16" ;
					log.debug("message====="+message);
					http(message,tel);
					log.debug("message=====end");
				}
			}
			return sqlMapDAO.insert("school.saveGps", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveGpsMemory(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			Map map = sqlMapDAO2.loadOne("school.getBusMemoryByBUSID", params);
			if (map != null){
				String OldTime = BasicUitl.transformNull(map.get("UTIME"),"");
				String NewTime = BasicUitl.transformNull(params.get("gpsDate"),"");
				SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
				java.util.Date OldT = dfs.parse(OldTime);
				java.util.Date NewT = dfs.parse(NewTime);
				long between = NewT.getTime()-OldT.getTime();
				if(between>0){
					sqlMapDAO2.delete("school.deletedGpsMemory", params);
					sqlMapDAO2.insert("school.saveGpsMemory", params);
				}				
			}else{
				sqlMapDAO2.delete("school.deletedGpsMemory", params);
				sqlMapDAO2.insert("school.saveGpsMemory", params);
			}
						
			
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//将GPS跟踪数据发送给MapGuide服务器
	public void saveSendGpsToMemory(Map params) throws RuntimeException {
		try{
			  PostMethod method = null;
			 //获取配置变量SERVICE_IP
			  String ip  = BettleApplicationContext.getPropertieContext("variables", "MAPGUIDE_SERVICE_IP");
			  HttpClient client = new HttpClient();  
		      method = new PostMethod( "http://"+ip+"/mapguide/api/execute" );  
		      NameValuePair SERVICENAME_PARAM = new NameValuePair( "SERVICENAME_PARAM" , "SCHOOL_SERVICE" );
		      NameValuePair ACTIV_PARAM = new NameValuePair( "ACTIV_PARAM" , "saveGpsMemory" ); 
		      String  paramString = JsonGsonSerializeUtil.map2Json(params);
		      NameValuePair PARAMES_PARAM = new NameValuePair( "PARAMES_PARAM" , paramString );  
		      method.setRequestBody( new NameValuePair[] { SERVICENAME_PARAM,ACTIV_PARAM,PARAMES_PARAM});  
		      try {
				client.executeMethod(method);
			} catch (HttpException e) {
				throw new ServiceException(e);
			} catch (IOException e) {
				throw new ServiceException(e);
			}   //打印服务器返回的状态   
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveGPS2(String gpsString) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			
			/*解析gpsString 放到表里*/
			String arrays[] = gpsString.split(",");
			if (arrays[1].equals("A")){
				//%%Honda,A,150210170205,N3441.8265W08635.1276,000,270,F100,47000000,108..
				//转换日期
				String strdatetime = "20"+arrays[2].substring(0,2)+"-"+arrays[2].substring(2,4)+"-"+arrays[2].substring(4,6)+" "+arrays[2].substring(6,8)+":"+arrays[2].substring(8,10)+":"+arrays[2].substring(10,12);
				//转换定位
				String strLocation = arrays[3];
				int indexs = strLocation.indexOf("W");
				String strLat = strLocation.substring(1,indexs);
				String strLong = strLocation.substring(indexs+1,strLocation.length());	
				strLat = String.format("%.6f", Double.valueOf(strLat.substring(0,2))+Double.valueOf(strLat.substring(2,strLat.length()))/60);
				strLong = "-"+String.format("%.6f", Double.valueOf(strLong.substring(0,3))+Double.valueOf(strLong.substring(3,strLong.length()))/60);
				//转换速度单位  公里至英里
				String speed = String.format("%.2f", Double.valueOf(arrays[4])/1.609);

				Map params = new HashMap();
				params.put("IMEI", arrays[0]);
				Map bus = sqlMapDAO2.loadOne("school.getBusMemory", params);
				
				Map map = sqlMapDAO2.loadOne("school.getBusMemoryByBUSID", bus);
				if(map != null) {
					String OldTime = BasicUitl.transformNull(map.get("UTIME"),"");
					SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
					java.util.Date OldT = dfs.parse(OldTime);
					java.util.Date NewT = dfs.parse(strdatetime);
					long between = NewT.getTime()-OldT.getTime();					
					if(between>0){		
						params.putAll(bus);
						params.put("speed", speed);
						params.put("latitude", strLat);
						params.put("longitude", strLong);
						params.put("gpsDate", strdatetime);
						params.put("direct", arrays[5]);
						sqlMapDAO.insert("school.saveGps", params);
						saveSendGpsToMemory(params);
					}	
				}else{
					params.putAll(bus);
					params.put("speed", speed);
					params.put("latitude", strLat);
					params.put("longitude", strLong);
					params.put("gpsDate", strdatetime);
					params.put("direct", arrays[5]);
					
					
					//--分布式内存数据库存储这里就不存储了
					//sqlMapDAO2.delete("school.deletedGpsMemory", params);
					//sqlMapDAO2.insert("school.saveGpsMemory", params);
					sqlMapDAO.insert("school.saveGps", params);
					saveSendGpsToMemory(params);
				}
			}
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	
	//@PostConstruct
	@Transactional(propagation=Propagation.REQUIRED)
	public void initSchoolMemory() throws RuntimeException{
		try{
			init();
			//spring初始化该类后运行@PostConstruct注解的方法
			this.initSqlMapDAO();
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			Map params = new HashMap();
			List<Map<String,Object>> busList = sqlMapDAO.loadList("school.getBusList", params);
			for(int i=0;i<busList.size();i++){
				sqlMapDAO2.insert("school.saveBusMemory", busList.get(i));
			}
			List<Map<String,Object>> driverList = sqlMapDAO.loadList("school.getDriversList", params);
			for(int i=0;i<driverList.size();i++){
				sqlMapDAO2.insert("school.saveDriversMemory", driverList.get(i));
			}
			List<Map<String,Object>> driverBusList = sqlMapDAO.loadList("school.getDriversBusList", params);
			for(int i=0;i<driverBusList.size();i++){
				sqlMapDAO2.insert("school.saveDriversBusMemory", driverBusList.get(i));
			}
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//@PreDestroy  
	public void endSchoolMemory()throws Exception{
		//当Spring销毁后会跑这个注解对应的方法，销毁用户自己的某些线程等占用资源
	}
	
	private DSerialPort mReadSerialPort;
	private DSerialPort mWriteSerialPort;
	
	public void activeSerialPort() throws ServiceException{
		
		if(mWriteSerialPort == null){
			String com = BettleApplicationContext.getPropertieContext("variables", "W_COM");
			int delayTime = Integer.parseInt(BettleApplicationContext.getPropertieContext("variables", "DELAY"));
			mWriteSerialPort = new DSerialPort();  
			try {
				mWriteSerialPort.selectPort(com,115200,delayTime);
			} catch (SerialPortException e2) {
				log.error("Serial creation failed con't set  "+com+" :: ",e2);
				throw new ServiceException(e2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("Serial creation failed con't set  "+com+" :: ",e);
				throw new ServiceException(e);
			}  
			mWriteSerialPort.setHandle(SchoolConsumerRead.class);
			try {
				mWriteSerialPort.read();
			} catch (SerialPortException e) {
				log.error("Serial communication reader failed to start restart it could not accept the news :: "+e.getMessage());
			}
			try {
				mWriteSerialPort.writeStart();
			} catch (Exception e) {
				log.error("Serial communication listener failed to start writing ::"+e.getMessage());
			}
			
			//启动时清除短信猫缓存
			try{
				Thread.currentThread().sleep(2*1000);
				String sms = "AT"+StringEscapeUtils.unescapeJava("\r");
				mWriteSerialPort.produceWriteMsg(sms);
				Thread.currentThread().sleep(5*1000);
				String sms2 = "AT+CMGD=1,4 "+StringEscapeUtils.unescapeJava("\r");
				mWriteSerialPort.produceWriteMsg(sms2);
			}catch (Exception e) {
				log.error("Serial communication listener failed to start writing ::"+e.getMessage());
			}
		}
		
		if(mReadSerialPort == null){
			String readWrite = BettleApplicationContext.getPropertieContext("variables", "READ_MESSAGE_TO_WRITE");
			String com = BettleApplicationContext.getPropertieContext("variables", "R_COM");
			int delayTime = Integer.parseInt(BettleApplicationContext.getPropertieContext("variables", "DELAY"));
			mReadSerialPort = new DSerialPort();  
			try {
				mReadSerialPort.selectPort(com,115200,delayTime);
			} catch (SerialPortException e2) {
				log.error("Serial creation failed con't set  "+com+" :: ",e2);
				throw new ServiceException(e2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("Serial creation failed con't set  "+com+" :: ",e);
				throw new ServiceException(e);
			}  
			mReadSerialPort.setHandle(SchoolConsumerRead.class);
			//如果读串口的信息在返回请求结果时用写串口发送则将写串口对象赋值给读信息的线程
			if("true".equals(readWrite)){
				mReadSerialPort.setHandleSerialPort(mWriteSerialPort);
			}
			
			try {
				mReadSerialPort.read();
			} catch (SerialPortException e) {
				log.error("Serial communication reader failed to start restart it could not accept the news :: "+e.getMessage());
			}
			try {
				mReadSerialPort.writeStart();
			} catch (Exception e) {
				log.error("Serial communication listener failed to start writing ::"+e.getMessage());
			}
			
			//启动时清除短信猫缓存
			try{
				Thread.currentThread().sleep(2*1000);
				String sms = "AT"+StringEscapeUtils.unescapeJava("\r");
				mReadSerialPort.produceWriteMsg(sms);
				Thread.currentThread().sleep(5*1000);
				String sms2 = "AT+CMGD=1,4 "+StringEscapeUtils.unescapeJava("\r");
				mReadSerialPort.produceWriteMsg(sms2);
			}catch (Exception e) {
				log.error("Serial communication listener failed to start writing ::"+e.getMessage());
			}
		}
		
	
	}
	
	public void sendMessage(HashMap params)throws ServiceException{
		try{
			String msseage = (String)params.get("MESSAGE");
			String tel = (String)params.get("TEL");
			//String sms = "AT+CMGS=\""+tel+"\" \\x0d "+msseage+" \\x1A";
			String sms = "AT+CMGS=\""+tel+"\" "+StringEscapeUtils.unescapeJava("\\x0d".replace("\\x", "\\u00"))+" "+msseage+" "+StringEscapeUtils.unescapeJava("\\x1A".replace("\\x", "\\u00"));
			log.debug("====="+sms);
			//activeSerialPort();
			//mSerialPort.write(sms);  
			mWriteSerialPort.produceWriteMsg(sms);
			
			//清除短信猫缓存
/*			try{
				Thread.currentThread().sleep(60*1000);
				String sms2 = "AT+CMGD=1,4 "+StringEscapeUtils.unescapeJava("\r");
				mWriteSerialPort.produceWriteMsg(sms2);
			}catch (Exception e) {
				log.error("Serial communication listener failed to start writing ::"+e.getMessage());
			}*/
			
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	

	
	public void sendMessageAllUser(LinkedHashMap params)throws ServiceException{
		try{
			String isLiunx = BettleApplicationContext.getPropertieContext("variables", "LINUX");
			if("false".equals(isLiunx)){
				// -- xp service run
				activeSerialPort(); 
				// == xp service run
			}
			String msseage = (String)params.get("MESSAGE");
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			List<Map<String,Object>> list = sqlMapDAO.loadList("school.getAllUsers", params);
			
			for(int i = 0; i < list.size(); i++)  
	        {  
				Map map = list.get(i);
				String tel = BasicUitl.nullToEmpty(map.get("MOBILE"));
				if(StringUtils.isEmpty(tel)){
					tel = BasicUitl.nullToEmpty(map.get("TEL"));
				}
				if(!StringUtils.isEmpty(tel)){
					http(msseage,tel);
				}
	        }  
			claerHttp();
			
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void sendMessageParents(LinkedHashMap params)
			throws ServiceException {

		try {
			String isLiunx = BettleApplicationContext.getPropertieContext(
					"variables", "LINUX");
			if ("false".equals(isLiunx)) {
				// -- xp service run
				activeSerialPort();
				// == xp service run
			}
			String msseage = (String) params.get("message");
			boolean sms = (Boolean) params.get("sms");
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			sqlMapDAO.update("school.updateParentSms", params);

			List<Map<String, Object>> list = sqlMapDAO.loadList(
					"school.getParentsByBusNumber", params);

			if (sms) {
				for (int i = 0; i < list.size(); i++) {
					Map map = list.get(i);
					String tel = BasicUitl.nullToEmpty(map.get("PARENT_TEL"));

					if (!StringUtils.isEmpty(tel)) {
						http(msseage, tel);
					}
				}
				claerHttp();
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private void http(String message,String tel) throws HttpException, IOException{
		
		    String ip  = BettleApplicationContext.getPropertieContext("variables", "COM_IP");
		    HttpClient client = new HttpClient();  
	        //PostMethod method = new PostMethod("http://192.168.1.110:8080/SpatialNetSchool/api/sms");  
		    //PostMethod method = new PostMethod("http://207.111.169.70:8080/SpatialNetSchool/api/sms");
		    //PostMethod method = new PostMethod("http://207.111.169.68:7592/SpatialNetSchool/api/sms"); 
		    //PostMethod method = new PostMethod("http://192.168.0.115:8080/SpatialNetSchool/api/sms"); 
		    //PostMethod method = new PostMethod("http://192.168.1.105:8080/SpatialNetSchool/api/sms");
		    
		    PostMethod method = new PostMethod("http://"+ip+"/SpatialNetSchool/api/sms"); 
		    
	        method.setRequestHeader("Content-Type",  
	                "application/x-www-form-urlencoded;charset=utf-8");  
	        NameValuePair[] param = { new NameValuePair("message", message),  
	                new NameValuePair("tel",tel), };  
	        method.setRequestBody(param);  
	        int statusCode = client.executeMethod(method);   
	        method.releaseConnection();  
	}
	
	private void claerHttp() throws HttpException, IOException{
	    String ip  = BettleApplicationContext.getPropertieContext("variables", "COM_IP");
	    HttpClient client = new HttpClient();  
	    PostMethod method = new PostMethod("http://"+ip+"/SpatialNetSchool/api/clearSms"); 
        method.setRequestHeader("Content-Type",  
                "application/x-www-form-urlencoded;charset=utf-8");  
        NameValuePair[] param = { new NameValuePair("message", ""), };  
        method.setRequestBody(param);  
        int statusCode = client.executeMethod(method);   
        method.releaseConnection();  
	}
	
	public void clearSms(HashMap params)throws ServiceException{
		try{
			Thread.currentThread().sleep(8*1000);
			String sms = "AT+CMGD=1,4 "+StringEscapeUtils.unescapeJava("\r");
			mWriteSerialPort.produceWriteMsg(sms);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	private void encodePassword(HashMap params) throws Exception {
		if(params.containsKey("PASSWORD")){
			String password = (String)params.get("PASSWORD");
			params.put("PASSWORD", Base64.encode(MD5Util.getMD5(Base64.encode(password))));
		}
	}
	
	
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveDriver(LinkedHashMap params) throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			UUIDHexGenerator mUUIDHexGenerator = new UUIDHexGenerator();
			params.put("DRIVER_ID", mUUIDHexGenerator.generate());
			sqlMapDAO2.insert("school.saveDriverMemory2", params);
			sqlMapDAO2.insert("school.saveDriverBusMemory2",params);
			sqlMapDAO.insert("school.saveDriver", params);
			return sqlMapDAO.insert("school.saveDriverBus", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteDriver(LinkedHashMap params) throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			sqlMapDAO2.delete("school.deleteDriverBusMemory", params);
			sqlMapDAO2.delete("school.deleteDriverMemory", params);
			sqlMapDAO.delete("school.deleteDriverBus", params);
			return sqlMapDAO.delete("school.deleteDriver", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Map<String,Object> getLocation(LinkedHashMap params) throws RuntimeException{
		try{
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			return sqlMapDAO2.loadOne("school.getBusMemoryByBusNumber", params);
			

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	
	private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)  
    
    /** 
     * 转化为弧度(rad) 
     * */  
    private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
    /** 
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下 
     * @param lon1 第一点的精度 
     * @param lat1 第一点的纬度 
     * @param lon2 第二点的精度 
     * @param lat3 第二点的纬度 
     * @return 返回的距离，单位km 
     * */  
    public static double getDistance(double lon1,double lat1,double lon2, double lat2)  
    {  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;  
       double b = rad(lon1) - rad(lon2);  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
       s = s * EARTH_RADIUS;  
       //s = Math.round(s);
       //s = Math.round(s * 10000) / 10000;  
       return s;  
    }
	
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    /*--------------------------------------------------service-------------------------------------------*/
	SocketAcceptor acceptor = null;
	

	@Transactional(propagation=Propagation.REQUIRED)
	private void socketService(LinkedHashMap params) throws RuntimeException {
		try {
			ISqlMapDAO mySqlMapDao = getSqlMapDAO("MySqlMap");
			ISqlMapDAO memorySqlMapDao = getSqlMapDAO("MemorySqlMap");
			int port = BasicUitl.nullToInt(
					BettleApplicationContext.getPropertieContext("variables",
							"SocketServicePort"), 9090);
			

			//int SocketServicePort = BasicUitl.nullToInt(params.get("port"), port);
			int SocketServicePort = port;
			
			acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new BettleTextLineCodecFactory()));
			// 配置CodecFactory
			LoggingFilter log = new LoggingFilter();
			log.setMessageReceivedLogLevel(LogLevel.INFO);
			acceptor.getFilterChain().addLast("logger", log);
			acceptor.setHandler(new SchoolServiceHandle(mySqlMapDao,memorySqlMapDao));// 配置handler
			// 设置读取数据的缓冲区大小
			acceptor.getSessionConfig().setReadBufferSize(2048 * 5000);// 发送缓冲区10M
			acceptor.getSessionConfig().setReceiveBufferSize(2048 * 5000);// 接收缓冲区10M
			// 读写通道30秒内无操作进入空闲状态
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
			acceptor.bind(new InetSocketAddress(SocketServicePort));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean stopService(LinkedHashMap params) throws RuntimeException {
		try {
			acceptor.unbind();
			acceptor.dispose(true);
			acceptor = null;
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean pauseService(LinkedHashMap params) throws RuntimeException {
		try {
			acceptor.unbind();
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean startService(LinkedHashMap params) throws RuntimeException {
		try {
			if (acceptor == null) {
				socketService(params);
			} else {
					int port = BasicUitl.nullToInt(
							BettleApplicationContext.getPropertieContext(
									"variables", "SocketServicePort"), 9090);
					int SocketServicePort = BasicUitl.nullToInt(params.get("port"), port);
					// 加上这句话，避免重启时提示地址被占用.同时这句话要放在下面的bind前面，如果放后面就不生效，没作用了。
					acceptor.setReuseAddress(true);
					acceptor.bind(new InetSocketAddress(SocketServicePort));
			}

			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	
	//@SuppressWarnings("rawtypes")
	
	@Override
	public Map getbusbyparentsID(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			return sqlMapDAO.loadOne("school.getbusbyparents", params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Map getodb2CurrentDate(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");			
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			Map bus = sqlMapDAO.loadOne("school.getbusbyparents", params);
			if (bus == null){
				return new HashMap();
			}
			String busid = BasicUitl.nullToEmpty(bus.get("BUS_NUMBER_TO"));
			if (StringUtils.isBlank(busid)){
				Map bus1 = sqlMapDAO2.loadOne("school.getCurrentByIMEI", bus);	
				if (bus1==null){
					return bus;
				}
				else{
					bus1.putAll(bus);
					return bus1;
				}	
			}else{
				Map bus1 = sqlMapDAO2.loadOne("school.getCurrentByNUMBER", bus);	
				if (bus1==null){
					return bus;
				}
				else{
					bus1.putAll(bus);
					return bus1;
				}	
			}

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void initBus(LinkedHashMap p)  {
		try {
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			//#{BUS_ID}, #{latitude}, #{longitude}, #{altitude}, #{gpsDate}, #{speed}, #{direct}
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("BUS_ID","ff8080814f097d84014f097e389d0001");
			params.put("latitude", 32.074333f);
			params.put("longitude",118.80894f);
			params.put("altitude", 100.0);
			params.put("gpsDate", new Date(System.currentTimeMillis()));
			sqlMapDAO2.insert("school.saveGpsMemory", params);
			
			params.put("BUS_ID","ff8080814f097d84014f097d84970000");
			params.put("latitude", 32.074333f);
			params.put("longitude",118.80895f);
			params.put("altitude", 100.0);
			sqlMapDAO2.insert("school.saveGpsMemory", params);
			
			params.put("BUS_ID","ff8080814efd9739014efebfeb170040");
			params.put("latitude", 32.074333f);
			params.put("longitude",118.80896f);
			params.put("altitude", 100.0);
			sqlMapDAO2.insert("school.saveGpsMemory", params);
			
			params.put("BUS_ID","ff8080814efd9739014efebf7010003f");
			params.put("latitude", 32.072252f);
			params.put("longitude",118.813795f);
			params.put("altitude", 100.0);
			sqlMapDAO2.insert("school.saveGpsMemory", params);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<Map<String, Object>> getodb2Date(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO2 = getSqlMapDAO("MemorySqlMap");
			List<Map<String, Object>> list = sqlMapDAO2.loadList("school.getAllBusGpsMemory", params);
			if (list == null){
				return null;
			}
			HttpServletRequest request = (HttpServletRequest)params.get("request");
			
		  	if (request.getParameter("LONGITUDE") ==null || request.getParameter("LATITUDE") ==null ){
		  		return list;
		  	}
		  	double lon1 = Double.parseDouble(request.getParameter("LONGITUDE").toString());
		  	double lat1 = Double.parseDouble(request.getParameter("LATITUDE").toString());
		  	
		  	double lon2;
		  	double lat2;
		  	double distance;
			for (Map<String,Object> item : list){
				 lon2 = Double.parseDouble(item.get("LONGITUDE").toString());
				 lat2 = Double.parseDouble(item.get("LATITUDE").toString());
				 distance = getDistance(lon1, lat1, lon2, lat2) * 0.000621371192237;
				 item.put("DISTANCE",  distance);
			}
			return list;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}	
	
		@Override
	public PageInfo getodb2HistryDate(LinkedHashMap params) throws RuntimeException {
		try{
			ISqlMapDAO sqlMapDAO = getSqlMapDAO("MySqlMap");
			Map busf = sqlMapDAO.loadOne("school.getBusfromto", params);
			int pageNum  = BasicUitl.DoubleToInt2(params.get("pageNum"), 0);
			int pageSize = BasicUitl.DoubleToInt2(params.get("pageSize"), 0);
			if (busf == null){				
				return sqlMapDAO.loadPageInfo("school.getodb2HistryGPS", params, pageNum, pageSize);
			}else{
				//params.put("PID",busf.get("BUS_NUMBER_TO"));
				return sqlMapDAO.loadPageInfo("school.getBusfHistryGPS", busf, pageNum, pageSize);
			}
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}	
	
	/*==================================================service  end======================================*/
	/*--------------------------------------------------cilent-------------------------------------------*/

	SocketConnector connector = null;
	ConnectFuture future = null;
	IoSession session = null;

	private void socketClient(LinkedHashMap params) throws RuntimeException {
		try {
			String ServiceIP = BasicUitl.NVLToStr(BettleApplicationContext
					.getPropertieContext("variables", "ServiceIP"),
					"218.90.157.210");
			int ServicePort = BasicUitl.nullToInt(BettleApplicationContext
					.getPropertieContext("variables", "ServicePort"), 8005);
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(3000);
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new BettleTextLineCodecFactory()));
			LoggingFilter log = new LoggingFilter();
			log.setMessageReceivedLogLevel(LogLevel.INFO);
			connector.getFilterChain().addLast("logger", log);
			connector.setHandler(new SchoolClientHandle());

			future = connector.connect(new InetSocketAddress(ServiceIP,
					ServicePort));
			future.awaitUninterruptibly();
			session = future.getSession();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setClientAttribute(Object key, Object value) {
		session.setAttribute(key, value);
	}

	void clientSentMsg(String message) {
		session.write(message);
	}

	public boolean clientClose() {
		CloseFuture future = session.getCloseFuture();
		future.awaitUninterruptibly(1000);
		connector.dispose();
		return true;
	}
	/*==================================cilent  end========================================*/
    
    
    

	public static void main(String[] args) {  
/*		String st = "+CMGR: \"REC UNREAD\",\"+12566569092\",,\"14/10/30,11:40:18-20\"Whereis:09-14";
		String[] smsNumber = st.split(",\"");
		for(int i=0;i<smsNumber.length;i++)
		 System.out.println(smsNumber[i]);
		
		System.out.println(smsNumber[1].substring(0,smsNumber[1].length()-2));
		System.out.println(smsNumber[2].split(":")[3]);*/
		//System.out.println(System.getProperty("line.separator"));
		
		
    } 
	
}
