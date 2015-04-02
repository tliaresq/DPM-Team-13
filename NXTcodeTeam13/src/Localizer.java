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


	public void alphaLocalize(){
		odo.lsM.start();
		odo.usCfront.start();
		odo.usCfront.restartUS();

		try {Thread.sleep(50);} catch (Exception e) {}
		nav.setAccSp(1000, 250);
		nav.spinClockWise();
		while(!crossLine()){
			Sound.buzz();
			nav.pointTo(findMinDist());
			nav.travelDist(odo.getFrontSensorDist()-25);
			nav.spinClockWise();
		}
		nav.stopMotors();
		nav.setAccSp(2000, 300);
		nav.travelDist(robot.lsDist);
		nav.qBreak(1000);
		nav.pointTo(findCorrectLine());
		nav.setAccSp(2000, 300);
		
		double wallA = odo.getFrontSensorDist();
		if (wallA > 50){
			nav.rotateClockwise(180);
			wallA = odo.getFrontSensorDist();
		}
		Sound.buzz();
		Sound.beep();
		nav.rotateClockwise(90);
		boolean firstTry = true;
		double wallB = odo.getFrontSensorDist();
		if (wallB>50){
			nav.rotateClockwise(180);
			firstTry = false;
			wallB = odo.getFrontSensorDist();
		}

		Sound.buzz();
		Sound.beep();
		if (!firstTry){
			odo.setTheta(270);
			odo.setX(wallA-23);
			odo.setY(wallB-23);
		}
		else{
			odo.setTheta(180);
			odo.setX(wallB-23);
			odo.setY(wallA-23);

		}
		nav.setAccSp(2000, 300);
		lineLocalizeNE();
		Sound.buzz();
		nav.qBreak(6000);
	}
	
	private double findMinDist(){
		while(odo.getFrontSensorDist()<45){
			nav.spinClockWise();
			try {Thread.sleep(40);} catch (Exception e) {}

		}
		while(odo.getFrontSensorDist()>40){
			nav.spinClockWise();
			try {Thread.sleep(20);} catch (Exception e) {}
		}

		int counter = 0 ;
		double minDistAngle = 0;
		double dist;
		double minDist = Integer.MAX_VALUE;
		nav.setAccSp(2000,300);

		nav.spinClockWise();
		while(counter<13){
			//dist = odo.getFrontSensorDist();
			dist = odo.getFrontSensorDist();
			minDist = Math.min(minDist, dist);
			if (minDist == dist){
				minDistAngle = odo.getTheta();
			}	
			if (odo.getFrontSensorDist()>50){
				counter ++;
			}
			else{
				counter =0;
			}
			try { Thread.sleep(10); } catch (Exception e) {}

		}
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(500);
		nav.setAccSp(2000, 300);

		return minDistAngle+2;

	}


	private double findCorrectLine(){
		double[] linePos = new double[4];
		nav.setAccSp(2000, 200);
		for(int i=0 ; i< linePos.length; i++){
			nav.spinCounterClockWise();
			crossLine();
			Sound.beep();
			linePos[i] = odo.getTheta();
			if(angleDiff(linePos[0],linePos[1])>165){
				return linePos[1];
			}
			if(angleDiff(linePos[0],linePos[2])>165){
				return linePos[2];
			}
			else if (angleDiff(linePos[1],linePos[3])>165){
				return linePos[3];
			}
			try { Thread.sleep(200); } catch (Exception e) {}


		}
		return 0;
	}
	private boolean crossLine() {
		double store = odo.getTheta();
		boolean isLine = false;
		try { Thread.sleep(200); } catch (Exception e) {}
		while (isLine==false && Math.abs(odo.getTheta()-store)>1){
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
	 * to work the robot needs to be pointing north
	 * @param x	line directly east of the light sensor
	 * @param y line directly north of the light sensor
	 */
	public void lineLocalize(double x, double y) {
		nav.setAccSp(9000, 100);
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
	
	public void lineLocalizeNE() {
		nav.travelTo(20, 20, false, false);
		nav.pointTo(270);
		nav.setAccSp(9000, 100);
		nav.goForth();
		nav.crossLine();
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
		nav.setAccSp(robot.acc, robot.speed);
	}
}