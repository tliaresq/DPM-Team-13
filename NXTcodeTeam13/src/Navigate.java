import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

public class Navigate {
	private NXTRegulatedMotor leftMotor;
	private NXTRegulatedMotor rightMotor;
	private Robot robot;
	private double rightRadius;
	private double leftRadius;
	private double xDest;
	private double yDest;
	private double width;
	private Odometer odometer;
	private boolean isNavigating;
	private double finalDestAngle;
	private double objectDist;
	private int defSpeed;
	private int defAcc;

	public Navigate(Robot r, Odometer odo) {
		robot = r;
		leftMotor = r.leftMotor;
		rightMotor = r.rightMotor;
		leftRadius = r.leftWradius;
		rightRadius = r.rightWradius;
		width = r.wwDist;
		objectDist = r.wallDist;
		odometer = odo;
		isNavigating = false;
		defSpeed = r.defSpeed;
		defAcc = r.defAcc;
		setAccSp(defSpeed,defAcc);

	}

	public void travelTo(double x, double y,boolean follow) {
		isNavigating = true;
		if (follow){odometer.usStart();}
		xDest = x;
		yDest = y;
		setAccSp(defAcc,defSpeed);
		double store = distToDest();;
		// sets nxt pointing towards destination
		odometer.restartCorrection();
		pointToDest();

		// keep navigating as long as destination isn't reached

		while (distToDest() >= 1) {
			int i = 0;
			//while no obstacle and not arrived at destination
			while (distToDest() >= 1 &&(!follow ||  odometer.getRightSensorDist() >= objectDist)) {
				if(distToDest()<robot.odoCorBand){
					odometer.stopCorrection();
				}
				i++;
				if (i % 2 == 0) { store = distToDest() ; } //stores the value of distance to destination
				goForth();
				if (distToDest() > store) { pointToDest() ; }		// check if heading away from destination and correct angle(if no obstacle)
			}
			
			// if obstacle implement wall follower
			if (follow && distToDest() > 1 && odometer.getRightSensorDist() < objectDist) { 
				follow() ;
			}
				setAccSp(defAcc,defSpeed);
			
		}
		Sound.beep();
		stopMotors();
		setAccSp(defAcc,defSpeed);
		

		// Arrived at  final desti`nation;
		pointTo(90);

		stopMotors();
	//	odometer.sensorsOff();
		isNavigating = false;
	}

	private void follow() {

		double leftDistance = odometer.getLeftSensorDist();
		double bandCenter = robot.wallDist;
		double delta;
		double tooRight;
		double tooLeft ;
		double speed = defSpeed;
	
		// initiating nxt wall following state
		rotateClockwise(90);
		updateDestAngle();
		delta = deltaAngle(finalDestAngle);
		setAccSp(8000,defSpeed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (Math.abs(delta) > 5) {
			
			try {Thread.sleep(20);} catch (Exception e) {}
			
			tooRight = (Math.pow((odometer.getLeftSensorDist()/bandCenter),1));
			tooLeft =(Math.pow((bandCenter/odometer.getLeftSensorDist()),2));

			// checks for wall in front
			if (odometer.getRightSensorDist()<objectDist){
				rotateClockwise(90);
			}


			//too left
			else if (leftDistance < bandCenter) {
				goForth();
				leftMotor.setSpeed((int) (speed*tooLeft));
				rightMotor.setSpeed((int)speed);
			} 

			//too right
			else if (leftDistance > bandCenter && leftDistance < 81) {
				goForth();
				rightMotor.setSpeed((int) (speed*tooRight));
				leftMotor.setSpeed((int)speed);
			} 

			// no more wall
			else if (leftDistance > 80) {
				goForth();
				rightMotor.setSpeed((int) (speed));
				leftMotor.setSpeed((int)(speed/tooRight));
			}
			leftDistance = odometer.getLeftSensorDist();
			updateDestAngle();
			delta = deltaAngle(finalDestAngle);
		}
		
		
		goForth();
		stopMotors();
		Sound.beep();
		setAccSp(defAcc,defSpeed);
		pointToDest();
	}
	
	
	//sets Acceleration and speed to both motors
	public void setAccSp(int acc, int sp){
		leftMotor.setAcceleration(acc);
		rightMotor.setAcceleration(acc);
		rightMotor.setSpeed(sp);
		leftMotor.setSpeed(sp);
	}

	// calculates distance between nxt and destination
	private double distToDest() {
		double y = Math.abs(odometer.getY() - yDest);
		double x = Math.abs(odometer.getX() - xDest);
		double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return distance;
	}

	public void rotateClockwise(double angle) {
		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);
	}

	// travels a given distance in a straight line
	// never used in the final code but very useful in general
	public void travelDist(double distance) {
		leftMotor.rotate(convertDistance(leftRadius, distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance), false);

	}
	public void goForth() {
		leftMotor.forward();
		rightMotor.forward();
	}
	
	/**
	 * stops both motors instantly
	 */
	public void stopMotors() {
		setAccSp(9000,0);
		leftMotor.stop();
		rightMotor.stop();
	}

	public void spinClockWise() {
		leftMotor.forward();
		rightMotor.backward();
	}

	public void spinCounterClockWise() {
		rightMotor.forward();
		leftMotor.backward();
	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

	// returns true if the nxt is currently driving to a destination
	public boolean isNavigating() {
		return isNavigating;
	}

	//the Nxt rotates an angle [-180;180] to point to an angle relative to the map (0 = east ; 90 = north; 180 = west ; 270 = south)
	public void pointTo(double destAngle) {
		double angle = deltaAngle(destAngle);
		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);
	}


	// the nxt spins minimum angle to point towards current destination
	public void pointToDest() {
		updateDestAngle();
		double angle = deltaAngle(finalDestAngle);
		leftMotor.rotate(convertAngle(leftRadius, width, angle), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, angle), false);
	}



	// calculate the difference in angle between the current odometer's Theta's and a designated angle
	// the angle is oriented correctly and given in range [-180; +180]
	public double deltaAngle(double destAngle) {
		destAngle = (destAngle + 360) % 360;
		double theta = odometer.getTheta();
		if (theta > 180) {
			theta -= 360;
		}
		if (destAngle > 180) {
			destAngle -= 360;
		}

		double deltaAngle = theta - destAngle;

		if (deltaAngle > 180) {
			deltaAngle -= 360;
		}
		if (deltaAngle < -180) {
			deltaAngle += 360;
		}
		return deltaAngle;
	}
	
	private void updateDestAngle(){
		finalDestAngle = Math.atan2(yDest - odometer.getY(),xDest - odometer.getX()) * 180 / 3.14159;
	}
}
