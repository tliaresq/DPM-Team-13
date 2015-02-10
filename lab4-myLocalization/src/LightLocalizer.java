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
		nav.travelDist(15);
		nav.rotateClockwise(90);
		nav.goForth(speed);
		crossLine();
		nav.travelDist(sensorDist);
		odo.setX(0);
		nav.spinClockWise();
		crossLine();
		odo.setTheta(270);
		nav.travelDist(odo.getY());
		nav.rotateClockwise(135);
		nav.spinClockWise();
		crossLine();
		nav.stopMotors();
		odo.setTheta(90);
		odo.lsStop();
	}

	private void clock4lines() {

		nav.rotateClockwise(45);
		int i = 0;
		double x;
		double y;
		double[] angles = new double[4];
		while (i < 3) {
			nav.spinClockWise();
			crossLine();
			angles[i] = odo.getTheta();
		}
		nav.stopMotors();

		if (angles[0] <= 180) {
			x = -sensorDist * Math.cos((Math.abs(angles[1] - angles[3]) / 2));
		} else {
			x = sensorDist * Math.cos((Math.abs(angles[1] - angles[3]) / 2));
		}
		if (angles[3] >= 90 && angles[3] < 270) {
			y = sensorDist * Math.sin((Math.abs(angles[0] - angles[2]) / 2));
		} else {
			y = -sensorDist * Math.sin((Math.abs(angles[0] - angles[2]) / 2));
		}

		odo.setX(x);
		odo.setY(y);
		try {
			Thread.sleep(7000);
		} catch (Exception e) {
		}

		nav.travelTo(0, 0);
		nav.pointTo(90.0);

	}

	private void crossLine() {
		odo.lsStart();
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
		odo.lsStop();
	}
}
