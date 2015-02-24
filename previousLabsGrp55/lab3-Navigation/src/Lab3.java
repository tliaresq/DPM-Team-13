import lejos.nxt.*;

public class Lab3 {

	static double leftRadius = 2.08;
	static double rightRadius = leftRadius;
	static double width = 15.22;
	

	public static void main(String[] args) {
		int option = 0;
		//listens to button to exit program
		Exit exit= new Exit();
		//controls ultrasonic sensor and provides distance measurement
		USController usController = new USController();
		//Handles all data relative to location
		Odometer odometer = new Odometer(usController, width,leftRadius);
		//displays all odometer data
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
		//Manages driving nxt to a given location
		Navigate nav = new Navigate(Motor.A, Motor.B, leftRadius, rightRadius,
				width, odometer);
		
		exit.start();
		usController.start();
		odometer.start();
		nav.start();
		LCD.clear();
		LCD.drawString("left = demo 1",  0, 0);
		LCD.drawString("right = demo 2", 0, 1);
		while (option == 0){
			option = Button.waitForAnyPress();
		}
		switch(option) {
		case Button.ID_LEFT:
			odometryDisplay.start();
			nav.demoOne();
			break;
		case Button.ID_RIGHT:
			odometryDisplay.start();
			nav.demoTwo();
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
	}
}
