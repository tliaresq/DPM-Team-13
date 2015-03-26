import lejos.nxt.ColorSensor;
import lejos.nxt.Sound;

/**
 * Manages All the information coming from a light sensor by filtering it and returning a filtered value. 
 * @author Cedric
 *
 */
public class LSController extends Thread {
	
	private ColorSensor colorSensor;
	private int prevColor;
	private int color;
	private boolean line;
	private boolean stop;
	private int[] colors;
	
	private final int lineDer = -20;

	public LSController(ColorSensor cs, int filterSize) {
		colorSensor = cs;
		prevColor = 0;
		line = false;
		stop = true;
		colors = new int[filterSize];
	}

	public void run() {
		stop = true;
		while (true) {
			if (stop) {	
				line = false;
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				for(int i = 0 ; i< colors.length-1; i ++){
					colors[i+1]  = colors[i];
				}
				colors[0] = colorSensor.getRawLightValue();
				filter(colors);
				try {Thread.sleep(10);} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * Filters the sensor value by taking the mean value
	 * @param c Number of most recent values to take the mean from
	 * @return
	 */
	private void filter(int[] c) {
		int sum = 0;
		for(int i = 0; i<c.length; i++ ){
			sum += colors[i];
		}
		if(prevColor==0)
		{
			prevColor=(sum/c.length);
		}
		else
		{
			prevColor = color;
		}
		color = sum/c.length;
		if(color-prevColor<lineDer)
		{
			line = true;
			Sound.beep();
			try {Thread.sleep(80);} catch (Exception e) {}
		}
		else
		{
			line = false;
		}
	}
	/**
	 * stops the light sensor
	 * Does not stop the thread from running
	 * This is mainly to save battery
	 */
	public void stopLS() {
		stop = true;
		colorSensor.setFloodlight(false);
		line = false;
	}
	/**
	 * restarts sensor
	 */
	public void restartLS(){
		stop = false;
		colorSensor.setFloodlight(0);
	}
	
	public boolean getLS() {
		return line;
	}

}
