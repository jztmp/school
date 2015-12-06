package cn.com.bettle.code.utils.serial;

//定义消费者
public abstract class ConsumerRead extends Thread {
	
	protected String message = null;
	
	protected DSerialPort mSerialPort;

	public DSerialPort getmSerialPort() {
		return mSerialPort;
	}

	public void setmSerialPort(DSerialPort mSerialPort) {
		this.mSerialPort = mSerialPort;
	}
	
	

}
