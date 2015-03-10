import lejos.nxt.ColorSensor;

public class LSController extends Thread {
	
	private ColorSensor colorSensor;
	private int color;
	private boolean stop;
	private int[] colors;

	public LSController(ColorSensor cs, int filterSize) {
		colorSensor = cs;
		color = -1;
		stop = true;
		colors = new int[filterSize];
	}

	public void run() {
		stop = true;
		while (true) {
			if (stop) {	
				color = -1;
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				for(int i = 0 ; i< colors.length-1; i ++){
					colors[i+1]  = colors[i];
				}
				colors[0] = colorSensor.getRawLightValue();
				color=filter(colors);
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

	public void stopLS() {
		stop = true;
		colorSensor.setFloodlight(false);
		color = -1;
	}

	public void restartLS(){
		stop = false;
		colorSensor.setFloodlight(0);
	}

	public int getColor() {
		return color;
	}



}
