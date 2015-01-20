import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 200;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int currentLeftSpeed;
	private int wideTurn;
	private int i;
	private boolean backwards;
	private boolean inBand;
	
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
	 * author: CEDRIC BONJOUR
	 * TODO: process a movement based on the us distance passed in (BANG-BANG style)
	 */
	@Override
	public void processUSData(int distance) {
	
		this.distance = distance;
		if (distance >= (bandCenter - bandwith)
				&& distance <= (bandCenter + bandwith)) {
			inBand = true;
		} else {
			inBand = false;
		}

		if (distance < 14) {
			sharpTurn();
		}

		if (backwards) {
			sharpTurn();
		} else {
			if (distance > 13 && distance < (bandCenter - bandwith)) {
				leftMotor.setSpeed(motorHigh);
				rightMotor.setSpeed(motorStraight);
			}
			if (inBand) {
				leftMotor.setSpeed(motorHigh);
				rightMotor.setSpeed(motorHigh);
			}
			if (distance > bandCenter + bandwith && distance < 60) {
				rightMotor.setSpeed(motorHigh);
				leftMotor.setSpeed(motorStraight);
			}

			if (distance > 59) {
				i++;
				if (wideTurn < 150 && i > 100) {
					wideTurn += 100;
				}
				rightMotor.setSpeed(motorHigh);
				leftMotor.setSpeed(motorStraight);
			} else {
				i = 0;
				wideTurn = 0;
			}

		}
		
	}

	private void sharpTurn() {
		backwards = true;
		if (distance < 16) {
			rightMotor.backward();
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
		} else {
			backwards = false;
			rightMotor.forward();
		}

	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
