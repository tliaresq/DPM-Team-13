import lejos.nxt.*;

public class Main{


	static Exit exit;							// Listens to escape button to exit program at any time
	static Robot robot;							// All the hardware Data
	static Navigate nav;						// Manages driving nxt to a given location
	//static LauncherController ballistics; 		//Handles launching PingPong Balls
	static Crossbow crossbow;
	static Localizer localizer;
	
/**
 * Starting point of program
 * @param args
 */
	public static void main(String[] args) {
		exit = new Exit();									
		robot = new Robot();					
		nav = new Navigate(robot, robot.odo);			
		//ballistics = new LauncherController(robot);	
		crossbow = new Crossbow(robot);
		localizer = new Localizer(nav, robot);
		exit.start();
		printMainMenu();
	}
/**
 * fetches the option requested in the list menu
 * @param option
 */
	public static void fetchOption(String option){
		switch (option){
		case "betaCorrect Test":
			robot.odo.start();
			try {Thread.sleep(100);} catch (Exception e) {}
			robot.odo.betaCorrect.restart();
			try {Thread.sleep(100);} catch (Exception e) {}
			//================================
			// BUILD YOUR ITINIRARY HERE
			//================================
			
			nav.travelTo(-60, 180, false);
			nav.travelTo(0, 0, false);
			break;
		case "odoCorrect test":
			robot.odo.start();

			try {Thread.sleep(100);} catch (Exception e) {}
			
			robot.odo.correctionOn();
			try {Thread.sleep(100);} catch (Exception e) {}
			nav.travelTo(-60, 180, false);
			nav.travelTo(0, 0, false);
			break;
			
		case "shoot x1":
			crossbow.shoot(1);
			break;

		case "shoot x6":
			crossbow.shoot(6);
			break;

		case "goto (0;60) with Follower":
			robot.odo.start();
			nav.travelTo(0, 60, true);
			break;

		case "wall Localize":
			robot.odo.start();
			localizer.goToBtmLeftTile();
			break;

		case "line localize":
			robot.odo.start();
			localizer.lineLocalize();
			break;
			
		case "rotate Dist test":
			nav.setAccSp(robot.acc, robot.speed);
			nav.rotateClockwise(360);
			try {Thread.sleep(3000);} catch (Exception e) {}
			nav.travelDist(60);
			break;

		case "map1":
			//fill in with itinerary corresponding to maps
			break;
		case "map2":
			break;
		case "map3":
			break;
		case "map4":
			break;
		case "map5":
			break;

		default: 
			LCD.clear();
			LCD.drawString("error", 0, 0);
			LCD.drawString("option does not exist", 0, 1);
			try {
				Thread.sleep(3000);
			} 
			catch (Exception e) {
			}
			mainRight();
			break;
		}
	}
	
/**
 * handles the LIST MENU 
 */
	public static void mainRight(){
		String[] option = { "betaCorrect Test","line localize","wall Localize","odoCorrect test","shoot x1","shoot x6","goto (0;60) with Follower", "rotate Dist test", "map1", "map2", "map3", "map4", "map5"};//list of options

		boolean display = true;

		while(display){
			int bPressed = 0;
			LCD.clear();

			for (int i = 0; i< 8; i++ ){
				LCD.drawString(option[i], 0, i);
			}


			while (bPressed == 0) {
				bPressed = Button.waitForAnyPress();
			}
			
			switch (bPressed) {
			
			case Button.ID_LEFT:
				String temp = option[option.length-1];
				for (int i = 0 ; i < option.length-1; i++){
					option[i+1] = option[i];
				}
				option[0] = temp;
				break;
				
			case Button.ID_RIGHT:
				String temp2 = option[0];
				for (int i = 0 ; i < option.length-1; i++){
					option[i] = option[i+1];
				}
				option[option.length-1] = temp2;
				break;

			case Button.ID_ENTER:
				display = false;
				fetchOption(option[0]);
				break;

			default:
				System.out.println("Error - invalid button");
				System.exit(-1);
				break;
			}
		}		
	}

/**
 * does whatever Test Or Stuff
 */
	public static void mainEnter(){
		//	============================================	
		//		do whatever Test Or Stuff
		//	============================================

		
		
	}

/**
 * do whatever Test Or Stuff
 */
	public static void mainLeft(){
		//	============================================	
		//		do whatever Test Or Stuff
		//	============================================
		robot.odo.start();

		nav.rotateClockwise(360);
		try {
			Thread.sleep(2000);
		} 
		catch (Exception e) {
		}
		nav.travelDist(60);
	}
	
/**
 * prints main menu
 */
	public static void printMainMenu(){
		LCD.clear();
		LCD.drawString("Enter = Full Demo", 0, 0);
		LCD.drawString("left = Some Test", 0, 1);
		LCD.drawString("right = list of options", 0, 2);

		int option = 0;

		while (option == 0) {
			option = Button.waitForAnyPress();
		}
		switch (option) {
		case Button.ID_LEFT:
			mainLeft();
			break;

		case Button.ID_RIGHT:
			mainRight();
			break;

		case Button.ID_ENTER:
			mainEnter();
			break;

		default:
			LCD.clear();
			LCD.drawString("Error - invalid button", 0, 0);
			break;
		}
	}
}






