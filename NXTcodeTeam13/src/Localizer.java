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
	public double[] linePos = new double[4];

	public Localizer( Navigate n, Robot r) {
		odo = r.odo;
		nav = n;
		robot = r;
	}



	/**
	 * Localizes independently of any odometer variables
	 * 
	 * @param origin if true localization should be at the starting area, else it should be at the shooting area.
	 */
	public void alphaLocalize(boolean origin){
		//correction has to be stopped not to interfeer with the calculation by implementing jumps in theta for example
		odo.correction.stop();
		try {Thread.sleep(100);} catch (Exception e) {}
		//us left sensor has to be stopped to make sure the front distance detected does not catch any interfeerance.(safety measure but it shouldn't happen anyways)
		odo.usCleft.stopUS();
		//resetting all that needs to be innitialized for a propper localization
		odo.usCfront.restartUS();
		robot.speed=300;
		robot.acc=2000;
		try {Thread.sleep(50);} catch (Exception e) {}
		nav.setAccSp(robot.acc, robot.speed);
		//tries to find a line within its radius
		nav.spinClockWise();
		while(!crossLine()){
			//if it wasn't able to find a line, it will point to the wall from which it is closest
			Sound.buzz();
			nav.pointTo(findMinDist());
			nav.qBreak(1000);
			//it travels to a distance from the wall such that a line should be in its radius range.
			nav.travelDist(odo.getFrontSensorDist()-25);
			nav.spinClockWise();
		}
		//the robots sensor should be placed on a line at this point
		nav.stopMotors();
		//places the center of rotation of the robot on the line.
		nav.travelDist(robot.lsDist);
		nav.qBreak(1000);
		//places the light sensor on the line on which the center of rotation of the robot is.
		nav.pointTo(findCorrectLine());
		nav.setAccSp(robot.acc, robot.speed);
		//checks the placement of the walls relative to the robot to set theta accordingly.
		double wallA = odo.getFrontSensorDist();
		if (wallA > 45){
			nav.rotateClockwise(180);
			wallA = odo.getFrontSensorDist();
		}
		Sound.buzz();
		Sound.beep();
		nav.rotateClockwise(90);
		boolean firstTry = true;
		double wallB = odo.getFrontSensorDist();
		if (wallB>45){
			nav.rotateClockwise(180);
			firstTry = false;
			wallB = odo.getFrontSensorDist();
		}

		Sound.buzz();
		Sound.beep();
		// sets theta x and y depending on the distances from the walls the robot read.
		if(origin== true)
		{
			if (!firstTry){
				odo.setTheta(270);
				odo.setX(wallA-30.48+robot.lsDist);
				odo.setY(wallB-30.48+robot.lsDist);
			}
			else{
				odo.setTheta(180);
				odo.setX(wallB-30.48+robot.lsDist);
				odo.setY(wallA-30.48+robot.lsDist);
			}
			lineLocalizeNE();
		}
		else
		{
			if (!firstTry){
				odo.setTheta(90);
				odo.setX(335.28-wallA-robot.lsDist);
				odo.setY(335.28-wallB-robot.lsDist);
			}
			else{
				odo.setTheta(0);
				odo.setX(335.28-wallB-robot.lsDist);
				odo.setY(335.28-wallA-robot.lsDist);
			}
			// replaces itself to relocalize much more accurately 
			omegalineLocalizeNE();
		}
	}

	/**
	 * rotates 360 degrees and returns the angle at which the us sensor found the minimum distance
	 * @return
	 */
	private double findMinDist(){
		double minDistAngle = 0;
		double dist;
		double minDist = 256;
		nav.spinClockWise();
		double store = odo.getTheta();
		try { Thread.sleep(200); } catch (Exception e) {}
		while(Math.abs(odo.getTheta()-store)>3){
			dist = odo.getFrontSensorDist();
			if(dist<minDist)
			{
				minDist=dist;
				minDistAngle = odo.getTheta();
			}
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		return minDistAngle;
	}




	/**
	 * when the center of rotation of the robot is placed on a line, rotates the robot and returns the angle at which the line  the robot is on points to.
	 * @return
	 */
	private double findCorrectLine(){
		//double[] linePos = new double[4];
		nav.setAccSp(2000, 250);
		for(int i=0 ; i< linePos.length; i++){
			nav.spinCounterClockWise();
			crossLine();
			Sound.beep();
			linePos[i] = odo.getTheta();
			if(angleDiff(linePos[0],linePos[1])>170 && i>=1){
				return linePos[1];
			}
			else if(angleDiff(linePos[0],linePos[2])>170 && i>=2){
				return linePos[2];
			}
			else if (angleDiff(linePos[1],linePos[3])>170 && i>=3){
				return linePos[3];
			}

			try { Thread.sleep(200); } catch (Exception e) {}
		}
		return linePos[3];
	}
	/**
	 * lets the robot run (or do whatever it is already doing) until it crosses a line
	 * @return
	 */
	private boolean crossLine() {
		double store = odo.getTheta();
		boolean isLine = false;
		try { Thread.sleep(200); } catch (Exception e) {}
		while (isLine==false && Math.abs(odo.getTheta()-store)>3){
			isLine=odo.isLineM();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		if (isLine){
			return true;
		}
		else{
			return false;
		}

	}
	/**
	 * returns a the minimum absolute value of the angle between 2 angles
	 * @param a
	 * @param b
	 * @return
	 */
	private double angleDiff(double a,double b) {
		a = (a + 360) % 360;
		if (b > 180) {
			b -= 360;
		}
		if (a > 180) {
			a -= 360;
		}
		double deltaAngle = a - b;

		if (deltaAngle > 180) {
			deltaAngle -= 360;
		}
		if (deltaAngle < -180) {
			deltaAngle += 360;
		}
		return (Math.abs(deltaAngle));
	}

	/**
	 * this localizes around an intersection
	 * to work the robot needs to be pointing north
	 * @param x	line directly east of the light sensor
	 * @param y line directly north of the light sensor
	 */
	public void lineLocalize(double x, double y) {
		nav.setAccSp(9000, 150);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setY(y);
		nav.spinClockWise();
		nav.crossLine();
		odo.setTheta(0);
		nav.rotateClockwise(90);
		nav.travelDist(4);
		nav.rotateClockwise(-90);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(x);
		nav.setAccSp(robot.acc, robot.speed);
	}

	/**
	 * this is a precision localization at the origin
	 */
	public void lineLocalizeNE() {
		nav.travelTo(15, 15, false, false);
		nav.pointTo(270);
		nav.setAccSp(9000, 150);
		nav.goForth();
		if (!nav.crossLine()){
			alphaLocalize(true);
		}
		nav.travelDist(robot.lsDist);
		odo.setY(0);
		nav.spinClockWise();
		nav.crossLine();
		odo.setTheta(180);
		nav.rotateClockwise(90);
		nav.travelDist(4);
		nav.rotateClockwise(-90);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(0);
		double check1 = odo.getFrontSensorDist();
		nav.rotateClockwise(-90);
		double check2 = odo.getFrontSensorDist();
		if (Math.abs(check1-22)>7 || Math.abs(check2-26)>7){
			alphaLocalize(true);
		}
		nav.setAccSp(robot.acc, robot.speed);

	}
	/**
	 * this is a precision localization at the shooting area
	 */
	public void omegalineLocalizeNE() {
		nav.travelTo(290, 290, false, false);
		nav.pointTo(90);
		nav.setAccSp(9000, 150);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setY(304.8);
		nav.spinClockWise();
		nav.crossLine();
		nav.qBreak(1000);
		odo.setTheta(0);
		nav.rotateClockwise(90);
		nav.travelDist(4);
		nav.rotateClockwise(-90);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(304.8);
		nav.setAccSp(robot.acc, robot.speed);
	}
}