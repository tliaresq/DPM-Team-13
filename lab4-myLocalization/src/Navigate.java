import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Motor;

public class Navigate extends Thread {
	private static final int ROTATE_SPEED = 100;
	private static NXTRegulatedMotor leftMotor;
	private static NXTRegulatedMotor rightMotor;
	private static NXTRegulatedMotor sensorMotor = Motor.C;
	private static double rightRadius;
	private static double leftRadius;
	private static double xDest;
	private static double yDest;
	private static double width;
	private static Odometer odometer;
	private static boolean isNavigating;
	private static double store;

	public Navigate(NXTRegulatedMotor lMotor, NXTRegulatedMotor rMotor,
			double lRadius, double rRadius, double w, Odometer odo) {
		new Object();
		leftMotor = lMotor;
		rightMotor = rMotor;
		leftRadius = lRadius;
		rightRadius = rRadius;
		width = w;
		odometer = odo;
		isNavigating = false;

	}

	public void run() {
		// limitation: if a destination can't be reached, the nxt will turn
		// around it forever
	}

	public void demoOne() {
		
		// destinations to go to in that order
		travelTo(60, 30);
		travelTo(30, 30);
		travelTo(30, 60);
		travelTo(60, 0);
	}

	public void demoTwo() {
		//implements USController

		// destinations to go to in that order
		odometer.usStart();
		travelTo(0, 60);
		travelTo(60, 0);
		odometer.usStop();
	}

	public static void travelTo(double x, double y) {
		isNavigating = true;
		xDest = x;
		yDest = y;
		int objectDist = 15;

		// innitializing motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor,
				rightMotor }) {
			motor.stop();
			motor.setAcceleration(750);

		}
		// sets nxt pointing towards destunation
		turnTo();

		// keep navigating as long as destination isn't reached

		while (distToDest() >= 1) {
			int i = 0;
			while (distToDest() >= 1 && odometer.getSensorDist() >= objectDist) {
				i++;
				if (i % 5 == 0) {
					store = distToDest();
				}
				leftMotor.forward();
				rightMotor.forward();
				leftMotor.setSpeed(200);
				rightMotor.setSpeed(200);
				// check if heading away from destination and correct angle(if
				// no obstacle)
				if (distToDest() - 1 > store) {
					turnTo();
				}
			}

			// if obstacle implement wall follower
			if (distToDest() > 1 && odometer.getSensorDist() <= objectDist) {
				follow(x, y, odometer);
				sensorMotor.rotate(80, true);
			}
		}
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		isNavigating = false;
	}

	private static void follow(double x, double y, Odometer odometer) {
		double distance = odometer.getSensorDist();
		double bandCenter = 10;
		double bandwith = 3;
		int motorHigh = 300;
		int motorStraight = 150;
		double delta;
		boolean inBand = false;
		// initiating nxt wall following state
		// limitation: doesn't see obstacle right ahead if already following an
		// obstacle;
		sensorMotor.rotate(-80, true);
		leftMotor.rotate(convertAngle(leftRadius, width, 90), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, 90), false);
		delta = deltaAngle();

		// follow obstacle while the theta isn't within 5 degrees of the angle
		// needed to reach destination
		while (Math.abs(delta) > 5) {
			// basic wall follower
			if (distance >= (bandCenter - bandwith)
					&& distance <= (bandCenter + bandwith)) {
				inBand = true;
			} else {
				inBand = false;
			}
			if (inBand) {
				leftMotor.forward();
				rightMotor.forward();
				leftMotor.setSpeed(motorHigh);
				rightMotor.setSpeed(motorHigh);
			} else if (distance < (bandCenter - bandwith)) {
				leftMotor.forward();
				rightMotor.forward();
				leftMotor.setSpeed(motorHigh);
				rightMotor.setSpeed(motorStraight / 2);
			} else if (distance > bandCenter + bandwith && distance < 50) {
				leftMotor.forward();
				rightMotor.forward();
				rightMotor.setSpeed(motorHigh);
				leftMotor.setSpeed(motorStraight);
			} else if (distance > 49) {
				leftMotor.forward();
				rightMotor.forward();
				rightMotor.setSpeed(motorHigh);
				leftMotor.setSpeed(motorStraight / 2);
			}
			distance = odometer.getSensorDist();
			delta = deltaAngle();
		}

		leftMotor.forward();
		rightMotor.forward();
		turnTo();
	}

	// calculates distance between nxt and destination
	private static double distToDest() {
		double distance;

		double y = Math.abs(odometer.getY() - yDest);
		double x = Math.abs(odometer.getX() - xDest);

		distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

		return distance;
	}

	// calculate the difference in angle between the current angle and the angle
	// pointing to direction
	// the angle is oriented correctly and given in range [-180; +180]
	private static double deltaAngle() {
		double angle = Math.atan2(yDest - odometer.getY(),
				xDest - odometer.getX()) * 180 / 3.14159;
		angle = (angle + 360) % 360;
		double theta = odometer.getTheta();
		if (theta > 180) {
			theta -= 360;
		}
		if (angle > 180) {
			angle -= 360;
		}

		angle = theta - angle;

		if (angle > 180) {
			angle -= 360;
		}
		if (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	// the nxt spins minimum angle to point towards current destination
	public static void turnTo() {
		double angle = deltaAngle();
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);
	}

	public void rotateClockwise(double angle) {
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);

	}

	// travels a given distance in a straight line
	// never used in the final code but very useful in general
	public static void travelDist(double distance) {
		leftMotor.setAcceleration(4000);
		rightMotor.setAcceleration(4000);
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		leftMotor.forward();
		rightMotor.forward();

		leftMotor.rotate(convertDistance(leftRadius, distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance), false);

	}

	public void goForth(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		leftMotor.forward();
		rightMotor.forward();

	}

	public static void stopMotors() {
		leftMotor.setAcceleration(4000);
		rightMotor.setAcceleration(4000);
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		leftMotor.stop();
		rightMotor.stop();
	}

	public static void spinClockWise() {
		leftMotor.setAcceleration(900);
		rightMotor.setAcceleration(900);
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		leftMotor.forward();
		rightMotor.backward();

	}

	public static void spinCounterClockWise() {
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		rightMotor.forward();
		leftMotor.backward();

	}

	// Given method in last lab
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	// Given method in last lab
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

	// returns true if the nxt is currently driving to a destination
	public boolean isNavigating() {
		return isNavigating;
	}

	public void pointTo(double angle) {
		double theta = odometer.getTheta();
		if (theta > 180) {
			theta -= 360;
		}
		if (angle > 180) {
			angle -= 360;
		}

		angle = theta - angle;

		if (angle > 180) {
			angle -= 360;
		}
		if (angle < -180) {
			angle += 360;
		}
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);

	}
}
