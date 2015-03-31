
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
		
		odo.usCfront.restartUS();
		odo.setTheta(0);
		nav.setAccSp(2000, 150);
		while(odo.getFrontSensorDist()<100){
		nav.spinClockWise();
		}
		while(odo.getFrontSensorDist()>100){
			nav.spinClockWise();
			}
		
		int counter = 0 ;
		double minDistAngle = 0;
		
		double dist;
		double minDist = Integer.MAX_VALUE;
		nav.spinClockWise();
		while(counter<50){
			dist = odo.getFrontSensorDist();
			minDist = Math.min(minDist, dist);
			if (minDist == dist){
				minDistAngle = odo.getTheta();
			}	
			if (odo.getFrontSensorDist()>250){
				counter ++;
			}
			else{
				counter =0;
			}
			try { Thread.sleep(10); } catch (Exception e) {}

		}
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(500);
		nav.pointTo(minDistAngle);
		nav.qBreak(500);nav.qBreak(500);
		nav.pointTo(minDistAngle - 90);
		if(odo.getFrontSensorDist()<60){
			odo.setTheta(180);
			odo.setX(odo.getFrontSensorDist()-20);
			odo.setY(minDist-20);
		}
		else{
			odo.setTheta(90);
			nav.pointTo(270);
			odo.setX(minDist-20);
			odo.setY(odo.getFrontSensorDist()-20);
		}
		
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(500);
		odo.lsM.start();
		nav.travelToRelocalizeCross(0, 0,false);
		nav.qBreak(60000);	
	}
	
	
	
	/**
	 * to work the robot needs to be pointing north
	 * @param x	line directly east of the light sensor
	 * @param y line directly north of the light sensor
	 */
	public void lineLocalize(double x, double y) {
		nav.setAccSp(9000, 100);
		//odo.lsC.restartLS();;
		try {Thread.sleep(100);} catch (Exception e) {}

		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setY(y);
		nav.spinCounterClockWise();
		nav.crossLine();
		odo.setTheta(180);
		nav.rotateClockwise(90);
		nav.travelDist(2);
		nav.rotateClockwise(90);
		nav.goForth();
		nav.crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(x);
		//odo.lsC.stopLS();;
		nav.setAccSp(robot.acc, robot.speed);
	}
	


	

}