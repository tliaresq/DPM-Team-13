import lejos.nxt.NXTRegulatedMotor;

public class Navigate extends Thread {
	private static final int FORWARD_SPEED = 200;
	private static final int ROTATE_SPEED = 150;
	private static NXTRegulatedMotor leftMotor;
	private static NXTRegulatedMotor rightMotor;
	private static double rightRadius;
	private static double leftRadius;
	private static double xDest;
	private static double yDest;
	private static double width;
	private static Odometer odometer;
	private static OdometryDisplay odometryDisplay;
	private static boolean isNavigating;

	public Navigate(NXTRegulatedMotor lMotor, NXTRegulatedMotor rMotor,
			double lRadius, double rRadius, double w, Odometer odo,
			OdometryDisplay display) {
		new Object();
		leftMotor = lMotor;
		rightMotor = rMotor;
		leftRadius = lRadius;
		rightRadius = rRadius;
		width = w;
		odometer = odo;
		odometryDisplay = display;
		isNavigating= false;
	}

	public void run() {
		travelTo(60,30);

		travelTo(30,30);

		travelTo(30,60);
		
		travelTo(60,0);
	}

	public static void travelTo(double x, double y) {
		isNavigating = true;
		xDest = x;
		yDest = y;

		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor,
				rightMotor }) {
			motor.stop();
			motor.setAcceleration(3000);
		}

	
		
		turnTo();
		travelDist(distToDest());
		isNavigating = false;
	}

	private static double distToDest() {
		double distance;

		double y = Math.abs(odometer.getY() - yDest);
		double x = Math.abs(odometer.getX() - xDest);

		distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

		odometer.setV(distance);

		return distance;
	}

	private static void turnTo() {

		double angle = Math.atan2(yDest-odometer.getY(), xDest-odometer.getX()) * 180 / 3.14159;
		double theta = odometer.getTheta();

		if (theta - angle <= 180) {
			angle = theta - angle;
		} else {
			angle = theta + angle;
		}

		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);
	}

	private static void travelDist(double distance) {

		leftMotor.rotate(convertDistance(leftRadius, distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance), false);

	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	public boolean isNavigating(){
		return isNavigating;
	}
}
