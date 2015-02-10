import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;


public class LSController extends Thread {
	static ColorSensor cs = new ColorSensor(SensorPort.S2);
	private int color;
	
	public LSController (){
		color = 0;
	}
	
	public void run() {
		while (true) {
			//process collected data
			color = cs.getRawLightValue();
			try { Thread.sleep(10); } catch(Exception e){}
		}
	}
	
	public int getColor(){
		return color;
	}

}
