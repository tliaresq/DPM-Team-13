import lejos.nxt.*;

public class Lab3 {
	static int xDest = 60;
	static int yDest = -30;
	static double leftRadius = 2.1;
	static double rightRadius = 2.1;
	static double width = 14.70;
	
	
	public static void main(String[] args) {
		Odometer odometer = new Odometer();
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);

		odometer.start();
		odometryDisplay.start();
		(new Thread() {
			public void run() {
						Navigate.drive(xDest, yDest, Motor.A, Motor.B, leftRadius, rightRadius, width);		
			}
		}).start();
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE); // error in origin file : used to be "Button.waitForPress()"
		System.exit(0);	
	}
}
