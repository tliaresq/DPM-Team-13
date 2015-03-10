import lejos.nxt.UltrasonicSensor;

public class USController extends Thread {
	UltrasonicSensor usSensor;
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
		return temp[(temp.length/2)+1];
	}

	public void stopUS() {
		stop = true;
		usSensor.off();
		distance = -1;
	}
	
	public void restartUS(){
		stop = false;
	}

	public int sensorDist() {
		return distance;
	}

}
