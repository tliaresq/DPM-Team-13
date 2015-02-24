import lejos.nxt.Sound;

/**
 * ==============================
 * TO BE MODIFIED COMPLETLY
 * ==============================
 */


public class USLocalizer {
	public enum LocalizationType {
		FALLING_EDGE, RISING_EDGE
	};

	public static double ROTATION_SPEED = 30;

	private Odometer odo;
	private Navigate nav;
	private LocalizationType locType;
	private double sensorDist;
	private double wallDist;
	private int counter;
	private int maxCount;
	private int defAcc;
	private int defSpeed;

	public USLocalizer(Odometer odo, Navigate navigation,Robot r,
			LocalizationType locType) {
		this.odo = odo;
		this.locType = locType;
		nav = navigation;
		sensorDist = 50;
		counter = 0;
		maxCount = 22;
		wallDist = r.wallDist;
		defAcc = r.defAcc;
		defSpeed = r.defSpeed;
	}

	//the two following methods filter any individual values that aren't representative of the real distance of the sensor
	private void findWall() {
		counter = 0;
		do {
			sensorDist = odo.getRightSensorDist();
			if (sensorDist <= wallDist) {
				counter++;
			} else {
				counter = 0;
			}
		} while (counter < maxCount);

	}

	private void findNoWall() {
		counter = 0;
		do {
			sensorDist = odo.getRightSensorDist();
			try {
				Thread.sleep(20);
			} catch (Exception e) {
			}
			if (sensorDist >= wallDist) {
				counter++;
			} else {
				counter = 0;
			}
		} while (counter < maxCount);
	}

	public void doLocalization() {
		nav.setAccSp(defSpeed, defAcc);
		double angleB;
		odo.usStart();


		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
			nav.spinClockWise();
			findNoWall();
			nav.stopMotors();
			// keep rotating until the robot sees a wall, then latch the angle
			nav.spinClockWise();
			findWall();
			nav.stopMotors();
			odo.setTheta(0);
			Sound.beep();
			// switch direction and wait until it sees no wall
			nav.spinCounterClockWise();
			findNoWall();
			nav.stopMotors();
			// keep rotating until the robot sees a wall, then latch the angle
			nav.spinCounterClockWise();
			findWall();
			nav.stopMotors();
			angleB = odo.getTheta();
			Sound.beep();
			double alpha = -45 + angleB / 2;
			nav.rotateClockwise(alpha);

			// update the odometer position
			odo.setPosition(new double[] { 0.0, 0.0, 90.0 }, new boolean[] {
					true, true, true });
		} else {

			nav.spinCounterClockWise();
			findWall();
			nav.spinCounterClockWise();
			findNoWall();
			nav.stopMotors();
			odo.setTheta(0);
			Sound.beep();
			nav.spinClockWise();
			findWall();
			nav.stopMotors();
			nav.spinClockWise();
			findNoWall();
			nav.stopMotors();
			angleB = odo.getTheta();
			Sound.beep();
			double alpha = angleB / 2 - 45;
			nav.rotateClockwise(alpha);
			// update the odometer position
			odo.setPosition(new double[] { 0.0, 0.0, 90.0 }, new boolean[] {
					true, true, true });
		}
		odo.usStop();
	}
}
