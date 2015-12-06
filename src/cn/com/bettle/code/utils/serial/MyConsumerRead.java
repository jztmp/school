package cn.com.bettle.code.utils.serial;


class MyConsumerRead extends ConsumerRead{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(this.message);
	}

}
