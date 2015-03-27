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
	public UltrasonicSensor usLeftSensor = new UltrasonicSensor(SensorPort.S1), usFrontSensor = new UltrasonicSensor(SensorPort.S2),usRightSensor = new UltrasonicSensor(SensorPort.S4);;
	public ColorSensor cs1 = new ColorSensor(SensorPort.S3);
	public Odometer odo;			// Handles all data relative to location
	/*
	 * ===============================================
	 * 				HARDWARE
	 *  ==============================================
	 */
	public double wwDist = 15.5; // less turns less & more turns more
	public double leftWradius = 2.06;//less travels more  
	public double rightWradius = leftWradius ;    
	public double lsDist = 8;

	/*
	 * ===============================================
	 * 				WALL FOLLOWER
	 *  ==============================================
	 */
	public double followAcc = 1000;
	public double followSpeed = 100;
	public double noWallDistance = 40;
	public double minFrontWallDist = 11; 		// distance the robot has to be from a wall when following it.
	public double followerSideDist = 18;

	/*
	 * ===============================================
	 * 				ODOMETRY CORRECTION
	 *  ==============================================
	 */
	public double odoCorBand = 5; 		//radius of the error accepted to correct the odometer


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
	public double acc = 700;			// default acceleration
	public double speed = 150;			// default speed
	/*
	 * ===============================================
	 * 				FILTERS
	 *  ==============================================
	 */
	public int usFilterSize = 10;  		// how important is the filter for sensor values
	public int lsFilterSize = 4;
	 			// this value needs to be in between the raw output of black and wood of the color sensor




	public Robot (){
		//leftMotor = MirrorMotor.invertMotor(Motor.A);

		odo = new Odometer(this);
	}

}
