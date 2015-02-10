import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 30;

	private Odometer odo;
	private Navigate nav;
	//private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	private double sensorDist ;
	private int wallDist;
	private int counter ;
	private int maxCount;
	
	
	public USLocalizer(Odometer odo, Navigate navigation, LocalizationType locType) {
		this.odo = odo;
		this.locType = locType;
		nav = navigation;
		sensorDist = 50;
		counter = 0;
		maxCount =22;
		wallDist = 30;
	}
	
	
	
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		odo.usStart();
		
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
			counter = 0;
			nav.spinClockWise();
			do{
				sensorDist = odo.getSensorDist();
				try { Thread.sleep(20); } catch(Exception e){}
				if(sensorDist>=wallDist){counter ++;}else{counter = 0;}
			}while(counter <maxCount);
			
			nav.stopMotors();
		
			
			// keep rotating until the robot sees a wall, then latch the angle
			counter =0;
			nav.spinClockWise();
			do{
				sensorDist = odo.getSensorDist();
				if(sensorDist<=wallDist){counter ++;}else{counter = 0;}
			}while(counter<maxCount);
			
			nav.stopMotors();
			odo.setTheta(0);
			angleA =0;// odo.getTheta();
			
			// switch direction and wait until it sees no wall
			counter =0;
			nav.spinCounterClockWise();
			do{
				sensorDist = odo.getSensorDist();
				if(sensorDist>wallDist){counter ++;}else{counter = 0;}
			}while(counter <maxCount);
			
			
			nav.stopMotors();
			// keep rotating until the robot sees a wall, then latch the angle
			counter = 0 ;
			nav.spinCounterClockWise();
			do{
				sensorDist = odo.getSensorDist();
				if(sensorDist<wallDist){counter ++;}else{counter = 0;}
			}while(counter<maxCount);
			
			nav.stopMotors();
			angleB = odo.getTheta();
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			double alpha = -45+angleB/2;
			nav.rotateClockwise(alpha);
			
			
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, 90.0}, new boolean [] {true, true, true});
		} else {
			wallDist = 35;
			
			// rotate the robot until it sees no wall
						counter = 0;
						nav.spinCounterClockWise();
						do{
							sensorDist = odo.getSensorDist();
							if(sensorDist<=wallDist){counter ++;}else{counter = 0;}
							try { Thread.sleep(20); } catch(Exception e){}
						}while(counter <maxCount);
						
						// keep rotating until the robot sees a wall, then latch the angle
						counter =0;
						nav.spinCounterClockWise();
						do{
							sensorDist = odo.getSensorDist();
							if(sensorDist>=wallDist){counter ++;}else{counter = 0;}
						}while(counter<maxCount);
						
						nav.stopMotors();
						odo.setTheta(0);
						angleA = odo.getTheta();
						
						// switch direction and wait until it sees no wall
						counter =0;
						nav.spinClockWise();
						do{
							sensorDist = odo.getSensorDist();
							if(sensorDist<wallDist){counter ++;}else{counter = 0;}
						}while(counter <maxCount);
						
						
						nav.stopMotors();
						// keep rotating until the robot sees a wall, then latch the angle
						counter = 0 ;
						nav.spinClockWise();
						do{
							sensorDist = odo.getSensorDist();
							if(sensorDist>wallDist){counter ++;}else{counter = 0;}
						}while(counter<maxCount);
						
						nav.stopMotors();
						angleB = odo.getTheta();
						// angleA is clockwise from angleB, so assume the average of the
						// angles to the right of angleB is 45 degrees past 'north'
						double alpha =angleB/2-45;
						nav.rotateClockwise(alpha);
						
						
						// update the odometer position (example to follow:)
						odo.setPosition(new double [] {0.0, 0.0, 90.0}, new boolean [] {true, true, true});
					
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//
			// FILL THIS IN
			//
		}
	}
}
