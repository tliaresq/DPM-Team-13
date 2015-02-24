import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {

	private final int bandCenter, bandwith;
	private final int motorStraight = 200, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	private boolean backwards;
	private boolean inBand;

	public PController(int bandCenter, int bandwith) {
		// Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
		inBand = true;
		backwards = false;
	}

	/**
	 * author: CEDRIC BONJOUR TODO: process a movement based on the us distance
	 * passed in (P style)
	 */
	@Override
	public void processUSData(int distance) {
		// rudimentary filter
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the
			// filter value
			filterControl++;
		} else if (distance == 255) {
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		if (distance >= (bandCenter - bandwith)
				&& distance <= (bandCenter + bandwith)) {
			inBand = true;
		} else {
			inBand = false;
		}
		if (distance < 18) {
			sharpTurn();
		}
		if (backwards) {
			sharpTurn();
		} else {
			if (distance > 17 && distance < (bandCenter - bandwith)) {
				leftMotor.setSpeed(motorStraight * 2);
				rightMotor.setSpeed(distance * 20);
			}
			if (inBand) {
				leftMotor.setSpeed(motorStraight * 2);
				rightMotor.setSpeed(motorStraight * 2);
			}
			if (distance > bandCenter + bandwith && distance < 60) {
				rightMotor.setSpeed(motorStraight * 2);
				leftMotor.setSpeed(360 - distance * 6);
			}
			if (distance > 59) {
				rightMotor.setSpeed(motorStraight * 2);
				leftMotor.setSpeed(motorStraight);
			}
		}
	}

	private void sharpTurn() {
		backwards = true;
		if (distance < 21) {
			rightMotor.backward();
			rightMotor.setSpeed(motorStraight * 2);
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
