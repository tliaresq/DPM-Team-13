/*
 * Odometer.java
 */
import lejos.nxt.Motor;
import lejos.nxt.Motor.*;

public class Odometer extends Thread {

	

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;
	// robot position
	private double x, y, theta;
	private int nowTachoL;
	private int nowTachoR;
	private int lastTachoL;
	private int lastTachoR;
	
	private double myVar;
	
	private double wB;//nxt wheel to wheel distance
	private double wR;//wheel radius

	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 90.0;
		lock = new Object();
		wB = 14.70;
		wR = 2.1;
		myVar = 3.1415;

	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			// put (some of) your odometer code here

			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!

				double distL, distR, deltaD, deltaT, dX, dY;

				nowTachoL = Motor.A.getTachoCount();
				nowTachoR = Motor.B.getTachoCount();
				// getting distances travelled by the left and right wheel
				// respectively
				distL = 3.14159 * wR * (nowTachoL - lastTachoL) / 180;
				distR = 3.14159 * wR * (nowTachoR - lastTachoR) / 180;
				lastTachoL = nowTachoL;
				lastTachoR = nowTachoR;
				// getting the distance travelled by the nxt as a whole since
				// last update
				deltaD = 0.5 * (distL + distR);
				// getting change in angle since last update
				deltaT = Math.atan((distL - distR) / wB) * 360 / 6.2832;
				theta -= deltaT;

				// keeping theta in the interval [0-360]
				if (theta < 0) {
					theta += 360;
				} else if (theta > 360) {
					theta -= 360;
				}
				// incrementing x and y position with respect to theta
				dX = deltaD * Math.cos(theta * 2 * 3.14159 / 360);
				dY = deltaD * Math.sin(theta * 2 * 3.14159 / 360);
				x = x + dX;
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
	public double getV() {
		double result;

		synchronized (lock) {
			result = myVar;
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
	public void setV(double v) {
		synchronized (lock) {
			this.myVar = v;
		}
	}
}