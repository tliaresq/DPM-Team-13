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
	double fd ;
	double ld ;
	double rd ;

	public Localizer( Navigate n, Robot r) {
		odo = r.odo;
		nav = n;
		robot = r;
	}
	/**
	 * Localizes completely when place anywhere in Zone 1
	 */
	public void updateDists(){
		fd = odo.getFrontSensorDist();
		ld = odo.getLeftSensorDist();
		rd = odo.getRightSensorDist();
	}

	public void alphaLocalize() {
		odo.usCleft.restartUS();
		odo.usCright.restartUS();
		odo.usCfront.restartUS();
		updateDists();
		try {Thread.sleep(20);} catch (Exception e) {}
		while(!(Math.abs(ld-rd)< 3.5 && rd<15 && ld< 15 && (fd>ld && fd>rd)))
		{
			nav.spinClockWise();
			while(!(rd<55 && ld<55 && (fd>ld && fd>rd) && (Math.abs(Math.sqrt(rd*rd+ld*ld)-fd))<10)){
				try {Thread.sleep(20);} catch (Exception e) {}
				updateDists();		
			}
			nav.stopMotors();
			
			if(fd>ld||fd>rd){
				Sound.beep();
				odo.setTheta(135);
			}
			nav.pointTo(90+Math.toDegrees(Math.atan((ld-7)/(rd-7))));
			nav.goForth();
			updateDists();
			while(fd< 90 && !(rd<8 || ld< 8 || fd< 12)){
			//while(fd< 90 && !( fd< 10)){
						try {Thread.sleep(20);} catch (Exception e) {}
				updateDists();
			}
			nav.stopMotors();
			try {Thread.sleep(50);} catch (Exception e) {}
			updateDists();
			try {Thread.sleep(50);} catch (Exception e) {}
		}
		odo.usCleft.stopUS();
		odo.usCright.stopUS();
		odo.usCfront.stopUS();
//		qBreak(1000);
//		Sound.beep();
		nav.rotateClockwise(135);

		lineLocalize(0,0);
	}

	
	public void beta()
	{
		boolean loop = true;
		while(loop)
		{
				nav.spinClockWise();
				while(!(rd<55 && ld<55 && fd>ld && fd>rd && (Math.abs(Math.sqrt(rd*rd+ld*ld)-fd))<5)){
					try {Thread.sleep(20);} catch (Exception e) {}
					updateDists();
				}
				nav.stopMotors();
				
				odo.setTheta(Math.toDegrees(Math.tan(ld/rd))+45);
				try {Thread.sleep(20);} catch (Exception e) {}
				
				nav.pointTo(135);
				try {Thread.sleep(1000);} catch (Exception e) {}
				
				while(rd>20 || ld>20)
				{
					nav.goForth();
					updateDists();
					try {Thread.sleep(20);} catch (Exception e) {}
				}
				nav.pointTo(0.0);
		}
		
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
	private void crossLine() {
		while (odo.getLSState()==false){
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
	

	
}
