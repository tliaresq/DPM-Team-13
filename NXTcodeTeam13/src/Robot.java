import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * A classe that store all the values that are likely to vary during testing 
 * It has no other purpose than to structure the code and make it adaptable and easy to access.
 * @author Cedric
 *
 */
public class Robot {
	public NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B, armMotor = Motor.C;

	public UltrasonicSensor usLeftSensor = new UltrasonicSensor(SensorPort.S1), usFrontSensor = new UltrasonicSensor(SensorPort.S2),usRightSensor = new UltrasonicSensor(SensorPort.S3);;

	public ColorSensor cs1 = new ColorSensor(SensorPort.S4), cs2 = new ColorSensor(SensorPort.S4);

	public double wwDist = 18.3;// less turns less & more turns more
	public double leftWradius = 2.07;//less travels more
	public double rightWradius = leftWradius;
	
	public double lsDist = 9;
	public double black = 400; 			// raw output of a black line being detected.
	
	public double minFrontWallDist = 9; 		// distance the robot has to be from a wall when following it.
	public double followerSideDist = 16;
	public double odoCorBand = 6; 		//radius of the error accepted to correct the odometer
	public double findWallDist = 40;	//
	public double shootAngle1 = 90;
	public double shootAngle2 = 0;

	public double acc = 500;			// default acceleration
	public double speed = 250;			// default speed
	
	public int usFilterSize = 20;  		// how important is the filter for sensor values
	public int lsFilterSize = 10;
	public Odometer odo;			// Handles all data relative to location
	

	public Robot (){
		odo = new Odometer(this);
	}

}
