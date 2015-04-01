import lejos.nxt.Sound;


/**
 * Implements Localisation methods to localize at specific times
 * Different localization methods may be used depending on how inaccurate is the odometer at the time of localization and the known environment
 * @author Cedric
 *
 */
public class Localizer {
	private Odometer odo;
	private Navigate nav;
	private Robot robot;


	public Localizer( Navigate n, Robot r) {
		odo = r.odo;
		nav = n;
		robot = r;
	}

	public void risingEdge(){
		odo.usCfront.restartUS();
		nav.setAccSp(2000, 200);
		nav.spinCounterClockWise();
		findWall();
		nav.spinCounterClockWise();
		findNoWall();
		odo.setTheta(0);
		Sound.beep();
		nav.spinClockWise();
		findWall();
		nav.spinClockWise();
		findNoWall();
		Sound.beep();
		odo.setTheta(90 + (odo.getTheta() / 2 - 45));
		nav.pointTo(180);
		odo.setX(8+odo.getFrontSensorDist()-38);
		nav.pointTo(270);
		odo.setY(8+odo.getFrontSensorDist()-38);
		nav.travelToRelocalizeCross(0, 0, false);
		nav.qBreak(500);
		nav.qBreak(500);
		nav.qBreak(500);

	}



	/**
	 * to work the robot needs to be pointing north
	 * @param x	line directly east of the light sensor
	 * @param y line directly north of the light sensor
	 */
	public void lineLocalize(double x, double y) {
		nav.setAccSp(9000, 100);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setY(y);
		nav.spinClockWise();
		nav.crossLine();
		odo.setTheta(0);
		nav.rotateClockwise(90);
		nav.travelDist(4);
		nav.rotateClockwise(-90);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(x);
		nav.setAccSp(robot.acc, robot.speed);
	}


	private void findWall() {
		int counter = 0;
		double sensorDist;
		int maxCount = 5;
		do {
			sensorDist = odo.getFrontSensorDist();
			if (sensorDist <=60  ) {
				counter++;
			} else {
				counter = 0;
			}
		} while (counter < maxCount);

	}

	private void findNoWall() {
		int counter = 0;
		double sensorDist;
		int maxCount = 5;
		do {
			sensorDist = odo.getFrontSensorDist();
			try {
				Thread.sleep(20);
			} catch (Exception e) {
			}
			if (sensorDist >= 60) {
				counter++;
			} else {
				counter = 0;
			}
		} while (counter < maxCount);
	}


	private double angleDiff(double a,double b) {
		a = (a + 360) % 360;
		if (b > 180) {
			b -= 360;
		}
		if (a > 180) {
			a -= 360;
		}
		double deltaAngle = a - b;

		if (deltaAngle > 180) {
			deltaAngle -= 360;
		}
		if (deltaAngle < -180) {
			deltaAngle += 360;
		}
		return deltaAngle;
	}

}