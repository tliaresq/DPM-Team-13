


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
	private double fd ;
	private double ld ;
	private double rd ;

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
	public boolean goodOrientation(){
		updateDists();
		if (Math.abs(ld-rd)< 5 && rd<16 && ld< 16 && (fd>ld && fd>rd)){
			return true;
		}
		else {
			return false;
		}
	}

	public void wallLocalize() {
		odo.usCleft.restartUS();
		odo.usCright.restartUS();
		odo.usCfront.restartUS();
		try {Thread.sleep(20);} catch (Exception e) {}
		while(!goodOrientation()){
			nav.spinClockWise();
			while(!(rd<55 && ld<55 && (fd>=ld && fd>=rd) )){
				try {Thread.sleep(20);} catch (Exception e) {}
				updateDists();		
			}			
				odo.setTheta(135);
			if(goodOrientation()){ break; }
			nav.pointTo(90+Math.toDegrees(Math.atan2(ld-10, rd-10)));
			nav.travelDist(3);
			if(goodOrientation()){ break; }
			nav.goForth();
			updateDists();
			while(fd< 90 && !(rd<3 || ld< 3 || fd< 12)){
				try {Thread.sleep(20);} catch (Exception e) {}
				updateDists();
			}
			updateDists();
		}
		odo.usCleft.stopUS();
		odo.usCright.stopUS();
		odo.usCfront.stopUS();
		nav.rotateClockwise(135);
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
