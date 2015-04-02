import lejos.nxt.UltrasonicSensor;

/**
 * Manages All the information coming from an ultra sonic sensor by filtering it and returning a filtered value. 
 * @author Cedric
 *
 */
public class USController extends Thread {
	public UltrasonicSensor usSensor;
	private int distance;
	private boolean stop;
	private int[] distances;


	public USController( UltrasonicSensor us, int filterSize) {
		usSensor = us;
		distance = -1;
		stop = true;
		distances = new int[filterSize];
	}

	public void run() {
		stop = true;
		while (true) {
			if (stop) {
				distance = -1;
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				for(int i = 0 ; i< distances.length-1; i ++){
					distances[i+1]  = distances[i];
				}
				distances[0] = usSensor.getDistance();
				distance = filter(distances);
				//				distance =   usSensor.getDistance();
				try {Thread.sleep(10);} catch (Exception e) {}
			}
		}
	}
	/**
	 * Filters the sensor value by taking the mean value
	 * @param c Number of most recent values to take the mean from
	 * @return
	 */
	private int filter(int[] c) {
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
//		double avrg = temp[(temp.length/2)]+temp[(temp.length/2)+1]+temp[(temp.length/2)+2];
//		avrg = avrg/3;
		return temp[(temp.length/2)+1];
//		return (int)avrg;
	}
	/**
	 * stops the US sensor
	 * Does not stop the thread from running
	 * This is mainly to save battery
	 */
	public void stopUS() {
		stop = true;
		usSensor.off();
		distance = -1;
	}
	/**
	 * restarts sensor
	 */
	public void restartUS(){
		usSensor.continuous();
		stop = false;
	}

	public int sensorDist() {
		return distance;
	}

}
