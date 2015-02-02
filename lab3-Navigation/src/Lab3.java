import lejos.nxt.*;

public class Lab3 {
	static double leftRadius = 2.1;
	static double rightRadius = 2.1;
	static double width = 14.70;

	public static void main(String[] args) {
		Odometer odometer = new Odometer();
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
		Navigate nav = new Navigate(Motor.A, Motor.B, leftRadius, rightRadius,
				width, odometer, odometryDisplay);
		odometer.start();
		odometryDisplay.start();
		nav.start();
		

		while (Button.waitForAnyPress() != Button.ID_ESCAPE)
			; // error in origin file : used to be "Button.waitForPress()"
		System.exit(0);
	}
}
