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
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	public void stopLS() {
		color = 999;
		stop = true;
	}

	public int getColor() {
		return color;
	}

}
