
/**
 * Implements Obstacle avoidance
 * @author Cedric
 *
 */
public class Follower {

	private Navigate nav;
	private Robot robot;
	private Odometer odo;
	private double farHigh;
	private double closeHigh ;
	private double leftDistance;

	public Follower(Robot r, Navigate n){
		robot = r;
		nav = n;
		odo = robot.odo;
	}
	/**
	 * This method follows a wall on the left of a robot
	 * @param withDest if true, the method will check whether the robot is pointing towards the navigation's destination and stop the wall follower if it is the case
	 */
	public void follow(boolean withDest) {
		robot.odo.usCleft.restartUS();
		nav.setAccSp(robot.wallFollowAcc,robot.wallFollowSpeed);
		// follow obstacle while theta isn't within 5 degrees of the angle needed to reach destination
		while (Math.abs( nav.deltaAngle(nav.destAngle()))>4 || !withDest) {
			leftDistance = odo.getLeftSensorDist();
			try {Thread.sleep(15);} catch (Exception e) {}
			farHigh = (Math.pow((odo.getLeftSensorDist()/robot.followerSideDist),1.4));
			closeHigh =(Math.pow((robot.followerSideDist/odo.getLeftSensorDist()),1.1));

			// checks for wall in front
			if (odo.getFrontSensorDist()<robot.minFrontWallDist){
				nav.rotateClockwise(90);
			}

			//too left
			else if (leftDistance < robot.followerSideDist) {
				nav.goForth();
				nav.leftMotor.setSpeed((int) (robot.speed*closeHigh));
				nav.rightMotor.setSpeed((int)(robot.speed/closeHigh));
			} 

			//too right
			else if (leftDistance > robot.followerSideDist && leftDistance <= robot.noWallDistance) {
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed*farHigh));
				nav.leftMotor.setSpeed((int)robot.speed);
			} 

			// no more wall
			else if (robot.odo.getLeftSensorDist()>robot.noWallDistance){
				nav.goForth();
				nav.rightMotor.setSpeed((int) (robot.speed*1.5));
				nav.leftMotor.setSpeed((int)(robot.speed*0.6));
			}

		}
		robot.odo.usCleft.stopUS();
		nav.setAccSp(robot.acc, robot.speed);
	}
	/**
	 * Makes sure there is no wall at  distance by adding to the filter
	 * We can't afford to mess up thinking there is n wall when there is one 
	 * @param dist
	 * @return
	 */
	private boolean noWall(double dist){
		int counter = 0;
		for (int i = 0; i<2; i++){
			//try {Thread.sleep(5);} catch (Exception e) {}
			if(dist<odo.getLeftSensorDist()){
				counter++;
			}
		}
		if (counter > 1){ 
			return true;
		}
		else {
			return false;
		}

	}

}