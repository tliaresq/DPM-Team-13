import lejos.nxt.ColorSensor;

public class LSController extends Thread {
	
	static ColorSensor colorSensor;
	private int color;
	private boolean stop;

	public LSController(ColorSensor cs) {
		colorSensor = cs;
		color = -1;
		stop = true;
	}

	public void run() {
		stop = true;
		while (true) {
			if (stop) {	
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				color = colorSensor.getRawLightValue();
				try {Thread.sleep(10);} catch (Exception e) {}
			}
		}
	}

	public void stopLS() {
		stop = true;
		colorSensor.setFloodlight(false);
		color = -1;
	}

	public void restartLS(){
		stop = false;
		colorSensor.setFloodlight(10);
	}

	public int getColor() {
		return color;
	}



}
