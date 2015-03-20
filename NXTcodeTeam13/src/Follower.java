import lejos.nxt.Sound;

/**
 * Implements Obstacle avoidance
 * @author Cedric
 *
 */
public class Follower {

	private Navigate nav;
	private Robot robot;
	private Odometer odo;
	private double delta;
	private double tooRight;
	private double tooLeft ;
	private double leftDistance;


	public Follower(Robot r, Navigate n){
		robot = r;
		nav = n;
		odo = robot.odo;
	}


	public void follow(boolean withDest) {

		
		robot.odo.usCleft.restartUS();
		if(withDest){
			delta = nav.deltaAngle(nav.finalDestAngle);
		}
		nav.setAccSp(8000,robot.speed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (Math.abs(delta)>5 || !withDest) {
			leftDistance = odo.getLeftSensorDist();
			try {Thread.sleep(15);} catch (Exception e) {}
			tooRight = (Math.pow((odo.getLeftSensorDist()/robot.followerSideDist),1));
			tooLeft =(Math.pow((robot.followerSideDist/odo.getLeftSensorDist()),2));

			// checks for wall in front
			if (odo.getFrontSensorDist()<robot.minFrontWallDist){
				nav.rotateClockwise(90);
			}
			//too left
			else if (leftDistance < robot.followerSideDist) {
				nav.goForth();
				nav.leftMotor.setSpeed((int) (robot.speed*tooLeft));
				nav.rightMotor.setSpeed((int)robot.speed);
			} 
			
			//too right
			else if (leftDistance > robot.followerSideDist && leftDistance < 81) {
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed*tooRight));
				nav.leftMotor.setSpeed((int)robot.speed);
			} 

			// no more wall
			else if (leftDistance > 80){
				Sound.beep();
				nav.setAccSp(robot.acc, robot.speed);
				nav.travelDist(20);
				nav.rotateClockwise(-90);
				nav.travelDist(20);
				nav.setAccSp(8000,robot.speed);
			}
			
			nav.updateDestAngle();
			delta = nav.deltaAngle(nav.finalDestAngle);
		}
		robot.odo.usCleft.stopUS();
	}
	public void followUntillWall(){

		leftDistance = odo.getLeftSensorDist();
		nav.rotateClockwise(90);
		nav.setAccSp(8000,robot.speed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (odo.getFrontSensorDist()>robot.minFrontWallDist) {
			try {Thread.sleep(20);} catch (Exception e) {}
			tooRight = (Math.pow((odo.getLeftSensorDist()/robot.minFrontWallDist),1));
			tooLeft =(Math.pow((robot.minFrontWallDist/odo.getLeftSensorDist()),2));

			//too left
			if (leftDistance < robot.minFrontWallDist) {
				nav.goForth();
				nav.leftMotor.setSpeed((int) (robot.speed*tooLeft));
				nav.rightMotor.setSpeed((int)robot.speed);
			} 

			//too right
			else if (leftDistance > robot.minFrontWallDist/2) {
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed*tooRight));
				nav.leftMotor.setSpeed((int)robot.speed);
			} 
			leftDistance = odo.getLeftSensorDist();
		}
		nav.stopMotors();
	}
}
