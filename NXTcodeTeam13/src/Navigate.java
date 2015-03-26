import lejos.nxt.Sound;
import lejos.robotics.RegulatedMotor;
/**
 * The Complete set of methods that allow robot mobility at a low level of complexity
 * @author Cedric
 *
 */
public class Navigate {
	public RegulatedMotor leftMotor;
	public RegulatedMotor rightMotor;
	private Robot robot;
	public Follower follower;
	public Localizer localizer;

	private double xDest;
	private double yDest;

	private Odometer odo;




	public Navigate(Robot r, Odometer o) {
		robot = r;
		leftMotor = r.leftMotor;
		rightMotor = r.rightMotor;
		odo = o;
		follower = new Follower(robot,this);
		localizer  = new Localizer(this, robot);
		setAccSp(robot.acc, robot.speed);
	}
	/**
	 * travels to a specific location
	 * @param x coordinate
	 * @param y coordinate
	 * @param follow : whether to implement wall follower or not going to the location
	 */
	public void travelTo(double x, double y,boolean follow) {
		
		//odo.lsC.restartLS();

		odo.correction.restart();
		qBreak(50);
		if (follow){ odo.usCfront.restartUS(); }
		xDest = x;
		yDest = y;
		setAccSp(robot.acc,robot.speed);
		goForth();
		double store = distToDest();
		// sets nxt pointing towards destination
		pointToDest();
		// keep navigating as long as destination isn't reached
		while (distToDest() >= 3) {
			//while no obstacle and not arrived at destination
			while (follow && odo.getFrontSensorDist() > robot.minFrontWallDist) {
				if(distToDest()<robot.minFrontWallDist+1){
					follow = false;
					odo.usCfront.stopUS();
					
				}
				 store = distToDest() ; 
				goForth();
				try {Thread.sleep(300);} catch (Exception e) {}
				if (distToDest() > store) { pointToDest();}		// check if heading away from destination and correct  angle(if no obstacle)
			}
			// if obstacle implement wall follower
			/*
			 * ========================================
			 *	CHECK SYNC IN IF STATEMENT BELOW
			 * ======================================== 
			 */
			if (follow && (odo.getFrontSensorDist() < robot.minFrontWallDist  )) { 
				rotateClockwise(90);
				follower.follow(true) ;
			}
			if(deltaAngle(destAngle())>1){
			 pointToDest();
			 }
			 goForth();
		}
		stopMotors();
		pointToDest();
		travelDist(distToDest());
		stopMotors();
		// Arrived at  final destination;
		stopMotors();
		qBreak(300);
		qBreak(300);
		stopMotors();
	}
	/**
	 * sets Acceleration and speed for both motors
	 * @param acc
	 * @param sp
	 */
	public void setAccSp(double acc, double sp){
		leftMotor.setAcceleration((int)acc);
		rightMotor.setAcceleration((int)acc);
		rightMotor.setSpeed((int)sp);
		leftMotor.setSpeed((int)sp);
	}

	/**
	 * calculates distance between robot and destination
	 * @return
	 */
	private double distToDest() {
		double y = Math.abs(odo.getY() - yDest);
		double x = Math.abs(odo.getX() - xDest);
		double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return distance;
	}
	/**
	 * rotates the robot clockwise a given angle
	 * handles negative angles
	 * @param angle
	 */
	public void rotateClockwise(double angle) {
		leftMotor.rotate(convertAngle(robot.leftWradius,robot.wwDist, angle), true);
		rightMotor.rotate(-convertAngle(robot.rightWradius, robot.wwDist, angle), false);
	}

	/**
	 * travels a given distance in a straight line
	 * @param distance
	 */
	public void travelDist(double distance) {
		rightMotor.rotate(convertDistance(robot.rightWradius, distance), true);
		leftMotor.rotate(convertDistance(robot.leftWradius, distance), false);

	}
	/**
	 * Robot goes forward until told to do something else
	 */
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
		setAccSp(robot.acc,robot.speed);

	}
	/**
	 * robot spins clockwise until told to do something else
	 */
	public void spinClockWise() {
		leftMotor.forward();
		rightMotor.backward();
	}
	/**
	 * robot spins counter-clockwise until told to do something else
	 */
	public void spinCounterClockWise() {
		rightMotor.forward();
		leftMotor.backward();
	}
	/**
	 * Converts a distance and returns the number of degrees the wheels must turn to travel that distance
	 * @param radius
	 * @param distance
	 * @return
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

	/**
	 * the Nxt rotates an angle [-180;180] to point to an angle relative to the map (0 = east ; 90 = north; 180 = west ; 270 = south)
	 * @param destAngle
	 */
	public void pointTo(double destAngle) {
		double angle = deltaAngle(destAngle);
		rotateClockwise(-angle);
	}


	/**
	 * the nxt spins minimum angle to point towards current destination
	 */
	public void pointToDest() {
		double angle = deltaAngle(destAngle());
		rotateClockwise(-angle);
	}


	/**
	 * calculate the difference in angle between the current odometer's Theta's and a designated angle
	 * the angle is oriented correctly and given in range [-180; +180]
	 * @param destAngle
	 * @return
	 */
	public double deltaAngle(double destAngle) {
		destAngle = (destAngle + 360) % 360;
		double theta = odo.getTheta();
		if (theta > 180) {
			theta -= 360;
		}
		if (destAngle > 180) {
			destAngle -= 360;
		}
		double deltaAngle = destAngle - theta;

		if (deltaAngle > 180) {
			deltaAngle -= 360;
		}
		if (deltaAngle < -180) {
			deltaAngle += 360;
		}
		return deltaAngle;
	}
	/**
	 * updates the angle at which the destination is relative to the robot
	 */
	public double destAngle(){
		double destAngle;
		destAngle =Math.atan2(yDest - odo.getY(),xDest - odo.getX());
		destAngle = Math.toDegrees(destAngle);
//		destAngle = Math.atan2(yDest - odo.getY(), xDest - odo.getX()) ;
//		if(xDest - odo.getX()>0){
//		destAngle = Math.atan((yDest - odo.getY())/(xDest - odo.getX()));
//		destAngle = 180 - Math.toDegrees(destAngle);
//		}
//		
//		else{
//			destAngle =  Math.atan((yDest - odo.getY())/(xDest - odo.getX()));
//			destAngle =  - Math.toDegrees(destAngle);
//		}
		return destAngle;
	}
	/**
	 * stops the Robot for a short moment for test purposes and so on 
	 */
	public void qBreak(int time){
		Sound.beep();
		stopMotors();
		try {Thread.sleep(time);} catch (Exception e) {}
		setAccSp(robot.acc, robot.speed);
	}
}
