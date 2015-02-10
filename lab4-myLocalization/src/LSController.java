import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;

public class LSController extends Thread {
	static ColorSensor cs = new ColorSensor(SensorPort.S2);
	private int color;
	private boolean stop;

	public LSController() {
		color = 999;
	}

	public void run() {
		stop = false;
		while (true) {
			// process collected data
			color = cs.getRawLightValue();
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
			if (stop) {
				cs.setFloodlight(false);
				color = 999;
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	public void stopLS() {
		
		stop = true;
	}

	public int getColor() {
		return color;
	}

}
