import lejos.nxt.LCD;
/**
 * Stores all the set of instructions to run specific tests
 * This class is mainly present to structure the code and give easy access
 * @author Cedric
 *
 */
public class Tests extends Main {








	public void runOption(String option){
		switch (option){
		case "360": do360();break;
		case "travel 61": travelDist(91);
		case "USfront test": usTest(); break;
		case "LS test":lsTest();break;
		case "odoCorrect test":odoCorrectTest();break;
		case "shoot 999": shoot999();break;
		case "travel to": travelToTest(61,61);break;
		case "localize": localize();break;
		case "opt test": printMsg();
		case "follow test": followerTest();break;


		default:
			printMsg();
			try {Thread.sleep(3000);} catch (Exception e) {}
			mainRight();
			break;
		}
	}


	private void followerTest() {
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.usCfront.restartUS();
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.goForth();
		while(robot.odo.getFrontSensorDist()>robot.minFrontWallDist){
			try {Thread.sleep(30);} catch (Exception e) {}
		}
		nav.rotateClockwise(90);
		nav.follower.follow(false);
		robot.odo.usCfront.stopUS();
	}

	public void do360(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.rotateClockwise(720);
		nav.stopMotors();
		//nav.pointTo(90);
	}
	public void travelDist(int dist){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.travelDist(dist);
		try {Thread.sleep(10000);} catch (Exception e) {}
		//		nav.rotateClockwise(180);
		//		nav.travelDist(dist);
		//		nav.rotateClockwise(180);
	}
	public void travelToTest(int x ,int y){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.travelTo(0, 300, false,false);
		try {Thread.sleep(100000);} catch (Exception e) {}
	}
	public void usTest(){
		robot.odo.start();

		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.usCfront.start();
		robot.odo.usCleft.start();
		try {Thread.sleep(100);} catch (Exception e) {}

		robot.odo.usCfront.restartUS();
		robot.odo.usCleft.restartUS();
		//robot.odo.usCright.restartUS();
		nav.qBreak(500);
		robot.odo.usCfront.stopUS();
		robot.odo.usCleft.stopUS();
		//robot.odo.usCright.stopUS();
		nav.qBreak(500);
		robot.odo.usCfront.restartUS();
		robot.odo.usCleft.restartUS();
		//robot.odo.usCright.restartUS();

	}
	public void lsTest(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.lsM.start();
		robot.odo.lsR.start();
		robot.odo.lsM.start();
		robot.odo.usCfront.start();
		try {Thread.sleep(50000);} catch (Exception e) {}

	}
	public void odoCorrectTest(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.lsM.start();
		robot.odo.lsR.start();
		robot.odo.usCfront.start();
		robot.odo.usCleft.start();
		robot.odo.correction.start();
		robot.odo.setX(15.0);
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.setY(15.0);
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.setTheta(90.0);
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.travelTo(15.0, 75.0, true,true);
		nav.qBreak(500);
		nav.travelTo(15.0, 15.0, true,true);
		nav.qBreak(500);
		nav.travelTo(75.0, 15.0, false,true);
		nav.qBreak(500);
		nav.travelTo(75.0, 75.0, false,true);
		nav.qBreak(500);
		nav.travelTo(60.96, 60.96, false,true);
		nav.pointTo(90.0);
		nav.qBreak(20000);
	}
	public void shoot999(){
		crossbow.shoot(100);
	}
	public void localize(){
		robot.odo.start();
		try {Thread.sleep(1000);} catch (Exception e) {}
		robot.odo.lsM.start();
		robot.odo.usCfront.start();
		nav.localizer.alphaLocalize(false);
	}

	private void printMsg() {
		LCD.clear();
		LCD.drawString("ERROR", 0, 0);
		LCD.drawString("no such option", 0, 1);
	}





}
