import lejos.nxt.LightSensor;
import lejos.nxt.Sound;

public class LightLocalizer {

	private Odometer odo;
	private Navigate nav;
	private int speed = 100;
	private double sensorDist = 10;

	public LightLocalizer(Odometer odo, Navigate navigation) {
		this.odo = odo;
		nav = navigation;

	}

	public void doLocalization() {
		odo.lsStart();
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		nav.goForth(speed);
		crossLine();
		nav.travelDist(sensorDist);
		odo.setY(0);
		nav.spinCounterClockWise();
		crossLine();
		odo.setTheta(180);
		nav.rotateClockwise(90);
		nav.travelDist(15);
		nav.rotateClockwise(90);
		nav.goForth(speed);
		crossLine();
		nav.travelDist(sensorDist);
		odo.setX(0);
		nav.spinClockWise();
		crossLine();
		nav.stopMotors();
		//try { Thread.sleep(3000); } catch(Exception e){}
		odo.setTheta(270);
		
		
		nav.travelDist(odo.getY());
		nav.rotateClockwise(135);
		nav.spinClockWise();
		crossLine();
		nav.stopMotors();
		odo.setTheta(90);
	}
	
	private void crossLine(){
		int counter = 0;
		int maxCount=5;
		double black = 340;
		double sensorColor=500;
		do {
			sensorColor = odo.getSensorColor();
			if (sensorColor < black) {
				counter++;
			} else {
				counter = 0;
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		} while (counter < maxCount);
		Sound.beep();
	}
}
