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

		lineLocalize();
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
	 * Last step of localization.
	 */
	public void lineLocalize() {
		nav.setAccSp(9000, 100);
		odo.lsC.restartLS();;
		try {Thread.sleep(1000);} catch (Exception e) {}

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
		odo.lsC.stopLS();;
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

	
}
