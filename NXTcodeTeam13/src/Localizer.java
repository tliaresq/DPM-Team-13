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

	public void alphaLocalize() {
		odo.usCleft.restartUS();
		odo.usCright.restartUS();
		odo.usCfront.restartUS();
		qBreak();Sound.beep();
		double fd = odo.getFrontSensorDist();
		double ld = odo.getLeftSensorDist();
		double rd = odo.getRightSensorDist();
		while(!(Math.abs(fd-rd)<3 && rd<15 && ld< 15)){
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
			nav.pointTo(90+Math.atan((ld)/(rd)));
			nav.goForth();
			fd = odo.getFrontSensorDist();
			while(fd>10){
				try {Thread.sleep(20);} catch (Exception e) {}
				fd = odo.getFrontSensorDist();
			}
			nav.stopMotors();
			fd = odo.getFrontSensorDist();
			ld = odo.getLeftSensorDist();
			rd = odo.getRightSensorDist();

			Sound.beep();qBreak();
		}
		nav.rotateClockwise(135);
		Sound.beep();qBreak();Sound.beep();qBreak();Sound.beep();qBreak();Sound.beep();qBreak();
		lineLocalize();
		

	}








	public void Localize(){
		goToBtmLeftTile();
		lineLocalize();

	}

	/**
	 * Most efficient localization method.
	 * Still needs to be tested.
	 */
	public void betaLocalize(){

		odo.setTheta(1);
		//		boolean success = false;
		//		while(!success){
		//			while(odo.getTheta()<350 && !success){
		//				nav.spinCounterClockWise();
		//				success=crossLineOr360();
		//			}
		//			if(!success){
		//				nav.rotateClockwise(45);
		//				nav.travelDist(8);
		//				Sound.beep();
		//				qBreak();
		//			}
		//		}
		//		nav.stopMotors();


		odo.lsC.restartLS();
		qBreak();
		nav.setAccSp(robot.acc, robot.speed);
		nav.goForth();
		crossLine();
		//nav.stopMotors();


		nav.travelDist(robot.lsDist);
		double linePos = findCorrectLine();

		Sound.beep();
		qBreak();Sound.beep();
		qBreak();Sound.beep();
		qBreak();
		nav.setAccSp(robot.acc, robot.speed);

		nav.pointTo(linePos);

		odo.usCfront.restartUS();
		try {Thread.sleep(500);} catch (Exception e) {}
		if(odo.getFrontSensorDist()>200){
			nav.rotateClockwise(180);
		}
		nav.setAccSp(robot.acc, robot.speed);
		nav.goForth();
		while(odo.getFrontSensorDist()>10){
			try {Thread.sleep(50);} catch (Exception e) {}
		}
		nav.travelDist(odo.getFrontSensorDist()-7);
		nav.stopMotors();
		Sound.beep();
		qBreak();Sound.beep();
		qBreak();Sound.beep();
		qBreak();
		nav.setAccSp(robot.acc, robot.speed);

		nav.rotateClockwise(90);
		if(odo.getFrontSensorDist()>200){
			odo.setTheta(90);
			nav.rotateClockwise(180);
		}
		else{
			odo.setTheta(180);
		}
		nav.goForth();
		while(odo.getFrontSensorDist()>8){
			try {Thread.sleep(50);} catch (Exception e) {}
		}
		nav.travelDist(odo.getFrontSensorDist()-5);
		nav.pointTo(90);		
	}
	public double findCorrectLine(){
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
	 * Last step of localization.
	 */
	public void lineLocalize() {
		try {Thread.sleep(1000);} catch (Exception e) {}
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
	//	private boolean crossLineOr360() {
	//		odo.lsC1.restartLS();
	//		try { Thread.sleep(30); } catch (Exception e) {}
	//		double black = odo.robot.black;
	//
	//		while (odo.getTheta()<340 && odo.getSensorColor()> black ){
	//
	//			try { Thread.sleep(10); } catch (Exception e) {}
	//		}
	//		if ( odo.getSensorColor()> black){
	//		return true;
	//		}else{return false;}
	//	}
	/**
	 * First method for localization
	 * Does not work efficiently
	 */
	public void goToBtmLeftTile(){
		try {Thread.sleep(500);} catch (Exception e) {}
		odo.usCfront.restartUS();
		odo.usCleft.restartUS();
		//try {Thread.sleep(500);} catch (Exception e) {}
		nav.setAccSp(9000, robot.speed);
		nav.spinClockWise();
		findNoWall();
		qBreak();
		nav.spinClockWise();
		findWall();
		qBreak();
		//the odometerhas found the wall parallel to the Y axis
		nav.travelDist(odo.getFrontSensorDist()-robot.minFrontWallDist);
		qBreak();
		nav.spinClockWise();
		double temp1;
		double temp2;
		do{
			temp1 = odo.getFrontSensorDist();
			try {Thread.sleep(300);} catch (Exception e) {}
			temp2 = odo.getFrontSensorDist();
		}while(temp1>temp2);
		qBreak();
		nav.follower.followUntillWall();
		qBreak();
		nav.travelDist(odo.getFrontSensorDist()-5);
		nav.travelDist(odo.getFrontSensorDist()-5);
		nav.travelDist(odo.getFrontSensorDist()-5);
		nav.rotateClockwise(90);	
		odo.usCfront.restartUS();
		odo.usCleft.restartUS();
	}
	/**
	 * stops the Robot for a short momment for test purpouses and so on 
	 * 
	 */
	public void qBreak(){
		nav.stopMotors();
		try {Thread.sleep(500);} catch (Exception e) {}
		nav.setAccSp(robot.acc, robot.speed);
	}

	private void findWall() {
		while (odo.getFrontSensorDist()> robot.findWallDist);{
			try {Thread.sleep(10);} catch (Exception e) {}
		} 

	}

	private void findNoWall() {

		while (odo.getFrontSensorDist()< robot.findWallDist);{
			try {Thread.sleep(10);} catch (Exception e) {}
		} 
	}



}
