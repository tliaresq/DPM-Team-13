import lejos.nxt.*;
/**
 * Contains main method
 * Displays menu and options
 * Runs the Robot depending on selected option
 * @author Cedric
 *
 */
public class Main{

	static String[] optionList = {"travel to","error","360","travel 61", "USfront test","LS test", "odoCorrect test","shoot 999",  "localize","follow test"}; 
	//static Exit exit;							// Listens to escape button to exit program at any time
	static Robot robot;							// All the hardware Data
	static Navigate nav;						// Manages driving nxt to a given location
	static Crossbow crossbow;
	static Tests test = new Tests();
	static FinalRun finalrun = new FinalRun();




	/**
	 * Quick Access to Test Or Demo
	 */
	public static void mainEnter(){
		finalrun.finalRun(false);
	}




	/**
	 * Quick Access to Test Or Demo
	 */
	public static void mainLeft(){
		finalrun.finalMapRun(false);
	}
	
	/**
	 * Starting point of program
	 * Initializes instances of all needed objects
	 * @param args
	 */
	public static void main(String[] args) {
		objectsIni();
		printMainMenu();
	}
	private static void objectsIni(){
		robot = new Robot();					
		nav = new Navigate(robot, robot.odo);			
		crossbow = new Crossbow(robot);
	}

	/**
	 * handles the LIST MENU 
	 * Displays the options and shifts them when left or right button is pressed 
	 */
	public static void mainRight(){
		boolean display = true;
		String storeOption;
		while(display){
			int bPressed = 0;
			LCD.clear();
			storeOption = optionList[0];
			optionList[0] = ">>"+optionList[0];
			for (int i = 0; i< optionList.length && i <8; i++ ){
				LCD.drawString(optionList[i], 0, i);
			}
			while (bPressed == 0) {
				bPressed = Button.waitForAnyPress();
			}
			switch (bPressed) {
			case Button.ID_LEFT:
				optionList[0] = storeOption;
				String temp = optionList[optionList.length-1];
				for (int i = optionList.length-1 ; i >0 ; i--){
					optionList[i] = optionList[i-1];
				}
				optionList[0] = temp;
				break;
			case Button.ID_RIGHT:
				optionList[0] = storeOption;
				String temp2 = optionList[0];
				for (int i = 0 ; i < optionList.length-1; i++){
					optionList[i] = optionList[i+1];
				}
				optionList[optionList.length-1] = temp2;
				break;
			case Button.ID_ENTER:
				optionList[0] = storeOption;
				display = false;
				test.runOption(optionList[0]);
				break;
			default:
				System.out.println("Error - invalid button");
				System.exit(-1);
				break;
			}
		}		
	}

	/**
	 * prints main menu (initial display)
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






