
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
		odo.usCleft.restartUS();
		while(odo.usCfront.sensorDist()<30){
			nav.spinClockWise();
		}
		while(odo.usCfront.sensorDist()>30){
			nav.spinClockWise();
		}
		nav.rotateClockwise(-8);
		odo.lsM.start();
		odo.lsR.start();
		nav.goForth();
		if(crossLine()==true){
			while(!(odo.isLineM()==true && odo.isLineR()==true)){
				nav.spinCounterClockWise();
				while(crossLine()!=false){
					try { Thread.sleep(10); } catch (Exception e) {}
				}
				nav.goForth();
				while(crossLine()!=true){
					try { Thread.sleep(10); } catch (Exception e) {}
				}
			}
		}
		else{
			while(!(odo.isLineM()==true && odo.isLineR()==true)){
				nav.spinClockWise();
				while(crossLine()!=true){
					try { Thread.sleep(10); } catch (Exception e) {}
				}
				nav.goForth();
				while(crossLine()!=false){
					try { Thread.sleep(10); } catch (Exception e) {}
				}
			}
		}
		
		odo.setTheta(0);
		odo.setX(-robot.lsDist);
		nav.rotateClockwise(-90);
		nav.goForth();
		crossLine();
		nav.travelTo(-robot.lsDist, -robot.lsDist, false, false);
		nav.pointTo(180);
		if(odo.getFrontSensorDist()>25){
			odo.setX(odo.getX()+30.48);
		}
		nav.pointTo(270);
		if(odo.getFrontSensorDist()>25){
			odo.setY(odo.getY()+30.48);
		}
		nav.stopMotors();
		odo.setY(-robot.lsDist);
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(500);
		nav.travelToRelocalizeCross(2, 2);
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
		crossLine();
		nav.travelDist(robot.lsDist);
		odo.setY(y);
		nav.spinCounterClockWise();
		crossLine();
		odo.setTheta(180);
		nav.rotateClockwise(90);
		nav.travelDist(2);
		nav.rotateClockwise(90);
		nav.goForth();
		crossLine();
		nav.travelDist(robot.lsDist);
		odo.setX(x);
		//odo.lsC.stopLS();;
		nav.setAccSp(robot.acc, robot.speed);
	}
	

	/**
	 * Keeps the thread running until a line is detected
	 * @return
	 */
	private boolean crossLine() {
		boolean middle = false;
		boolean right = false;
		while (middle == false && right == false){
			try { Thread.sleep(10); } catch (Exception e) {}
			middle = odo.isLineM();
			right = odo.isLineR();
		}
		if (right = true){
			return true;
		}
		else{
			return false;
		}
	}
	

}