import lejos.nxt.NXTRegulatedMotor;

public class Navigate {
	public NXTRegulatedMotor leftMotor;
	public NXTRegulatedMotor rightMotor;
	private Robot robot;
	public Follower follower;

	private double xDest;
	private double yDest;

	private Odometer odo;
	public double finalDestAngle;



	public Navigate(Robot r, Odometer o) {
		robot = r;
		leftMotor = r.leftMotor;
		rightMotor = r.rightMotor;
		odo = o;
		follower = new Follower(robot,this, odo);
	}

	public void travelTo(double x, double y,boolean follow) {

		odo.correctionOn();
		if (follow){ odo.usCfront.restartUS(); }

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
			while (distToDest() >= 1 &&(!follow ||  odo.getFrontSensorDist() >= robot.wallDist)) {
				if(distToDest()<robot.odoCorBand){
					odo.correctionOff();
				}
				if(distToDest()<robot.wallDist){
					follow = false;
					odo.usCfront.stopUS();
				}
				i++;
				if (i % 2 == 0) { store = distToDest() ; } //stores the value of distance to destination
				goForth();
				if (distToDest() > store) { pointToDest() ; }		// check if heading away from destination and correct angle(if no obstacle)
			}

			// if obstacle implement wall follower
			if (follow && distToDest() > 1 && odo.getFrontSensorDist() < robot.wallDist) { 
				follower.destFollow() ;
			}
			setAccSp(robot.acc,robot.speed);

		}
		stopMotors();
		setAccSp(robot.acc,robot.speed);
		checkLocation();
		pointTo(90);
		// Arrived at  final destination;
		stopMotors();
	}


	//sets Acceleration and speed to both motors
	public void setAccSp(double acc, double sp){
		leftMotor.setAcceleration((int)acc);
		rightMotor.setAcceleration((int)acc);
		rightMotor.setSpeed((int)sp);
		leftMotor.setSpeed((int)sp);
	}


	private void checkLocation(){
		double tempX = odo.getX();
		double tempY = odo.getY();
		odo.correctionOn();
		rotateClockwise(360);
		odo.correctionOff();
		xDest = tempX;
		yDest = tempY;
		pointToDest();
		travelDist(distToDest());


	}

	// calculates distance between nxt and destination
	private double distToDest() {
		double y = Math.abs(odo.getY() - yDest);
		double x = Math.abs(odo.getX() - xDest);
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

	public void findWAll(){
		spinClockWise();
		while (odo.getFrontSensorDist()< robot.findWallDist);{
			try {Thread.sleep(10);} catch (Exception e) {}
		}

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
		double theta = odo.getTheta();
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

	public void updateDestAngle(){
		finalDestAngle = Math.atan2(yDest - odo.getY(),xDest - odo.getX()) * 180 / 3.14159;
	}
}
