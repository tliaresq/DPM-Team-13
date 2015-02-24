import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class USController extends Thread {
	UltrasonicSensor usSensor = new UltrasonicSensor(SensorPort.S1);
	private int distance;
	
	public USController (){
		distance = 0;
	}
	
	public void run() {
		while (true) {
			//process collected data
			distance = usSensor.getDistance();
			try { Thread.sleep(10); } catch(Exception e){}
		}
	}
	
	public int sensorDist(){
		return distance;
	}

}
