import lejos.nxt.UltrasonicSensor;


public class UltrasonicPoller extends Thread{
	private UltrasonicSensor us;
	private UltrasonicController cont;
	private Object locker;
	
	public UltrasonicPoller(UltrasonicSensor us, UltrasonicController cont, Object lock) {
		this.us = us;
		this.cont = cont;
		locker= lock;
	}
	
	public void run() {
		synchronized(locker){
		while (us.getDistance()<59) {
			//process collected data
			cont.processUSData(us.getDistance());
			try { Thread.sleep(10); } catch(Exception e){}
		}
	}
	}
}
