/* 
 * OdometryCorrection.java
 */
import lejos.nxt.ColorSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SoundSensor.*;
import lejos.nxt.SoundSensor;

public class OdometryCorrection extends Thread {
	static ColorSensor cs = new ColorSensor(SensorPort.S2);
	private int lightDetector;

	private static final long CORRECTION_PERIOD = 10;
	private Odometer odometer;
	private int iColor;
	private double x, y;
	private double dX, dY;
	
	private int i;

	// constructor
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
		dY = 0;
		dX = 0;
		i=0;
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		
		while (true) {
			
			
			correctionStart = System.currentTimeMillis();
			dY = 4.5 * Math.sin(odometer.getTheta()*3.1416/180); // light sensor is 4.5cm
														// away from the center
														// of robot
			dX = 4.5 * Math.cos(odometer.getTheta()*3.1416/180);
			cs.setFloodlight(true);
			iColor = cs.getRawLightValue();

			//odometer.setV((double)iColor);
			// when a black line is updated check the current position and if it
			// is within j cm of any preset position, updated to that position.
			if (iColor < 250) {

				i++;
				//odometer.setV((double)i);
				y = 15 - dY;
				odometer.setV(y);
				if (inBand(odometer.getY(), y)) {
					odometer.setY(y);
				}

				y = 45 - dY;
				if (inBand(odometer.getY(), y)) {
					odometer.setY(y);
				}

				y = 75 - dY;
				if (inBand(odometer.getY(), y)) {
					odometer.setY(y);
				}

				x = 15 - dX;
				if (inBand(odometer.getX(), x)) {
					odometer.setX(x);
				}
				x = 45 - dX;
				if (inBand(odometer.getX(), x)) {
					odometer.setX(x);
				}
				x = 75 - dX;
				if (inBand(odometer.getX(), x)) {
					odometer.setX(x);
				}
			}

			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}

	private boolean inBand(double val, double pos) {
		int j = 8;// distance error permitted between a preset position and the
					// odometer theoretical position for update
		if (val > (pos - j) && val < (pos + j)) {
			return true;
		} else {
			return false;
		}

	}
}