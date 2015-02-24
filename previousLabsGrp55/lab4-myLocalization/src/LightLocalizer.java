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
		nav.goForth(speed);
		crossLine();
		nav.travelDist(sensorDist);
		odo.setY(0);
		nav.spinCounterClockWise();
		crossLine();
		odo.setTheta(180);
		nav.rotateClockwise(90);
		nav.travelDist(2);
		nav.rotateClockwise(90);
		nav.goForth(speed);
		crossLine();
		nav.travelDist(sensorDist);
		odo.setX(0);
		nav.rotateClockwise(45);
		nav.spinClockWise();
		crossLine();
		odo.setTheta(270);
		nav.travelDist(odo.getY());
	//	nav.pointTo(90.0);
		nav.rotateClockwise(-180);
//		nav.rotateClockwise(-135);
//		nav.spinCounterClockWise();
//		crossLine();
		
		nav.stopMotors();
		//odo.setTheta(90);
		odo.lsStop();
		
	}	

	public void clockLines() {
		odo.lsStart();
		nav.rotateClockwise(45);
		int i = 0;
		double x;
		double y;
		double[] angles = new double[4];
		odo.setX(i);
		
		do {
			nav.rotateClockwise(5);
			nav.spinClockWise();
			crossLine();
			nav.stopMotors();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
			angles[i] = odo.getTheta();
			odo.setX(i);
			i++;
		
		}while(i<4);
		nav.stopMotors();

		if (angles[0] <= 180) {
			x = -Math.abs(sensorDist * Math.cos(((Math.abs(angles[1] - angles[3]) / 2))*3.15159/180));
		} else {
			x = Math.abs(sensorDist * Math.cos(((Math.abs(angles[1] - angles[3]) / 2))*3.15159/180));
		}
		if (angles[3] >= 90 && angles[3] < 270) {
			y = Math.abs(sensorDist * Math.sin(((Math.abs(angles[0] - angles[2]) / 2))*3.15159/180));
		} else {
			y = -Math.abs(sensorDist * Math.sin(((Math.abs(angles[0] - angles[2]) / 2))*3.15159/180));
		}

		odo.setX(x);
		odo.setY(y);
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}

		nav.travelTo(0, 0);
		nav.pointTo(90.0);
		odo.lsStop();

	}

	//lets the nxt do whatever it is doing until a line is crossed
	//the following method filter any individual values that aren't representative of the real color the sensor sees
	private void crossLine() {
		
		int counter = 0;
		int maxCount = 5;
		double black = 340;
		double sensorColor = 500;
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
