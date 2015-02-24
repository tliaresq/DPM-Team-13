import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class Robot {
	public NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B, armMotor = Motor.C;

	public UltrasonicSensor usLeftSensor = new UltrasonicSensor(SensorPort.S1), usRightSensor = new UltrasonicSensor(SensorPort.S2);

	public ColorSensor cs = new ColorSensor(SensorPort.S3);

	public LSController ls = new LSController(cs);
	public USController usLeft = new USController(usLeftSensor), usRight = new USController(usRightSensor); 


	public double wwDist = 15.5;// less turns less & more turns more
	public double leftWradius = 2.07;//less travels more
	public double rightWradius = leftWradius;
	public double lsDist = 8;

	public double black = 300; 		// raw output of a black line being detected.
	public double wallDist = 25; 		// distance the robot has to be from a wall when following it.

	public int defAcc = 300;
	public int defSpeed = 200;

	public Robot (){
		ls.start();
		usLeft.start();
		usRight.start();
	}

}
