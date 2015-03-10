
public class Localizer {
	private Odometer odo;
	private Navigate nav;
	private Robot robot;
	public Localizer( Navigate n, Robot r) {
		odo = r.odometer;
		nav = n;
		robot = r;

	}
	public void Localize(){
		goToBtmLeftTile();
		lineLocalize();

	}

	public void lineLocalize() {
		try {Thread.sleep(1000);} catch (Exception e) {}
		odo.lsOn();
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
		odo.lsOff();
		

	}

	private void crossLine() {

		double black = odo.robot.black;
		while (odo.getSensorColor()> black ){
			
			try { Thread.sleep(10); } catch (Exception e) {}
		}

	}

	public void goToBtmLeftTile(){
		try {Thread.sleep(500);} catch (Exception e) {}
		odo.usOn();
		//try {Thread.sleep(500);} catch (Exception e) {}
		nav.setAccSp(9000, robot.speed);
		nav.spinClockWise();
		findNoWall();
		qBreak();
		nav.spinClockWise();
		findWall();
		qBreak();
		//the odometerhas found the wall parallel to the Y axis
		nav.travelDist(odo.getRightSensorDist()-robot.wallDist);
		qBreak();
		nav.spinClockWise();
		double temp1;
		double temp2;
		do{
			temp1 = odo.getRightSensorDist();
			try {Thread.sleep(300);} catch (Exception e) {}
			temp2 = odo.getRightSensorDist();
		}while(temp1>temp2);
		qBreak();
		nav.follower.followUntillWall();
		qBreak();
		nav.travelDist(odo.getRightSensorDist()-5);
		nav.travelDist(odo.getRightSensorDist()-5);
		nav.travelDist(odo.getRightSensorDist()-5);
		nav.rotateClockwise(90);	
		odo.usOff();
	}
	
	public void qBreak(){
		nav.stopMotors();
		try {Thread.sleep(500);} catch (Exception e) {}
		nav.setAccSp(robot.acc, robot.speed);
	}

	private void findWall() {
		while (odo.getRightSensorDist()> robot.findWallDist);{
			try {Thread.sleep(10);} catch (Exception e) {}
		} 

	}

	private void findNoWall() {

		while (odo.getRightSensorDist()< robot.findWallDist);{
			try {Thread.sleep(10);} catch (Exception e) {}
		} 
	}


}
