package cn.com.bettle.logic.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bettle.code.appcontext.context.BettleApplicationContext;
import cn.com.bettle.code.sqlmap.dao.ISqlMapDAO;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialComm{
	static Logger log = LoggerFactory.getLogger(SerialComm.class);
	static Executor pool = Executors.newSingleThreadExecutor();
	static final int LENGTH = 550;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	ISqlMapDAO sqlMapDao;
	SerialReader serailReader = null;
	
	public SerialComm (ISqlMapDAO mySqlMapDao){
		this.sqlMapDao = mySqlMapDao;
	}
	public void init() {

		try {
			Integer Baudrate = Integer.parseInt(BettleApplicationContext.getPropertieContext("variables", "JACKSON_COMM_BAUDRATE"));
			
			startListen(BettleApplicationContext.getPropertieContext("variables", "JACKSON_COMM_NAME"),Baudrate);
			
		} catch (Exception e) {
			throw new Error("bind serial comm error", e);
		}
	}

	private void startListen(String com,Integer Baudrate) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(com);
		if (portIdentifier.isCurrentlyOwned()) {
			throw new Exception("Port is currently in use");
		} else {
			final CommPort commPort = portIdentifier.open(SerialComm.class.getName(), 2000);
			
			JacksonContextListener.addDestoryTask(new Runnable(){
				@Override
				public void run() {
					if (serailReader != null){
						serailReader.cancel();
					}
					if (commPort != null){
						log.info("try to close serial port:" + commPort.getName());
						commPort.close();
					}
				}
			});
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				/*
				serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);*/
				serialPort.setSerialPortParams(Baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				serailReader = new SerialReader(serialPort.getInputStream());
				serailReader.start();
				log.info("start listener at " + com);
			} else {
				throw new Exception(String.format("%s is not serial port.", com));
			}
		}
	}

	class SerialReader extends Thread {
		InputStream in;
		byte[] buffer = new byte[1024];
		boolean cancel = false;

		public SerialReader(InputStream in) {
			this.in = in;
		}
		public void cancel(){
			cancel = true;
			try {
				in.close();
			} catch (IOException e) {
				log.error("An error occurs in close port.",e);
			}
		}

		int readNotBlocking() {
			try {
				if (in.available() > 0){						
					return in.read(buffer);
				} else {
					Thread.sleep(100);
				}
			} catch (Exception e) {
				log.error("An error occurs in reading buffer, discard the buffer and try again .");
			}
			return 0;
		}
		
		
		public void run() {
			int len = -1;
			int status = 0;
			int i = 0;
			byte[] content = null;
			int count = 0;
			
			

			while (!cancel && (len = readNotBlocking()) > -1) {
				/*
				if (len != 0){
					String logx = new String(buffer, 0,len);
					log.info("str:"+logx);
				}*/
				
				for (i = 0; i < len; i++) {
					switch (status) {
					case 0: // start: need to find the start flag '2'
						if ((char)buffer[i] == 2) {
							status = 1;
							content = new byte[1024];
							count = 0;
							content[count++] = buffer[i];
						}
						break;
					case 1: 						
						if ((char)buffer[i] == 2) {
							String logstr = new String(content,0,count);
							log.error("***Error:"+logstr+"***");
							status = 1;
							content = new byte[1024];
							count = 0;
							content[count++] = buffer[i];
							break;
						}		
						content[count++] = buffer[i];
						if (count == LENGTH && (char)buffer[i] == 3){
							status = 0;
							pool.execute(new ParserAndStore(content));
							break;
						}
						if (count != LENGTH && (char)buffer[i] == 3){
							status = 0;
							String logstr = new String(content,0,count);
							log.error("***Error:"+logstr+"***");
							break;
						}
						if (count == 1024){							
							log.error("discard content for not finding the end flag"+"***");
							String logstr = new String(content,0,count);
							log.error("***Error:"+logstr+"***");
							status = 0;
							break;
						}
						/*
						if (count > LENGTH){
							String logstr = new String(content,0,count-1);
							log.error("discard content for not finding the end flag"+"***"+logstr);
							status = 0;
							break;
						}
						*/
						break;
					}
				}
			}
		}
	}
	
	class ParserAndStore implements Runnable{
		byte[] content;
		ParserAndStore(byte[] content){
			this.content = content;
		}
		String sub(int off, int length){
			// for VB scriptssqlMapDao
			return new String(content, off -1 ,length);
		}
		@Override
		public void run() {
			LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
			
			String logstr = sub(1,550);
			log.info(logstr);
			
			map.put("phoneNumber",sub(70,14).trim());
			map.put("dateS",sub(96,5).trim());
			map.put("timeS",sub(87,5).trim());
			String tempstr= sub(153,5).trim();
			if(tempstr!=""){
				tempstr = Integer.parseInt(tempstr)+"";
			}	
			map.put("esn",tempstr);
			
			map.put("addrH",sub(102,12).trim());
			map.put("addrHS",null);
			map.put("addrPre",sub(114,2).trim());
			
			map.put("addrSt",sub(114,20).trim());
			map.put("addrSfix",null);
			map.put("addrPst",null);
			map.put("city",sub(134,16).trim());
			map.put("custName",null);
			map.put("addInfo",sub(230,10).trim());
			map.put("wirelessX",sub(296,11).trim());
			if ("--".equals(sub(301,2))){
				map.put("wirelessX",sub(302,10).trim());
			}
			map.put("wirelessY",sub(311,9).trim());
			map.put("station",sub(3,2).trim());
			map.put("serverT",sub(161,4).trim());
			map.put("address",map.get("addrH") + " "+ map.get("addrSt") + " "  + map.get("esn"));
			map.put("dateTimes",sdf.format(new Date()) + map.get("dateS") + map.get("timeS"));
			
			if(map.get("esn") == "" && map.get("wirelessY") == ""){
				
			}else{
				sqlMapDao.insert("school.saveE911", map);
				sqlMapDao.insert("school.saveE911_current", map);
				
				if (((BigInteger)(sqlMapDao.loadOne(
						"school.getE911_current_count", null
						).get("num"))).intValue() > 20){
					sqlMapDao.delete("school.deletedE911_current", null);
				}
			}
			
		}
	}

}
