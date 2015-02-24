import lejos.nxt.*;

public class MyLab4 {

	static double leftRadius = 2.08;
	static double rightRadius = leftRadius;
	static double width = 15.02;

	public static void main(String[] args) {
		int option = 0;
		// listens to button to exit program
		Exit exit = new Exit();

		// Handles all data relative to location
		Odometer odometer = new Odometer(width, leftRadius);

		// Manages driving nxt to a given location
		Navigate nav = new Navigate(Motor.A, Motor.B, leftRadius, rightRadius,
				width, odometer);
		
		LauncherController lc = new LauncherController(Motor.C);

		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odometer, nav,
				USLocalizer.LocalizationType.RISING_EDGE);

		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odometer, nav);

		exit.start();
		nav.start();
		LCD.clear();
		LCD.drawString("left = demo 1", 0, 0);
		LCD.drawString("right = demo 2", 0, 1);
		while (option == 0) {
			option = Button.waitForAnyPress();
		}
		switch (option) {
		case Button.ID_LEFT:
			odometer.start();
			usl = new USLocalizer(odometer, nav,
					USLocalizer.LocalizationType.FALLING_EDGE);
			usl.doLocalization();
			lsl.doLocalization();

			break;
		case Button.ID_RIGHT:
			lc.ballisticsMenu();
			
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
	}
}
