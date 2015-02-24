import lejos.nxt.*;

public class Main{


	static Exit exit;
	static Robot robot;
	static Odometer odometer;
	static Navigate nav;
	static LauncherController ballistics;
	static USLocalizer usl;
	static LightLocalizer lsl;
	
	public USController usLeft, usRight;
	public LSController ls;


	public static void main(String[] args) {

		
		exit = new Exit();								// Listens to button to exit program
		
		
		robot = new Robot();	// All the hardware Data
		
		odometer = new Odometer(robot);					// Handles all data relative to location
		nav = new Navigate(robot, odometer);			// Manages driving nxt to a given location
		ballistics = new LauncherController(robot);		
		usl = new USLocalizer(odometer, nav, robot, USLocalizer.LocalizationType.RISING_EDGE);// perform the ultrasonic localization
		lsl = new LightLocalizer(odometer, nav, robot);	// perform the light sensor localization

		exit.start();
		
		
		
		
		

		//MAIN MENU
		LCD.clear();
		LCD.drawString("Enter = full Demo", 0, 0);
		LCD.drawString("left = Ballistics menu", 0, 1);
		LCD.drawString("right = Nav/localize", 0, 2);
		
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
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
	}


	public static void mainEnter(){
		//	============================================	
		//		do whatever
		//	============================================
		odometer.start();
		nav.rotateClockwise(360);
		try {
			Thread.sleep(2000);
		} 
		catch (Exception e) {
		}
		
		nav.travelDist(60);
		}




	public static void mainLeft(){
		int option = 0;
		LCD.clear();
		LCD.drawString("Enter = shoot forever", 0, 0);
		LCD.drawString("left = shoot 1", 0, 1);
		LCD.drawString("right = Nav/localize", 0, 2);
		while (option == 0) {
			option = Button.waitForAnyPress();
		}
		switch (option) {
		case Button.ID_LEFT:
			ballistics.shoot();
			break;
		case Button.ID_RIGHT:
			ballistics.shoot(6);
			break;

		case Button.ID_ENTER:
			while(true){ballistics.shoot();}

		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}


	}



	public static void mainRight(){
		String[] option = {"shoot x1","shoot x2","goto (60;60)", "wall Localize", "line localize"};
		boolean display = true;

		while(display){
			int bPressed = 0;
			LCD.clear();
			LCD.drawString(option[0], 0, 0);
			LCD.drawString(option[1], 0, 1);
			LCD.drawString(option[2], 0, 2);
			LCD.drawString(option[3], 0, 3);
			LCD.drawString(option[4], 0, 4);
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


	public static void fetchOption(String option){
		switch (option){
		case "shoot x1":
			ballistics.shoot();
			break;

		case "wall Localize":
			odometer.start();
			usl = new USLocalizer(odometer, nav, robot, USLocalizer.LocalizationType.FALLING_EDGE);
			usl.doLocalization();
			break;
			
		case "goto (60;60)":
			odometer.start();
			nav.travelTo(60, 60);

			break;

		case "line localize":
			odometer.start();
			lsl.doLocalization();
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

}






