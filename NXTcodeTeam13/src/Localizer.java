import lejos.nxt.Sound;



/**
 * Implements Localisation methods to localize at specific times
 * Different localization methods may be used depending on how inaccurate is the odometer at the time of localization and the known environment
 * @author Cedric
 *
 */
public class Localizer {
	private Odometer odo;
	private Navigate nav;
	private Robot robot;

	public Localizer( Navigate n, Robot r) {
		odo = r.odo;
		nav = n;
		robot = r;
	}
	/**
	 * Localizes completely when place anywhere in Zone 1
	 */
	public void alphaLocalize() {
		odo.usCleft.restartUS();
		odo.usCright.restartUS();
		odo.usCfront.restartUS();
		double fd = odo.getFrontSensorDist();
		double ld = odo.getLeftSensorDist();
		double rd = odo.getRightSensorDist();
		try {Thread.sleep(20);} catch (Exception e) {}
		while(!(Math.abs(ld-rd)< 5 && rd<15 && ld< 15 && (fd>ld||fd>rd))){
			nav.spinClockWise();
			while(!(rd<60 && ld<60 &&(fd>ld||fd>rd))){
				try {Thread.sleep(20);} catch (Exception e) {}
				fd = odo.getFrontSensorDist();
				ld = odo.getLeftSensorDist();
				rd = odo.getRightSensorDist();
				
			}
			nav.stopMotors();
			odo.setTheta(135);
			odo.setX(0);
			odo.setY(0);
			nav.pointTo(90+Math.toDegrees(Math.atan((ld)/(rd))));
			nav.goForth();
			ld = odo.getLeftSensorDist();
			rd = odo.getRightSensorDist();
			fd = odo.getFrontSensorDist();
			while(!(rd<robot.followerSideDist && ld< robot.followerSideDist || fd< 9)){
				try {Thread.sleep(20);} catch (Exception e) {}
				ld = odo.getLeftSensorDist();
				rd = odo.getRightSensorDist();
				fd = odo.getFrontSensorDist();
			}
			nav.stopMotors();
			fd = odo.getFrontSensorDist();
			ld = odo.getLeftSensorDist();
			rd = odo.getRightSensorDist();
			Sound.beep();
		}
		qBreak(1000);
		Sound.beep();
		nav.rotateClockwise(135);
		
		lineLocalize();
	}
	/**
	 * Last step of localization.
	 */
	public void lineLocalize() {
		odo.lsC.restartLS();;
		try {Thread.sleep(1000);} catch (Exception e) {}
		nav.setAccSp(robot.acc, robot.speed);
		nav.goForth();
		crossLine();
		nav.travelDist(robot.lsDist);
		odo.setY(0);
		nav.spinCounterClockWise();
		crossLine();
		odo.setTheta(180);
		nav.rotateClockwise(90);
		nav.travelDist(1);
		nav.rotateClockwise(90);
		nav.goForth();
		crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(0);
		odo.lsC.restartLS();;
	}
	/**
	 * Keeps the thread running until a line is detected
	 * @return
	 */
	private void crossLine() {

		double black = odo.robot.black;
		while (odo.getSensorColor()> black ){

			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
	/**
	 * When placed on a line, the odometer rotates 360 which means the LS will cross 2 to 4 lines (4 if an intersection is in its radius)
	 * the method then calculates which one to place the light sensor on to be sure to have the same line as the one the robot is centered on.
	 * @return
	 */
	private double findCorrectLine(){
		double[] linePos = new double[4];
		for(int i=0 ; i< linePos.length; i++){
			nav.spinCounterClockWise();
			crossLine();
			linePos[i] = odo.getTheta();
			nav.rotateClockwise(-5);
		}
		nav.stopMotors();
		if(Math.abs(linePos[0]-linePos[2])<3){
			return linePos[0];
		}
		for(int i=0 ; i< linePos.length; i++){
			linePos[i] +=360;
		}
		if (Math.abs(180-Math.abs(linePos[0]-linePos[2]))<3){
			if (Math.abs(linePos[0]-linePos[1])>90 || Math.abs(linePos[0]-linePos[3])>90){
				return linePos[0];
			}
			else{
				return linePos[2];
			}
		}
		else{
			if (Math.abs(linePos[1]-linePos[0])>90 || Math.abs(linePos[1]-linePos[2])>90){
				return linePos[1];
			}
			else{
				return linePos[3];
			}
		}
	}
	/**
	 * stops the Robot for a short momment for test purpouses and so on 
	 */
	private void qBreak(int time){
		nav.stopMotors();
		try {Thread.sleep(time);} catch (Exception e) {}
		nav.setAccSp(robot.acc, robot.speed);
	}
}
