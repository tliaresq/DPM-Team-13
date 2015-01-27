/*
 * Odometer.java
 */
import lejos.nxt.Motor;
import lejos.nxt.Motor.*;

public class Odometer extends Thread {
	// robot position
	private double x, y, theta;
	private double temp, tempX, tempY, tempTacho, tempTheta, wheelPerimeter,
			nxtRadius, rad;
	boolean turning;

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;
	
	
	private int nowTachoL;
	private int nowTachoR;
	private int lastTachoL;
	private int lastTachoR;
	private double wB;
	private double wR;

	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		temp = 0;
		tempX = 0;
		tempY = 0;
		tempTacho = 0;
		tempTheta = 0;
		wheelPerimeter = 13.35;
		nxtRadius = 7.56;
		rad = 0;
		turning = false;
		lock = new Object();
		
		wB = 15.08;
		wR= 2.1;
		

	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			// put (some of) your odometer code here

			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!

				// Motor.A.getTachoCount();
/*
				if (Motor.B.getSpeed() < 170) {
					tempX = x;
					tempY = y;
					tempTacho = Motor.A.getTachoCount();
					turning = true;
					theta = tempTheta
							- ((temp - Motor.B.getTachoCount())
									* wheelPerimeter / 360) * 360
							/ (2 * 3.1415 * nxtRadius);

				} else {
					turning = false;
					temp = Motor.B.getTachoCount();
					tempTheta = theta;
					rad = Math.toRadians(theta);
					x = tempX + Math.cos(rad)
							* (Motor.A.getTachoCount() - tempTacho)
							* wheelPerimeter / 360;
					y = tempY + Math.sin(rad)
							* (Motor.A.getTachoCount() - tempTacho)
							* wheelPerimeter / 360;

				}
				
				*/
				
				double distL, distR, deltaD, deltaT, dX, dY;
				
				nowTachoL = Motor.A.getTachoCount();      // get tacho counts 
				nowTachoR = Motor.B.getTachoCount();
				distL = 3.14159*wR*(nowTachoL-lastTachoL)/180;
				distR = 3.14159*wR*(nowTachoR-lastTachoR)/180;
				lastTachoL=nowTachoL;
				lastTachoR=nowTachoR;
				deltaD = 0.5*(distL+distR);
				deltaT = Math.atan((distL-distR)/wB)*360/6.2832;             //    delta X, Y, Theta 
				theta += deltaT;
			    dX = deltaD * Math.sin(theta*2*3.14159/360);
				dY = deltaD * Math.cos(theta*2*3.14159/360);
				x = x + dX;                                      // Update
				y = y + dY;
				
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}

	}

	// accessors
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}