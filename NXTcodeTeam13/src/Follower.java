
public class Follower {

	private Navigate nav;
	private Robot robot;
	private Odometer odometer;
	private double delta;
	private double tooRight;
	private double tooLeft ;
	private double leftDistance;


	public Follower(Robot r, Navigate n, Odometer o){
		robot = r;
		nav = n;
		odometer = o;
	}


	public void destFollow() {
		leftDistance = odometer.getLeftSensorDist();


		// initiating nxt wall following state
		nav.rotateClockwise(90);
		nav.updateDestAngle();
		delta = nav.deltaAngle(nav.finalDestAngle);
		nav.setAccSp(8000,robot.speed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (Math.abs(delta) > 5) {

			try {Thread.sleep(20);} catch (Exception e) {}
			tooRight = (Math.pow((odometer.getLeftSensorDist()/robot.wallDist),1));
			tooLeft =(Math.pow((robot.wallDist/odometer.getLeftSensorDist()),2));

			// checks for wall in front
			if (odometer.getRightSensorDist()<robot.wallDist){
				nav.rotateClockwise(90);
			}
			//too left
			else if (leftDistance < robot.wallDist) {
				nav.goForth();
				nav.leftMotor.setSpeed((int) (robot.speed*tooLeft));
				nav.rightMotor.setSpeed((int)robot.speed);
			} 

			//too right
			else if (leftDistance > robot.wallDist && leftDistance < 81) {
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed*tooRight));
				nav.leftMotor.setSpeed((int)robot.speed);
			} 

			// no more wall
			else if (leftDistance > 80) {
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed));
				nav.leftMotor.setSpeed((int)(robot.speed/tooRight));
			}
			leftDistance = odometer.getLeftSensorDist();
			nav.updateDestAngle();
			delta = nav.deltaAngle(nav.finalDestAngle);
		}


		nav.goForth();
		nav.stopMotors();
		nav.setAccSp(robot.acc,robot.speed);
		nav.pointToDest();
	}
	public void followUntillWall(){

		leftDistance = odometer.getLeftSensorDist();
		nav.rotateClockwise(90);
		nav.setAccSp(8000,robot.speed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (odometer.getRightSensorDist()>robot.wallDist) {
			try {Thread.sleep(20);} catch (Exception e) {}
			tooRight = (Math.pow((odometer.getLeftSensorDist()/robot.wallDist),1));
			tooLeft =(Math.pow((robot.wallDist/odometer.getLeftSensorDist()),2));

			//too left
			if (leftDistance < robot.wallDist) {
				nav.goForth();
				nav.leftMotor.setSpeed((int) (robot.speed*tooLeft));
				nav.rightMotor.setSpeed((int)robot.speed);
			} 

			//too right
			else if (leftDistance > robot.wallDist/2) {
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed*tooRight));
				nav.leftMotor.setSpeed((int)robot.speed);
			} 
			leftDistance = odometer.getLeftSensorDist();
		}
		nav.stopMotors();
	}
}