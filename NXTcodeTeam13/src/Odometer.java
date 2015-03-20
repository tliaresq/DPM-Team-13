import lejos.nxt.Motor;
/**
 * Basic odometer
 * Also store all the sensor values for any class to call and use when necessary.
 * @author Cedric
 *
 */
public class Odometer extends Thread {

	private static final long ODOMETER_PERIOD = 25;	// odometer update period, in ms
	private Object lock;
	private double x, y, theta;
	private int nowTachoL, nowTachoR, lastTachoL, lastTachoR;
	private int sensorLeftDist, sensorRightDist, sensorFrontDist, sensorColor;
	

	private OdometryDisplay odometryDisplay = new OdometryDisplay(this); // displays all odometer data
	private OdoCorrection odoCorrect;
	public Robot robot;

	public USController usCleft, usCfront,usCright;
	public LSController lsC;//,lsC2;

	
	public Odometer(Robot r) {
		lock = new Object();
		robot = r;
		lsC = new LSController(robot.cs1, robot.lsFilterSize);
		odoCorrect = new OdoCorrection(this);
		usCleft = new USController(robot.usLeftSensor, robot.usFilterSize);
		usCfront  = new USController(robot.usFrontSensor,robot.usFilterSize);
		usCright  = new USController(robot.usRightSensor,robot.usFilterSize);

		x = 0.0;
		y = 0.0;
		theta = 90.0;

	}

	
	public void run() {
		long updateStart, updateEnd;
		//=====================================================
		//       start only the sensors available for run
		//=====================================================
		odometryDisplay.start();
		lsC.start();
		usCleft.start();
		usCfront.start();
		usCright.start();
		odoCorrect.start();
		

		while (true) {
			updateStart = System.currentTimeMillis();
			// put (some of) your odometer code here

			synchronized (lock) {

				// don't use the variables x, y, or theta anywhere but here!

				double distL, distR, deltaD, deltaT, dX, dY;

				sensorLeftDist = usCleft.sensorDist();
				sensorFrontDist = usCfront.sensorDist();
				sensorRightDist  = usCright.sensorDist();
				sensorColor = lsC.getColor();

				nowTachoL = Motor.A.getTachoCount();
				nowTachoR = Motor.B.getTachoCount();
				// getting distances travelled by the left and right wheel
				// respectively
				distL = 3.14159 * robot.leftWradius* (nowTachoL - lastTachoL) / 180;
				distR = 3.14159 * robot.leftWradius* (nowTachoR - lastTachoR) / 180;
				lastTachoL = nowTachoL;
				lastTachoR = nowTachoR;
				// getting the distance travelled by the nxt as a whole since
				// last update
				deltaD = 0.5 * (distL + distR);
				// getting change in angle since last update
				deltaT = Math.atan((distL - distR) / robot.wwDist) * 360 / 6.2832;
				theta -= deltaT;

				// keeping theta in the interval [0-360]
				if (theta < 0) {
					theta += 360;
				} else if (theta > 360) {
					theta -= 360;
				}
				// incrementing x and y position with respect to theta
				dX = deltaD * Math.cos(theta * 3.14159 / 180);
				dY = deltaD * Math.sin(theta * 3.14159 / 180);
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
	

	public void correctionOff(){
		odoCorrect.stop();
	}
	public void correctionOn(){
		odoCorrect.restart();
	}
	

	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0]) position[0] = x;
			if (update[1]) position[1] = y;
			if (update[2]) position[2] = theta;
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

	public double getLeftSensorDist() {
		double result;
		synchronized (lock) {
			result = sensorLeftDist;
		}
		return result;
	}
	public double getRightSensorDist() {
		double result;
		synchronized (lock) {
			result = sensorRightDist;
		}
		return result;
	}
	public double getFrontSensorDist() {
		double result;

		synchronized (lock) {
			result = sensorFrontDist;
		}

		return result;
	}

	public double getSensorColor() {
		double result;

		synchronized (lock) {
			result = sensorColor;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0]) x = position[0];
			if (update[1]) y = position[1];
			if (update[2]) theta = position[2];
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