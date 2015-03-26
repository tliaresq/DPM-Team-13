import lejos.nxt.ColorSensor;
import lejos.nxt.Sound;

/**
 * Manages All the information coming from a light sensor by filtering it and returning a filtered value. 
 * @author Cedric
 *
 */
public class LSController extends Thread {
	
	public ColorSensor colorSensor;
	private int prevColor;
	private int color;
	private boolean line;
	private boolean stop;
	private int[] colors;
	private int black = 500;
	//private final int lineDer = -20;
	private int fSize;

	public LSController(ColorSensor cs, int filterSize) {
		colorSensor = cs;
		prevColor = 0;
		line = false;
		stop = true;
		colors = new int[filterSize];
		fSize = filterSize;
	}

	public void run() {
		stop = true;
		int counter=0;
		while (true) {
			if (stop) {	
				line = false;
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				try {Thread.sleep(10);} catch (Exception e) {}
				if(colorSensor.getRawLightValue()<black){counter ++;}
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
	}
	
	/**
	 * Filters the sensor value by taking the mean value
	 * @param c Number of most recent values to take the mean from
	 * @return
	 */
	private int getMedian() {
 		int[] temp = new int[colors.length];
 		//copying the array not to affect the oder of C
 		for(int i = 0; i<colors.length; i++ ){
 			temp[i] = colors[i];
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
 		return temp[(temp.length/2)+1];
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
