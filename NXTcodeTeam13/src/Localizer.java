
public class Localizer {
	private Odometer odo;
	private Navigate nav;
	private Robot robot;
	public Localizer( Navigate n, Robot r) {
		odo = r.odo;
		nav = n;
		robot = r;

	}
	public void Localize(){
		goToBtmLeftTile();
		lineLocalize();

	}

	public void betaLocalize(){

		odo.setTheta(1);
		boolean success = false;
		while(!success){
			while(odo.getTheta()>1 || success){
				nav.spinCounterClockWise();
				success=crossLine();
			}
			if(!success){
				nav.rotateClockwise(45);
				nav.travelDist(8);
			}
		}
		nav.stopMotors();
		nav.travelDist(robot.lsDist);
		double linePos = findCorrectLine();
		nav.pointTo(linePos);
		odo.usCfront.restartUS();
		try {Thread.sleep(500);} catch (Exception e) {}
		if(odo.getFrontSensorDist()>120){
			nav.rotateClockwise(180);
		}
		nav.goForth();
		while(odo.getFrontSensorDist()>8){
			try {Thread.sleep(50);} catch (Exception e) {}
		}
		nav.travelDist(odo.getFrontSensorDist()-5);
		nav.stopMotors();
		nav.rotateClockwise(90);
		if(odo.getFrontSensorDist()>120){
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
		nav.spinCounterClockWise();
		for(int i=0 ; i< linePos.length; i++){
			crossLine();
			linePos[i] = odo.getTheta();
			nav.rotateClockwise(-4);
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

	public void lineLocalize() {
		try {Thread.sleep(1000);} catch (Exception e) {}
		odo.lsC1.restartLS();;
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
		odo.lsC1.restartLS();;


	}

	private boolean crossLine() {

		double black = odo.robot.black;
		while (odo.getSensorColor()> black ){

			try { Thread.sleep(10); } catch (Exception e) {}
		}
		return true;
	}

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
		nav.travelDist(odo.getFrontSensorDist()-robot.wallDist);
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
