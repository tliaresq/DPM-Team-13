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

		case "map1":			break;
		case "map2":			break;
		case "map3":			break;
		case "map4":			break;
		case "map5":			break;

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
		nav.travelTo(60, 90, true,true);
	}
	public void usTest(){
		robot.odo.start();
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
		try {Thread.sleep(50000);} catch (Exception e) {}

	}
	public void odoCorrectTest(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		//robot.odo.correctionOn();
		nav.travelTo(60, 60, true,true);
		nav.qBreak(15000);
		nav.travelTo(0, 0, true,true);
	}
	public void shoot999(){
		crossbow.shoot(999);
	}
	public void localize(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.localizer.alphaLocalize();
	}


	public void map1(){

	}
	public void map2(){

	}
	public void map3(){

	}
	public void map4(){

	}
	public void map5(){

	}
	private void printMsg() {
		LCD.clear();
		LCD.drawString("ERROR", 0, 0);
		LCD.drawString("no such option", 0, 1);
	}


	public void demoMain(){
		robot.speed = 200;
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.localizer.alphaLocalize();
		robot.odo.lsM.start();
		nav.qBreak(100);
		nav.localizer.lineLocalize(0, 0);
		nav.qBreak(300); nav.qBreak(300); nav.qBreak(300);
		robot.speed = 200;
		nav.travelToRelocalizeCross(6,6,true);
		robot.speed = 200;
		nav.travelTo(6*30.48, 6*30.48, false, false);
		nav.pointTo(45);
		nav.stopMotors();
		nav.qBreak(300);nav.qBreak(300);nav.qBreak(300);
		crossbow.shoot(8);
		nav.qBreak(300);nav.qBreak(300);nav.qBreak(300);nav.qBreak(30000);	
	}

	public static void mapDemo()
	{
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}

		nav.localizer.alphaLocalize();
		robot.odo.lsM.start();
		nav.localizer.lineLocalize(0, 0);
		try {Thread.sleep(100);} catch (Exception e) {}
		nav.qBreak(500);
		nav.qBreak(500);
		nav.travelTo(-10, 1, false,true);
		nav.travelTo(-10, 168, false,true);
		nav.travelTo(46, 168, false,true);
		nav.travelTo(46, 198, false,true);
		nav.travelTo(138, 198, false,true);
		nav.travelTo(172.88, 167.88, true,true);
		nav.pointTo(90);
		nav.localizer.lineLocalize(182.88, 182.88);
		nav.pointTo(45.0);
		nav.qBreak(500);
		nav.qBreak(500);
		crossbow.shoot(6);
	}



}
