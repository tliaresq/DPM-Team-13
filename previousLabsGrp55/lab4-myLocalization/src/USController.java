import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class USController extends Thread {
	UltrasonicSensor usSensor = new UltrasonicSensor(SensorPort.S1);
	private int distance;
	private boolean stop;

	public USController() {
		distance = 333;
		stop = false;
	}

	public void run() {
		stop = false;
		while (true) {
			// process collected data

			distance = usSensor.getDistance();
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
			if (stop) {
				usSensor.off();
				distance = 333;
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	public void stopUS() {
		
		stop = true;
	}

	public int sensorDist() {
		return distance;
	}

}
