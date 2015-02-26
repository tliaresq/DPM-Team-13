import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

public class Navigate {
	private NXTRegulatedMotor leftMotor;
	private NXTRegulatedMotor rightMotor;
	private Robot robot;

	private double xDest;
	private double yDest;

	private Odometer odometer;
	private double finalDestAngle;



	public Navigate(Robot r, Odometer o) {
		robot = r;
		leftMotor = r.leftMotor;
		rightMotor = r.rightMotor;
		odometer = o;
	}

	public void travelTo(double x, double y,boolean follow) {

		odometer.correctionOn();
		if (follow){ odometer.usOn(); }
		
		xDest = x;
		yDest = y;
		
		setAccSp(robot.acc,robot.speed);
				
		
		double store = distToDest();;
		// sets nxt pointing towards destination

		pointToDest();

		// keep navigating as long as destination isn't reached

		while (distToDest() >= 1) {
			int i = 0;
			//while no obstacle and not arrived at destination
			while (distToDest() >= 1 &&(!follow ||  odometer.getRightSensorDist() >= robot.wallDist)) {
				if(distToDest()<robot.odoCorBand){
					odometer.correctionOff();
				}
				if(distToDest()<robot.wallDist){
					follow = false;
				}
				i++;
				if (i % 2 == 0) { store = distToDest() ; } //stores the value of distance to destination
				goForth();
				if (distToDest() > store) { pointToDest() ; }		// check if heading away from destination and correct angle(if no obstacle)
			}
			
			// if obstacle implement wall follower
			if (follow && distToDest() > 1 && odometer.getRightSensorDist() < robot.wallDist) { 
				follow() ;
			}
			setAccSp(robot.acc,robot.speed);
			
		}
		Sound.beep();
		stopMotors();
		setAccSp(robot.acc,robot.speed);
		

		// Arrived at  final destination;
		pointTo(90);
		stopMotors();
		
	//	odometer.sensorsOff();

	}

	private void follow() {

		double leftDistance = odometer.getLeftSensorDist();
		double delta;
		double tooRight;
		double tooLeft ;

	
		// initiating nxt wall following state
		rotateClockwise(90);
		updateDestAngle();
		delta = deltaAngle(finalDestAngle);
		setAccSp(8000,robot.speed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (Math.abs(delta) > 5) {
			
			try {Thread.sleep(20);} catch (Exception e) {}
			tooRight = (Math.pow((odometer.getLeftSensorDist()/robot.wallDist),1));
			tooLeft =(Math.pow((robot.wallDist/odometer.getLeftSensorDist()),2));
			
			// checks for wall in front
			if (odometer.getRightSensorDist()<robot.wallDist){
				rotateClockwise(90);
			}
			//too left
			else if (leftDistance < robot.wallDist) {
				goForth();
				leftMotor.setSpeed((int) (robot.speed*tooLeft));
				rightMotor.setSpeed((int)robot.speed);
			} 

			//too right
			else if (leftDistance > robot.wallDist && leftDistance < 81) {
				goForth();
				rightMotor.setSpeed((int) (robot.speed*tooRight));
				leftMotor.setSpeed((int)robot.speed);
			} 

			// no more wall
			else if (leftDistance > 80) {
				goForth();
				rightMotor.setSpeed((int) (robot.speed));
				leftMotor.setSpeed((int)(robot.speed/tooRight));
			}
			leftDistance = odometer.getLeftSensorDist();
			updateDestAngle();
			delta = deltaAngle(finalDestAngle);
		}
		
		
		goForth();
		stopMotors();
		setAccSp(robot.acc,robot.speed);
		pointToDest();
	}
	
	
	//sets Acceleration and speed to both motors
	public void setAccSp(double acc, double sp){
		leftMotor.setAcceleration((int)acc);
		rightMotor.setAcceleration((int)acc);
		rightMotor.setSpeed((int)sp);
		leftMotor.setSpeed((int)sp);
	}

	// calculates distance between nxt and destination
	private double distToDest() {
		double y = Math.abs(odometer.getY() - yDest);
		double x = Math.abs(odometer.getX() - xDest);
		double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return distance;
	}

	public void rotateClockwise(double angle) {
		leftMotor.rotate(convertAngle(robot.leftWradius,robot.wwDist, angle), true);
		rightMotor.rotate(-convertAngle(robot.rightWradius, robot.wwDist, angle), false);
	}

	// travels a given distance in a straight line
	// never used in the final code but very useful in general
	public void travelDist(double distance) {
		leftMotor.rotate(convertDistance(robot.leftWradius, distance), true);
		rightMotor.rotate(convertDistance(robot.rightWradius, distance), false);

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

	//the Nxt rotates an angle [-180;180] to point to an angle relative to the map (0 = east ; 90 = north; 180 = west ; 270 = south)
	public void pointTo(double destAngle) {
		double angle = deltaAngle(destAngle);
		leftMotor.rotate(convertAngle(robot.leftWradius,robot.wwDist, angle), true);
		rightMotor.rotate(-convertAngle(robot.rightWradius, robot.wwDist, angle), false);
	}


	// the nxt spins minimum angle to point towards current destination
	public void pointToDest() {
		updateDestAngle();
		double angle = deltaAngle(finalDestAngle);
		leftMotor.rotate(convertAngle(robot.leftWradius, robot.wwDist, angle), true);
		rightMotor.rotate(-convertAngle(robot.rightWradius, robot.wwDist, angle), false);
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
