import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class Robot {
	public NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B, armMotor = Motor.C;

	public UltrasonicSensor usLeftSensor = new UltrasonicSensor(SensorPort.S1), usRightSensor = new UltrasonicSensor(SensorPort.S2);

	public ColorSensor cs = new ColorSensor(SensorPort.S3);

	public double wwDist = 15.5;// less turns less & more turns more
	public double leftWradius = 2.07;//less travels more
	public double rightWradius = leftWradius;
	
	public double lsDist = 9;
	public double black = 380; 			// raw output of a black line being detected.
	
	public double wallDist = 14; 		// distance the robot has to be from a wall when following it.
	public double odoCorBand = 5; 		//radius of the error accepted to correct the odometer

	public double acc = 500;			// default acceleration
	public double speed = 250;			// default speed
	
	public int filterSize = 5;  		// how important is the filter for sensor values
	
	public Odometer odometer;			// Handles all data relative to location

	public Robot (){

		odometer = new Odometer(this);
		

	}

}
