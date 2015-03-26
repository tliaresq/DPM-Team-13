import lejos.nxt.LCD;
import lejos.nxt.Sound;
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
		nav.rotateClockwise(90);
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
		nav.travelTo(x, y, true);
	}
	public void usTest(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.usCfront.restartUS();
		robot.odo.usCleft.restartUS();
		robot.odo.usCright.restartUS();
		nav.qBreak(500);
		robot.odo.usCfront.stopUS();
		robot.odo.usCleft.stopUS();
		robot.odo.usCright.stopUS();
		nav.qBreak(500);
		robot.odo.usCfront.restartUS();
		robot.odo.usCleft.restartUS();
		robot.odo.usCright.restartUS();
		
	}
	public void lsTest(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.lsC.restartLS();
		nav.qBreak(500);
		robot.odo.lsC.stopLS();
		nav.qBreak(500);
		robot.odo.lsC.restartLS();
		try {Thread.sleep(300);} catch (Exception e) {}
		Sound.beep();
	}
	public void odoCorrectTest(){
		robot.odo.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		robot.odo.correctionOn();
		nav.travelTo(61, 61, true);
		nav.qBreak(15000);
		nav.travelTo(0, 0, true);
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

	



}
