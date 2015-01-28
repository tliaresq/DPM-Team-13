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
	private double x,y;
	
	// constructor
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;

		while (true) {
			correctionStart = System.currentTimeMillis();
		
			cs.setFloodlight(true);
			
			iColor=cs.getRawLightValue();			
			//odometer.setX((double) iColor);
			//odometer.setY(i);
			
			// when a black line is updated check the current position and if it is within j cm of any preset position, updated to that position.
			if (iColor <300){
				
				x=0;
				y=27.5;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}

				x=0;
				y=57.5;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}

				x=27.5;
				y=60.96;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}

				x=57.5;
				y=60.96;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}

				x=60.96;
				y=33.46;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}

				x=60.96;
				y=3.46;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}

				x=33.46;
				y=0;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}
				x=3.46;
				y=0;
				if(inBand(odometer.getX() , x) && inBand(odometer.getY() , y) ){
					 odometer.setX(x);
					 odometer.setY(y);
				}
			
			}
			
			// put your correction code here

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
	private boolean inBand(double val, double pos){
		int j = 10;//distance error permitted between a preset position and the odometer theoretical position for update 
		if (val>(pos-j) && val< (pos+j)){
			return true;
		}else{
			return false;
		}
	
	}
}