import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Navigate {
	private static final int FORWARD_SPEED = 200;
	private static final int ROTATE_SPEED = 150;
	private static NXTRegulatedMotor leftMotor;
	private static NXTRegulatedMotor rightMotor;
	private static double rightRadius;
	private static double leftRadius;
	private static int xDest;
	private static int yDest;
	private static double width;
	
	public static void drive(int x, int y, NXTRegulatedMotor lMotor,
			NXTRegulatedMotor rMotor, double lRadius,
			double rRadius, double w) {
		
		double distanceLeft;
		leftMotor=lMotor;
		rightMotor=rMotor;
		leftRadius= lRadius;
		rightRadius= rRadius;
		xDest=x;
		yDest=y;
		width = w;
		
		
		
		
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor }) {
			motor.stop();
			motor.setAcceleration(3000);
		}

		// wait 5 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}

			Odometer odometer = new Odometer();
			OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
			
			
			
			directToDest(odometer);
			
			travelDist(distToDest(odometer));
			
			leftMotor.setSpeed(0);
			rightMotor.setSpeed(0);
		
			
			//leftMotor.rotate(convertDistance(leftRadius, 60.96), true);
			//rightMotor.rotate(convertDistance(rightRadius, 60.96), false);
			


	}
	
	
	private static double distToDest(Odometer odometer){
		double distance;
		
		double y= Math.abs(odometer.getY()-yDest);
		double x= Math.abs(odometer.getX()-xDest);
		
		distance = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		
		odometer.setV(distance);
		
		
		return distance;
	}
	
	

	private static void directToDest(Odometer odometer) {
		
		double angle = Math.atan2(yDest,xDest)*180/3.14159;
		double theta= odometer.getTheta();
		
		if(theta-angle<=180 ){
			angle = theta-angle;
		}else{
			angle = theta+angle;
		}
		
		
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);

		
		
	}
	
	private static void travelDist(double distance){


		leftMotor.rotate(convertDistance(leftRadius, distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance), false);

	}


	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}
