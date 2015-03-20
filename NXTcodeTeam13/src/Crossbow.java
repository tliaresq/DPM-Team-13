import lejos.nxt.NXTRegulatedMotor;


/**
 * Manages shooting Ping Pong balls
 * @author Cedric
 *
 */
public class Crossbow {
	private NXTRegulatedMotor motor;
	public Crossbow(Robot r){
		motor = r.armMotor;	
	}
	
	
	/**
	 * Makes the motor rotate "n" revolutions to shoot "n" times
	 * @param n
	 */
	public void shoot(int n){
		for(int i = 0; i< n; i++ ){
			shoot();
		}
	}
	
	
	/**
	 * Makes the motor rotate "1" revolutions to shoot "1" times
	 */
	private void shoot(){
		motor.setAcceleration(6000);
		motor.setSpeed(150);
		motor.rotate(360);
	
	}

}
