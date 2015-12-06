package cn.com.bettle.code.utils.serial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TooManyListenersException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.exception.SerialPortException;
import cn.com.bettle.net.controller.ApiController;

/**
 * @项目名称 :Bettle
 * @文件名称 :SerialPort.java
 * @所在包 :org.serial
 * @功能描述 :
 *	串口类
 * @创建者 :John	343816974@qq.com
 * @创建日期 :2014-11-22
 * @修改记录 :
 */
public class DSerialPort implements  SerialPortEventListener {

	
	protected final Logger log = LoggerFactory.getLogger(DSerialPort.class);
	
	private String commPortName = "COM1";
	
	private int baudRate = 9600;
	
	private int delay = 3;
	
	
	private String appName = "School Bus";
	
	private int timeout = 2000;//open 端口时的等待时间
	
	private int threadTime = 0;
	
	private CommPortIdentifier commPort;
	
	private SerialPort serialPort;
	
	//发送指令的队列
	private LinkedBlockingQueue<String>  writeMsgQueue = new LinkedBlockingQueue<String>(5000);
	//private List writeMsgList = new ArrayList();
	
	//用来判断短信猫是否处于空闲状态
	private LinkedBlockingQueue<Boolean>  okQueue = new LinkedBlockingQueue<Boolean>(1);
	
	private ExecutorService writePool = Executors.newSingleThreadExecutor(); 

	private ExecutorService handleReadPool = Executors.newFixedThreadPool(20);
	
	private Class<? extends ConsumerRead> handle;
	
	private DSerialPort handleSerialPort = null;
	
	private LinkedBlockingQueue<String>  messageQueue = new LinkedBlockingQueue<String>(1000);
	//private List messageQueueList = new ArrayList();
	

	public DSerialPort getHandleSerialPort() {
		return handleSerialPort;
	}
	public void setHandleSerialPort(DSerialPort handleSerialPort) {
		this.handleSerialPort = handleSerialPort;
	}
	public Class getHandle() {
		return handle;
	}
	public void setHandle(Class handle) {
		this.handle = handle;
	}
	
	
	//生产需要临时产生的或者存储的信息
    public void produceMsgList(String msg) throws InterruptedException{
    		//put方法信息放入队列,若writeMsgQueue满了,等到writeMsgQueue有位置 -- 防止漏发
    	messageQueue.put(msg);
    }
    //消费临时产生的或者存储的信息
    public String consumeMsgList() throws InterruptedException{
           //获取队列里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null
           return messageQueue.poll(5, TimeUnit.SECONDS);
    }
	
	
	
	
	//生产
    public void produceWriteMsg(String msg) throws InterruptedException{
    	/*
		 * 对短信猫的主动操作前必须等到短信猫就位也就是空闲状态。如果等待超时则直接获取控制权进行指令发送
		 * */
    	if(isOk()){ 
    		//put方法信息放入队列,若writeMsgQueue满了,等到writeMsgQueue有位置 -- 防止漏发
    		writeMsgQueue.put(msg);
    	}
    }
    //消费
    public String consumeWriteMsg() throws InterruptedException{
           //获取队列里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null
           return writeMsgQueue.poll(5, TimeUnit.SECONDS);
    }

    
	
	
	//如果短信猫处于空闲状态就赋值
    public void setOk() throws InterruptedException{
           //表示如果可能的话,将a加到Queue里,即如果Queue可以容纳,则返回true,否则返回false.
    	    Boolean a = Boolean.valueOf(true);
    		okQueue.offer(a,1,TimeUnit.SECONDS);
    		System.out.println("------------------ok success--------------------");
    }
    //判断短信猫是否空闲并如果是则获取短信猫的控制权否则等到一段时间后再取如果还是取不到则直接获取控制权。
    public boolean isOk() throws InterruptedException{
           //获取队列里排在首位的对象,若Queue为空,阻断进入等待状态直到Queue有新的对象被加入为止
    	   //Boolean a = okQueue.take();
    	   /*
    	    * 获取队列里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null。
    	    * 由于串口通信有时信号不稳定所以不能一直等待，
    	    * 如获取不到则等到delay秒后继续操作,取不到时返回null。
    	    * 通队列同步的机制来控制获取短信猫空闲状态权和设置短信猫空闲状态标志位的线程能够同步（条件变量的原理）
    	   */
    		Boolean a = okQueue.poll(delay, TimeUnit.SECONDS);
    		System.out.println("------------------ok get--------------------");
            return true;
    }
    
	/**
	 * @方法名称 :listPort
	 * @功能描述 :列出所有可用的串口
	 * @返回值类型 :void
	 */
	@SuppressWarnings("rawtypes")
	public void listPort(){
		CommPortIdentifier cpid;
		Enumeration en = CommPortIdentifier.getPortIdentifiers();
		
		log.debug("now to list all Port of this PC:" +en);
		
		while(en.hasMoreElements()){
			cpid = (CommPortIdentifier)en.nextElement();
			if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL){
				//System.out.println(cpid.getName() + ", " + cpid.getCurrentOwner());
				log.debug(cpid.getName() + ", " + cpid.getCurrentOwner());
			}
		}
	}
	
	
	/**
	 * @方法名称 :selectPort
	 * @功能描述 :选择一个端口，比如：COM1
	 * @返回值类型 :void
	 *	@param portName
	 * @throws SerialPortException 
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public void selectPort(String portName,int baudRate,int delayTime) throws SerialPortException, IOException{
		if(portName!=null){
			this.commPortName = portName;
		}
		if(baudRate!=0){
			this.baudRate = baudRate;
		}
		if(delayTime!=0){
			this.delay = delayTime;
		}
		
		if(this.commPort!=null){
			this.close();
			this.commPort = null;
		}
		
		CommPortIdentifier cpid;
		Enumeration en = CommPortIdentifier.getPortIdentifiers();
		
		while(en.hasMoreElements()){
			cpid = (CommPortIdentifier)en.nextElement();
			log.debug(String.valueOf(cpid.getPortType()));
			log.debug(String.valueOf(cpid.getName()));
			if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL
					&& cpid.getName().equals(this.commPortName)){
				
				this.commPort = cpid;
				break;
			}
		}
		if(this.commPort!=null){
			openPort();
		}else{
			log.debug(String.format("Unable to find a name for the '%1$s' Serial", this.commPortName));
		}
		
	}
	
	/**
	 * @方法名称 :openPort
	 * @功能描述 :打开SerialPort
	 * @返回值类型 :void
	 */
	private void openPort()throws SerialPortException{
		if(commPort == null){
			log.debug(String.format("Unable to find a name for the '%1$s' Serial", this.commPortName));
		}
		else{
			log.debug("Port selection is successful, the current port:"+commPort.getName()+", SerialPort:");
			
			try{
				serialPort = (SerialPort)commPort.open(appName, timeout);
				log.debug("instance  SerialPort successful");
			
				
				
			}catch(PortInUseException e){
				throw new SerialPortException(String.format("Serial'%1$s'Is in use", 
						commPort.getName()));
			}
		}
	}
	
	/**
	 * @throws SerialPortException 
	 * @方法名称 :checkPort
	 * @功能描述 :检查端口是否正确连接
	 * @返回值类型 :void
	 */
	private void checkPort() throws SerialPortException{
		if(commPort == null)
			throw new SerialPortException("The port is not selected, please use" +
					" selectPort(String portName) Methods select the port");
		
		if(serialPort == null){
			throw new SerialPortException("SerialPort  is invalid!");
		}
	}
	
	//定义消费者
    class ConsumerWrite implements Runnable {
        public void run() {
            try {
                while (true) {
                    // 消费信息
                	try{
                		String msg = consumeWriteMsg();
                		if(msg!=null){
                			write(msg);
                		}
                	} catch (SerialPortException ex2) {
                        
                        log.error("DSerialPort write message :: "+ex2.getMessage());
                    } catch (Exception ex3) {
                        
                        log.error("DSerialPort write message :: "+ex3.getMessage());
                    } 
                    // 休眠
                    Thread.sleep(200);
                }
            } catch (InterruptedException ex) {
                
                log.error("DSerialPort write message at ConsumerWrite :: "+ex.getMessage());
            } 
        }
    }

    


  

	
	/**
	 * @方法名称 :write
	 * @功能描述 :向端口发送数据，请在调用此方法前 先选择端口，并确定SerialPort正常打开！
	 * @返回值类型 :void
	 *	@param message
	 * @throws SerialPortException 
	 */
	public void write(String message) throws SerialPortException {
		OutputStream outputStream = null;
		checkPort();
		try {
			setBaudRate(baudRate);
			outputStream = new BufferedOutputStream(serialPort.getOutputStream());
			if(this.isOk()){
				outputStream.write(message.getBytes());
				log.debug("==============================Send port data :: "+message);
				//log.debug("Information transmission is successful");
			}
		}catch(IOException e){
			throw new SerialPortException("Error information is sent to the port:"+e.getMessage());
		}catch (UnsupportedCommOperationException e1) {
			throw new SerialPortException("Access port error:",e1);
		} catch (InterruptedException e) {
			throw new SerialPortException("Access port error:",e);
		}finally{
			try{
				if(outputStream!=null){
					outputStream.close();
					outputStream = null;
				}
			}catch(Exception e){
                log.error("DSerialPort write message :: "+e.getMessage(),e);
			}
		}

	}
	

	public void read() throws SerialPortException {
		checkPort();
		
	
		try{
			serialPort.addEventListener(this);
		}catch(TooManyListenersException e){
			throw new SerialPortException(e.getMessage());
		}
		
		serialPort.notifyOnDataAvailable(true);
		
		Thread readThread = new Thread(new Runnable(){
			 public void run() {
			        try {
			            Thread.sleep(20000);
			        } catch (InterruptedException e) {}
			    }
		});
        readThread.start();
		
		log.debug(String.format("Start monitoring data from '%1$s'--------------", commPort.getName()));
	}
	
	
	
	
	public void writeStart(){
		 ConsumerWrite consumerWrite = new ConsumerWrite();
		 writePool.execute(consumerWrite);
	}
	/**
	 * @throws IOException 
	 * @方法名称 :close
	 * @功能描述 :关闭 SerialPort
	 * @返回值类型 :void
	 */
	public void close() throws IOException{
		if(serialPort!=null){
			serialPort.close();
			serialPort = null;
			commPort = null;
			
			
			writePool.shutdown();
			handleReadPool.shutdown();
			writeMsgQueue.clear();
		}

	}
	
	



	/**
	 * 数据接收的监听处理函数
	 */
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		switch(arg0.getEventType()){
		case SerialPortEvent.BI:/*Break interrupt,通讯中断*/ 
			break;
        case SerialPortEvent.OE:/*Overrun error，溢位错误*/ 
        	break;
        case SerialPortEvent.FE:/*Framing error，传帧错误*/
        	break;
        case SerialPortEvent.PE:/*Parity error，校验错误*/
        	break;
        case SerialPortEvent.CD:/*Carrier detect，载波检测*/
        	break;
        case SerialPortEvent.CTS:/*Clear to send，清除发送*/ 
        	break;
        case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/ 
        	break;
        case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
        	break;
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/ 
            break;
        case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
        	byte[] readBuffer = new byte[1024];
            String readStr="";
            String s2 = "";
            InputStream inputStream = null;
            try {
            	setBaudRate(baudRate);
            	try{
            		inputStream = new BufferedInputStream(serialPort.getInputStream());
        		}catch(IOException e){
        			log.error("Access port InputStream error:",e);
        		}
        		
            	
            	while (inputStream.available() > 0) {
                    inputStream.read(readBuffer);
                }
            	s2 = new String(readBuffer).trim();
            	log.debug("===========================Receive port data :: "+s2);
            	
            	ConsumerRead th = handle.newInstance();
            	if(this.handleSerialPort!=null){
            		th.setmSerialPort(this.handleSerialPort);
            	}else{
            		th.setmSerialPort(this);
            	}
            	th.message = s2;
            	handleReadPool.execute(th); 
            	Thread.currentThread().sleep(2000);
            } catch (IOException e) {
            	log.error("Receive port  :: "+e.getMessage(),e);
            } catch (InterruptedException e) {
            	log.error("Receive port InterruptedException :: "+e.getMessage(),e);
			} catch (UnsupportedCommOperationException e) {
				log.error("Receive port UnsupportedCommOperationException :: "+e.getMessage(),e);
			} catch (InstantiationException e) {
				log.error("Receive port InstantiationException "+handle.getName()+" :: "+e.getMessage(),e);;
			} catch (IllegalAccessException e) {
				log.error("Receive port IllegalAccessException "+handle.getName()+" :: "+e.getMessage(),e);;
			}finally{
				try{
					if(inputStream!=null){
						inputStream.close();
					}
				}catch(Exception e){
	                log.error("DSerialPort write message :: "+e.getMessage(),e);
				}
			}
		}
	}

	
	



	
	
	public void setBaudRate(int baudRate) throws UnsupportedCommOperationException{
		serialPort.setSerialPortParams(baudRate,
		SerialPort.DATABITS_8,
		SerialPort.STOPBITS_1,
		SerialPort.PARITY_NONE);
	}
	

    

	
	public static void main(String[] args) {  
        
        DSerialPort sp = new DSerialPort();  
        //sp.listPort();  
        try {
			sp.selectPort("COM1",9600,3);
		} catch (SerialPortException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        if(sp.commPort!=null){
            sp.setHandle(MyConsumerRead.class);
            try {
    			sp.read();
    		} catch (SerialPortException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            sp.writeStart();
            try {
				sp.produceWriteMsg("Zxmapple");
				Thread.currentThread().sleep(3000);
				sp.produceWriteMsg("Hello Zxm");
				sp.produceWriteMsg("Come on");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }


    } 
}