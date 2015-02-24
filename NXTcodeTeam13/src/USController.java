import lejos.nxt.UltrasonicSensor;

public class USController extends Thread {
	UltrasonicSensor usSensor;
	private int distance;
	private boolean stop;

	public USController( UltrasonicSensor us) {
		usSensor = us;
		distance = -1;
		stop = true;
	}

	public void run() {
		stop = true;
		while (true) {
			
			if (stop) {
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{	
			distance = usSensor.getDistance();
			try {Thread.sleep(10);} catch (Exception e) {}
			}
		}
	}

	public void stopUS() {
		stop = true;
		usSensor.off();
		distance = -1;
	}
	
	public void restartUS(){
		stop = false;
		usSensor.continuous();
	}

	public int sensorDist() {
		return distance;
	}

}
