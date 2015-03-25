import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;

/**
 * A classe that store all the values that are likely to vary during testing 
 * It has no other purpose than to structure the code and make it adaptable and easy to access.
 * @author Cedric
 *
 */
public class Robot {
	public RegulatedMotor leftMotor =  MirrorMotor.invertMotor(Motor.B);
	public RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.C);
	public NXTRegulatedMotor armMotor = Motor.A;
	public UltrasonicSensor usLeftSensor = new UltrasonicSensor(SensorPort.S1), usFrontSensor = new UltrasonicSensor(SensorPort.S2),usRightSensor = new UltrasonicSensor(SensorPort.S3);;
	public ColorSensor cs1 = new ColorSensor(SensorPort.S4), cs2 = new ColorSensor(SensorPort.S4);
	public Odometer odo;			// Handles all data relative to location
	/*
	 * ===============================================
	 * 				HARDWARE
	 *  ==============================================
	 */
	public double wwDist = 15.4; // less turns less & more turns more
	public double leftWradius = 2.09;//less travels more  
	public double rightWradius = 2.09 ;    
	public double lsDist = 9;

	/*
	 * ===============================================
	 * 				WALL FOLLOWER
	 *  ==============================================
	 */
	public double wallFollowAcc = 2000;
	public double wallFollowSpeed = 150;
	public double noWallDistance = 40;
	public double minFrontWallDist = 11; 		// distance the robot has to be from a wall when following it.
	public double followerSideDist = 20;

	/*
	 * ===============================================
	 * 				ODOMETRY CORRECTION
	 *  ==============================================
	 */
	public double odoCorBand = 6; 		//radius of the error accepted to correct the odometer


	/*
	 * ===============================================
	 * 				TARGETTING
	 *  ==============================================
	 */
	public double shootAngle1 = 90;
	public double shootAngle2 = 0;

	/*
	 * ===============================================
	 * 				NAVIGATION
	 *  ==============================================
	 */
	public double acc = 100;			// default acceleration
	public double speed = 100;			// default speed
	/*
	 * ===============================================
	 * 				FILTERS
	 *  ==============================================
	 */
	public int usFilterSize = 20;  		// how important is the filter for sensor values
	public int lsFilterSize = 5;
	 			// this value needs to be in between the raw output of black and wood of the color sensor




	public Robot (){
		//leftMotor = MirrorMotor.invertMotor(Motor.A);

		odo = new Odometer(this);
	}

}
