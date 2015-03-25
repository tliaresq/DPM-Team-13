import lejos.nxt.ColorSensor;
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
		int[] temp = new int[c.length];
		//copying the array not to affect the oder of C
		for(int i = 0; i<c.length; i++ ){
			temp[i] = c[i];
		}
		//Sorting the array
		boolean loop = true;
		while(loop){
			int count = 0;
			for(int i = 0 ; i < temp.length-1; i++){
				if(temp[i]>temp[i+1]){
					int t = temp[i];
					temp[i] = temp[i+1];
					temp[i+1] = t;
				}
			}
			if(count == 0){loop = false;}
		}
		if(prevColor==0)
		{
			prevColor=temp[(temp.length/2)+1];
		}
		prevColor = color;
		color = temp[(temp.length/2)+1];
		if(color-prevColor<lineDer)
		{
			line = true;
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
