import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 200;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int currentLeftSpeed;
	
	public BangBangController(int bandCenter, int bandwith, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
	}
	

	/**
	 * CEDRIC BONJOUR method
	 * TODO: process a movement based on the us distance passed in (BANG-BANG style)
	 */
	@Override
	public void processUSData(int distance) {
	
		this.distance = distance;
		
		if(distance==25){
		leftMotor.setSpeed(motorHigh);
		rightMotor.setSpeed(motorHigh);
		}
		if (distance<25){
			rightMotor.setSpeed(50);
			leftMotor.setSpeed(250);
			
		}
		
		if (distance>25 && distance<200){
		leftMotor.setSpeed(50); 
		rightMotor.setSpeed(250);
		}
		if (distance >140){
		leftMotor.setSpeed(50);
		rightMotor.setSpeed(motorStraight);
		}
		
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
