import lejos.nxt.ColorSensor;

/**
 * Manages All the information coming from a light sensor by filtering it and returning a filtered value. 
 * the filter is based on a simple system of consecutive values.
 * when a values is under a certain reference value, the counter is incremented, else, it is set back to 0.
 * when the counter is superior to a certain amount considered as the filter size, the boolean signaling a line is set to true.
 * th same boolean is set back to false as soon as the counter is lower than the filter size
 * @author Cedric
 *
 */
public class LSController extends Thread {
	public ColorSensor colorSensor;
	private boolean line;
	private int black;
	private int fSize;

	public LSController(ColorSensor cs, int filterSize, int b){
		colorSensor = cs;
		line = false;
		fSize = filterSize;
		black = b;
	}

	public void run() {
		colorSensor.setFloodlight(0);
		int counter=0;
		while (true) {
			try {Thread.sleep(10);} catch (Exception e) {}
			if(colorSensor.getRawLightValue()<black ){counter ++;}
			else{
				line = false;
				counter = 0;
			}
			if (counter>fSize ){
				line = true;
				try {Thread.sleep(100);} catch (Exception e) {}
			}
		}
	}

	public boolean getLS() {
		return line;
	}
}
