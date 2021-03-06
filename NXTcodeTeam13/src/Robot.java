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
	public UltrasonicSensor usLeftSensor = new UltrasonicSensor(SensorPort.S1), usFrontSensor = new UltrasonicSensor(SensorPort.S2);
	public ColorSensor csM = new ColorSensor(SensorPort.S3),csR = new ColorSensor(SensorPort.S4);
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
	public double lsDistR = 4.6;
	public double lsrAngle = Math.atan2(lsDistR, lsDist);
	public double lsrDist = Math.sqrt(lsDist*lsDist+lsDistR*lsDistR);
	

	/*
	 * ===============================================
	 * 				WALL FOLLOWER
	 *  ==============================================
	 */
	public double followAcc = 500;
	public double followSpeed = 200;
	public double noWallDistance = 40;
	public double minFrontWallDist = 12; 		// distance the robot has to be from a wall when following it.
	public double followerSideDist = 18;

	/*
	 * ===============================================
	 * 				ODOMETRY CORRECTION
	 *  ==============================================
	 */
	public double odoCorBand = 7; 		//radius of the error accepted to correct the odometer


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
	public int usFilterSize = 5;  		// how important is the filter for sensor values
	public int lsFilterSize = 3;
	public int blackM = 500;
	public int blackR = 500;            // this value needs to be in between the raw output of black and wood of the color sensor




	public Robot (){
		odo = new Odometer(this);
	}

}
